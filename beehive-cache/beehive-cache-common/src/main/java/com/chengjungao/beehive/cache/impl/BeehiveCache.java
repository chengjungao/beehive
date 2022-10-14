package com.chengjungao.beehive.cache.impl;

import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chengjungao.beehive.cache.Cache;
import com.chengjungao.beehive.cache.CacheLoader;
import com.chengjungao.beehive.cache.CacheStats;
import com.chengjungao.beehive.cache.Key;
import com.chengjungao.beehive.cache.Value;
import com.chengjungao.beehive.cache.config.CacheConfig;
import com.chengjungao.beehive.cache.redis.RedisFactory;

public class BeehiveCache implements Cache {
	private static final Logger LOG = LoggerFactory.getLogger(BeehiveCache.class);
	
	private static final String LISTENER = "%s-Listener:%s";
	private static final String LOCK = "%s-Lock:%s";
	private static final String KEY = "%s:%s";
	private CacheConfig cacheConfig;
	private CacheLoader<Key, Value> cacheLoader;
	private RedissonClient redissonClient;
	
	public BeehiveCache(CacheConfig config,CacheLoader<Key, Value> cacheLoader) {
		this.cacheConfig = config;
		this.cacheLoader = cacheLoader;
		try {
			this.redissonClient = RedisFactory.getClient(config.getRedisConfig());
		} catch (Exception e) {
			LOG.error("init cache redis error!", e);
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		},"Produce-Listener-Key").start();
	}

	@Override
	public Value get(Key key) {
		// TODO Auto-generated method stub
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

}
