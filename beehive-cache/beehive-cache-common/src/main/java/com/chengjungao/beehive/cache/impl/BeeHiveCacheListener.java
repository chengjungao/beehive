package com.chengjungao.beehive.cache.impl;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chengjungao.beehive.cache.Cache;
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

	public BeeHiveCacheListener(CacheConfig config) {
		this.config = config;
		try {
			this.redissonClient = RedisFactory.getClient(config.getRedisConfig());
		} catch (Exception e) {
			LOG.error("init cache redis error!", e);
		}
	}

	private void refreshListenedKeys(String listenerKey) {
	
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
					refreshListenedKeys(String.format(LISTENER, config.getBusiness(),time));
				} catch (InterruptedException e) {}
				
			}
		},"Refresh-Cache").start();
		
	}

}
