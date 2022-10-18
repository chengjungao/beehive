package com.chengjungao.beehive.cache.impl;


import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.redisson.api.RBucket;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
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
	private static final String KEY = "%s:%s";
	private CacheConfig cacheConfig;
	private CacheLoader<K, V> cacheLoader;
	private RedissonClient redissonClient;
	private ExecutorService writeRedisPool;
	
	private String business;
	private int expireAfterAccessMs;
	private int refreshAfterWriteMs;
	private int refreshIntervalMs;
	private CacheStats cacheStats;
	private CacheListener listener;
	
	public BeehiveCache(CacheConfig config,CacheLoader<K, V> cacheLoader) {
		this.cacheConfig = config;
		this.business = config.getBusiness();
		this.refreshAfterWriteMs = config.getRefreshAfterWriteMs();
		this.expireAfterAccessMs = config.getExpireAfterAccessMs();
		this.refreshIntervalMs = config.getRefreshIntervalMs();
		
		this.cacheLoader = cacheLoader;
		this.writeRedisPool = Executors.newFixedThreadPool(config.getSyncRedisThread());
		this.cacheStats = new CacheStats();
		
		try {
			this.redissonClient = RedisFactory.getClient(config.getRedisConfig());
		} catch (Exception e) {
			LOG.error("init cache redis error!", e);
		}
		listener = new BeeHiveCacheListener(cacheConfig);
		listener.startListen();
	}

	@Override
	public Value<V> get(Key<K> key) throws Exception {
		if (redissonClient == null) {
			try {
				return new BeehiveCacheValue<K,V>(key.getDatum(),cacheLoader.reload(key.getDatum()));
			} catch (Exception e) {
				throw new CacheServerException(key.getDatum() + " load faild!", e);
			}
		}
		String redisKey = String.format(KEY, cacheConfig.getBusiness(),key.hash());
		RBucket<byte[]> cacheBucket = redissonClient.getBucket(redisKey);
		byte[] redisValue = (byte[]) cacheBucket.get();
		//miss hit
		if (redisValue == null || redisValue.length == 0) {
			long current = System.currentTimeMillis();
			try {
				BeehiveCacheValue<K,V>  value =  new BeehiveCacheValue<K,V>(key.getDatum(),cacheLoader.reload(key.getDatum()));
				cacheStats.loadSuccessCountIncr();
				
				writeRedisPool.submit(new Runnable() { //submit sync redis task
					
					@Override
					public void run() {
						//set cache data
						RBucket<byte[]> object = redissonClient.getBucket(redisKey);
						object.set(value.writeTo());
						object.expire(Duration.ofMillis(expireAfterAccessMs));
						
						//add listener key
						String listenerKey = String.format(LISTENER, business,String.valueOf(System.currentTimeMillis()/refreshIntervalMs) );
						RSet<String> listenerSet = redissonClient.getSet(listenerKey, StringCodec.INSTANCE);
						listenerSet.add(redisKey);
						
					}
				});
				return value;
			} catch (Exception e) {
				cacheStats.loadExceptionCountIncr();
				throw new CacheServerException(key.getDatum() + " load faild!", e);
			}finally {
				cacheStats.totalLoadTimeIncr(System.currentTimeMillis() - current);
				cacheStats.missCountIncr();
			}
		}else {
			BeehiveCacheValue<K,V>  value =  new BeehiveCacheValue<K,V>();
			value.readFrom(redisValue);
			cacheStats.hitCountIncr();
			
			cacheBucket.expire(Duration.ofMillis(expireAfterAccessMs));
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


}
