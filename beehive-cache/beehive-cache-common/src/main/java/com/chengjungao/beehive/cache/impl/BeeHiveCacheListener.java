package com.chengjungao.beehive.cache.impl;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.chengjungao.beehive.cache.Cache;
import com.chengjungao.beehive.cache.CacheListener;
import com.chengjungao.beehive.cache.config.CacheConfig;

public class BeeHiveCacheListener implements CacheListener {
	private BlockingQueue<String> waitedRefresh;
	private int rollbackRefreshMs;
	private int refreshIntervalMs;

	public BeeHiveCacheListener() {
		super();
	}

	private List<String> listenedKeys(String listenerKey) {
		return null;
	}

	@Override
	public void startListen(Cache cache) {
		CacheConfig config = cache.getCacheConfig();
		this.waitedRefresh = new ArrayBlockingQueue<>(config.getMaxWaitedRefreshSize());
		
		//produce listener key
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				long refreshTime = (System.currentTimeMillis() - rollbackRefreshMs) / refreshIntervalMs * refreshIntervalMs;
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
					
				} catch (InterruptedException e) {}
				
			}
		},"Refresh-Cache").start();
		
	}

}
