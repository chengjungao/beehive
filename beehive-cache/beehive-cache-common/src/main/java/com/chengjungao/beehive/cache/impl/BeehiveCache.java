package com.chengjungao.beehive.cache.impl;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chengjungao.beehive.cache.Cache;
import com.chengjungao.beehive.cache.CacheListener;
import com.chengjungao.beehive.cache.CacheLoader;
import com.chengjungao.beehive.cache.CacheStats;
import com.chengjungao.beehive.cache.Key;
import com.chengjungao.beehive.cache.RedisCacheHandler;
import com.chengjungao.beehive.cache.Value;
import com.chengjungao.beehive.cache.config.CacheConfig;
import com.chengjungao.beehive.cache.exception.CacheServerException;
import com.chengjungao.beehive.cache.redis.RedisFactory;

public class BeehiveCache<K,V> implements Cache<K,V> {
	private static final Logger LOG = LoggerFactory.getLogger(BeehiveCache.class);
	
	private static final String LISTENER = "%s-Listener:%s";
	private CacheConfig cacheConfig;
	private CacheLoader<K, V> cacheLoader;
	private RedisCacheHandler<K, V> redisCacheHandler;
	private ExecutorService writeRedisPool;
	
	private String business;
	private int expireAfterAccessMs;
	private int refreshIntervalMs;
	private CacheStats cacheStats;
	private CacheListener<K,V> listener;
	
	public BeehiveCache(CacheConfig config,CacheLoader<K, V> cacheLoader) {
		this.cacheConfig = config;
		this.business = config.getBusiness();
		this.expireAfterAccessMs = config.getExpireAfterAccessMs();
		this.refreshIntervalMs = config.getRefreshIntervalMs();
		
		this.cacheLoader = cacheLoader;
		this.writeRedisPool = Executors.newFixedThreadPool(config.getSyncRedisThread());
		this.cacheStats = new CacheStats();
		
		try {
			this.redisCacheHandler = RedisFactory.getClient(config.getRedisConfig());
		} catch (Exception e) {
			LOG.error("init cache redis error!", e);
		}
		listener = new BeeHiveCacheListener<>(cacheConfig,cacheLoader);
		listener.startListen();
	}

	@Override
	public Value<K, V> get(Key<K> key) throws Exception {
		if (redisCacheHandler == null) {
			try {
				return new BeehiveCacheValue<K,V>(key.getDatum(),cacheLoader.reload(key.getDatum()));
			} catch (Exception e) {
				throw new CacheServerException(key.getDatum() + " load faild!", e);
			}
		}
		Value<K,V>  value =  redisCacheHandler.get(key);
		//miss hit
		if (value == null || !value.isValid()) {
			long current = System.currentTimeMillis();
			try {
				value =  new BeehiveCacheValue<K,V>(key.getDatum(),cacheLoader.reload(key.getDatum()));
				cacheStats.loadSuccessCountIncr();
				
				writeRedisPool.submit(new WriteCacheThread(redisCacheHandler,key,value));//submit sync redis task
				return value;
			} catch (Exception e) {
				cacheStats.loadExceptionCountIncr();
				throw new CacheServerException(key.getDatum() + " load faild!", e);
			}finally {
				cacheStats.totalLoadTimeIncr(System.currentTimeMillis() - current);
				cacheStats.missCountIncr();
			}
		}else {
			cacheStats.hitCountIncr();
			redisCacheHandler.expire(key,expireAfterAccessMs);
			return value;
		}
	}

	@Override
	public void refresh(Key key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invalid(Key key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CacheStats stats() {
		// TODO Auto-generated method stub
		return null;
	}
	
	class WriteCacheThread implements Runnable{
		private RedisCacheHandler<K, V> redisCacheHandler;
		private Key<K> key;
		private Value<K, V> value;
		
		public WriteCacheThread(RedisCacheHandler<K, V> redisCacheHandler,Key<K> key,Value<K, V> value) {
			this.redisCacheHandler = redisCacheHandler;
			this.key = key;
			this.value =value;
		}

		@Override
		public void run() {
			//set cache data
			redisCacheHandler.set(key, value, expireAfterAccessMs);
			
			//add listener key
			String listenerKey = String.format(LISTENER, business,String.valueOf(System.currentTimeMillis()/refreshIntervalMs) );
			redisCacheHandler.sadd(listenerKey, key.getRedisKey());
		}
		
	}


}
