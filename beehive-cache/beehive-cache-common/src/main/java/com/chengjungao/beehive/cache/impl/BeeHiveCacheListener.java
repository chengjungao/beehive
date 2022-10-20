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
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.redisson.client.codec.StringCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chengjungao.beehive.cache.CacheListener;
import com.chengjungao.beehive.cache.config.CacheConfig;
import com.chengjungao.beehive.cache.redis.RedisFactory;

public class BeeHiveCacheListener implements CacheListener {
	private static final Logger LOG = LoggerFactory.getLogger(BeeHiveCacheListener.class);
	
	private static final String LOCK = "%s-Lock:%s";
	private static final String LISTENER = "%s-Listener:%s";
	
	private CacheConfig config;
	private RedissonClient redissonClient;
	private BlockingQueue<String> waitedRefresh;
	private int rollbackRefreshMs;
	private int refreshIntervalMs;
	
	private ExecutorService relaodPool;

	public BeeHiveCacheListener(CacheConfig config) {
		this.config = config;
		try {
			this.redissonClient = RedisFactory.getClient(config.getRedisConfig());
		} catch (Exception e) {
			LOG.error("init cache redis error!", e);
		}
		this.relaodPool = Executors.newFixedThreadPool(config.getReloadThread());
	}

	private void refreshListenedKeys(String listenerTime) throws Exception{
		if (this.redissonClient == null){
			return;
		}
		
		String listenerKey = String.format(LISTENER, config.getBusiness(),listenerTime);
		
		RSet<String> rset = redissonClient.getSet(listenerKey,StringCodec.INSTANCE);
		Set<String> refreshKeys	= rset.readAll();
		if (refreshKeys == null || refreshKeys.isEmpty()) {
			return;
		}
		
		RedissonLock lock = (RedissonLock) redissonClient.getLock(String.format (LOCK, config.getBusiness(),listenerTime));
		Boolean lockresult = lock.tryLock(config.getRefreshIntervalMs(), TimeUnit.MILLISECONDS);
		if (lockresult && redissonClient.getBucket(listenerKey).isExists()) {
			Long nextRefreshDate = System.currentTimeMillis () + config.getRefreshAfterWriteMs();
			Set<String> nextRefreshKeys = new HashSet<>(refreshKeys);
			try {
				Map<String, byte[]> values = redissonClient.getBuckets(ByteArrayCodec.INSTANCE).get(refreshKeys.toArray(new String[refreshKeys.size()]));
				List<Future<Boolean>> futures = new ArrayList<>();
				for (Entry<String, byte[]> entry : values.entrySet()) {
					if (entry.getValue () != null && entry.getValue().length != 0) {
						futures.add(relaodPool.submit(new Callable<Boolean>() {
							@Override
							public Boolean call() throws Exception {
								
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
						RSet<String> rSet = redissonClient.getSet(nextListenerKey, StringCodec.INSTANCE);
						rSet.addAll(nextRefreshKeys);
					}
					redissonClient.getBucket(listenerKey).delete();
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
					refreshTime += refreshIntervalMs;
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
