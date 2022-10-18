package com.chengjungao.beehive.cache.config;

import com.chengjungao.beehive.cache.redis.RedisConfig;

public class CacheConfig {
	
	private String business;
	
	private RedisConfig redisConfig;
	
	private int maxWaitedRefreshSize;
	
	private int refreshAfterWriteMs;
	
	private int expireAfterAccessMs;
	
	private int rollbackRefreshMs;
	
	private int refreshIntervalMs;
	
	private int syncRedisThread;
	
	private int reloadThread;

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public RedisConfig getRedisConfig() {
		return redisConfig;
	}

	public void setRedisConfig(RedisConfig redisConfig) {
		this.redisConfig = redisConfig;
	}

	public int getMaxWaitedRefreshSize() {
		return maxWaitedRefreshSize;
	}

	public void setMaxWaitedRefreshSize(int maxWaitedRefreshSize) {
		this.maxWaitedRefreshSize = maxWaitedRefreshSize;
	}

	public int getRefreshAfterWriteMs() {
		return refreshAfterWriteMs;
	}

	public void setRefreshAfterWriteMs(int refreshAfterWriteMs) {
		this.refreshAfterWriteMs = refreshAfterWriteMs;
	}

	public int getExpireAfterAccessMs() {
		return expireAfterAccessMs;
	}

	public void setExpireAfterAccessMs(int expireAfterAccessMs) {
		this.expireAfterAccessMs = expireAfterAccessMs;
	}

	public int getRollbackRefreshMs() {
		return rollbackRefreshMs;
	}

	public void setRollbackRefreshMs(int rollbackRefreshMs) {
		this.rollbackRefreshMs = rollbackRefreshMs;
	}

	public int getRefreshIntervalMs() {
		return refreshIntervalMs;
	}

	public void setRefreshIntervalMs(int refreshIntervalMs) {
		this.refreshIntervalMs = refreshIntervalMs;
	}

	public int getSyncRedisThread() {
		return syncRedisThread;
	}

	public void setSyncRedisThread(int syncRedisThread) {
		this.syncRedisThread = syncRedisThread;
	}

	public int getReloadThread() {
		return reloadThread;
	}

	public void setReloadThread(int reloadThread) {
		this.reloadThread = reloadThread;
	}

	
		
}
