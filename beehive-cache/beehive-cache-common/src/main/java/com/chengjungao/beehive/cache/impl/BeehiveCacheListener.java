package com.chengjungao.beehive.cache.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.redisson.RedissonLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chengjungao.beehive.cache.CacheListener;
import com.chengjungao.beehive.cache.CacheLoader;
import com.chengjungao.beehive.cache.RedisCacheHandler;
import com.chengjungao.beehive.cache.Value;
import com.chengjungao.beehive.cache.config.CacheConfig;
import com.chengjungao.beehive.cache.redis.RedisFactory;

/**
 * Beehive Cache监听器实现类
 * @author wolf
 *
 * @param <K>
 * @param <V>
 */
public class BeehiveCacheListener<K,V> implements CacheListener<K,V> {
	private static final Logger LOG = LoggerFactory.getLogger(BeehiveCacheListener.class);
	
	private static final String LOCK = "%s-Lock:%s";
	private static final String LISTENER = "%s-Listener:%s";
	
	private CacheConfig config;
	private RedisCacheHandler<K, V> redisCacheHandler;
	private CacheLoader<K, V> loader;
	
	private BlockingQueue<String> waitedRefresh;
	private int rollbackRefreshMs;
	private int refreshIntervalMs;
	
	private ExecutorService reloadPool;

	public BeehiveCacheListener(CacheConfig config,CacheLoader<K, V> loader) {
		this.config = config;
		this.loader = loader;
		this.rollbackRefreshMs = config.getRollbackRefreshMs();
		this.refreshIntervalMs = config.getRefreshIntervalMs();
		this.waitedRefresh = new ArrayBlockingQueue<>(config.getMaxWaitedRefreshSize());
		try {
			this.redisCacheHandler = RedisFactory.getClient(config.getRedisConfig());
		} catch (Exception e) {
			LOG.error("init cache redis error!", e);
		}
		this.reloadPool = Executors.newFixedThreadPool(config.getReloadThread());
	}

	private void refreshListenedKeys(String listenerTime) throws Exception{
		if (this.redisCacheHandler == null){
			return;
		}
		
		String listenerKey = String.format(LISTENER, config.getBusiness(),listenerTime);
		Set<String> refreshKeys = redisCacheHandler.smembers(listenerKey);
		if (refreshKeys == null || refreshKeys.isEmpty()) {
			return;
		}
		
		RedissonLock lock = (RedissonLock) redisCacheHandler.getLock(String.format (LOCK, config.getBusiness(),listenerTime));
		Boolean lockresult = lock.tryLock(config.getRefreshIntervalMs(), TimeUnit.MILLISECONDS);
		
		if (lockresult && redisCacheHandler.exists(listenerKey)) {
			Long nextRefreshDate = System.currentTimeMillis () + config.getRefreshAfterWriteMs();
			Set<String> nextRefreshKeys = new HashSet<>(refreshKeys);
			try {
				Map<String, Value<K, V>> values = redisCacheHandler.mget(refreshKeys);
				List<Future<Boolean>> futures = new ArrayList<>();
				for (Entry<String, Value<K, V>> entry : values.entrySet()) {
					if (entry.getValue () != null && entry.getValue().isValid()) {
						futures.add(reloadPool.submit(new Callable<Boolean>() {
							@Override
							public Boolean call() throws Exception {
								redisCacheHandler.refresh(config.getBusiness(),new BeehiveCacheKey<>(entry.getValue().getDataKey()),
										loader);
								return true;
							}
						}));
					}else {
						nextRefreshKeys.remove(entry.getKey());
					}
				}
				for (Future<Boolean> future : futures) {
					  future.get();
					}
				}finally {
					String nextListenerKey = String.format(LISTENER, config.getBusiness() , String.valueOf(nextRefreshDate/refreshIntervalMs) );
					if (!nextRefreshKeys.isEmpty()) {
						redisCacheHandler.sadd(nextListenerKey, nextRefreshKeys);
						
					}
					redisCacheHandler.delete(listenerKey);
					lock.unlock();
					lock.delete();
				}
		}
	}

	@Override
	public void startListen() {
		this.waitedRefresh = new ArrayBlockingQueue<>(config.getMaxWaitedRefreshSize());
		
		//produce listener key
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				long refreshTime = (System.currentTimeMillis() - rollbackRefreshMs) / refreshIntervalMs;
				while (true) {
					try {
						waitedRefresh.put(String.valueOf(refreshTime));
						Thread.sleep(refreshIntervalMs);
					} catch (InterruptedException e) {
						
					}
					refreshTime += 1;
				}
			}
		},"Produce-Listener-Key").start();
		
		//consumer listener key , refresh cache
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					String time = waitedRefresh.take();
					refreshListenedKeys(time);
				} catch (Exception e) {}
				
			}
		},"Refresh-Cache").start();
	}

}
