package com.chengjungao.beehive.cache.impl;


import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chengjungao.beehive.cache.Cache;
import com.chengjungao.beehive.cache.CacheListener;
import com.chengjungao.beehive.cache.CacheLoader;
import com.chengjungao.beehive.cache.CacheStats;
import com.chengjungao.beehive.cache.Key;
import com.chengjungao.beehive.cache.Value;
import com.chengjungao.beehive.cache.config.CacheConfig;
import com.chengjungao.beehive.cache.exception.CacheServerException;
import com.chengjungao.beehive.cache.redis.RedisFactory;

public class BeehiveCache<K,V> implements Cache<K,V> {
	private static final Logger LOG = LoggerFactory.getLogger(BeehiveCache.class);
	
	private static final String LISTENER = "%s-Listener:%s";
	private static final String LOCK = "%s-Lock:%s";
	private static final String KEY = "%s:%s";
	private CacheConfig cacheConfig;
	private CacheLoader<K, V> cacheLoader;
	private RedissonClient redissonClient;
	
	
	private int expireAfterAccessMs;
	private int refreshAfterWriteMs;
	private CacheStats cacheStats;
	private CacheListener listener;
	
	public BeehiveCache(CacheConfig config,CacheLoader<K, V> cacheLoader) {
		this.cacheConfig = config;
		this.refreshAfterWriteMs = config.getRefreshAfterWriteMs();
		this.expireAfterAccessMs = config.getExpireAfterAccessMs();
		this.cacheLoader = cacheLoader;
		this.cacheStats = new CacheStats();
		
		try {
			this.redissonClient = RedisFactory.getClient(config.getRedisConfig());
		} catch (Exception e) {
			LOG.error("init cache redis error!", e);
		}
		listener = new BeeHiveCacheListener();
		listener.startListen(this);
	}

	@Override
	public Value get(Key<K> key) {
		if (redissonClient == null) {
			try {
				return new BeehiveCacheValue<V>(cacheLoader.reload(key.getDatum()));
			} catch (Exception e) {
				throw new CacheServerException(key.getDatum() + " load faild!", e);
			}
		}
		String redisKey = String.format(KEY, cacheConfig.getBusiness(),key.hash());
		byte[] redisValue = (byte[]) redissonClient.getBucket(redisKey).get();
		//miss hit
		if (redisValue == null || redisValue.length == 0) {
			
		}
		return null;
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

	@Override
	public CacheConfig getCacheConfig() {
		// TODO Auto-generated method stub
		return null;
	}

}
