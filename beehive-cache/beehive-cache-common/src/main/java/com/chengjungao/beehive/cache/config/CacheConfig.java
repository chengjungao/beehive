package com.chengjungao.beehive.cache.config;

public class CacheConfig {
	
	private String business;
	
	private RedisConfig redisConfig;
	
	private int maxWaitedRefreshSize = 10000;
	
	private int refreshAfterWriteMs = 1000 * 300;
	
	private int expireAfterAccessMs = 1000 * 900;
	
	private int rollbackRefreshMs = 1000 * 60;
	
	private int refreshIntervalMs = 1000;
	
	private int syncRedisThread = 20;
	
	private int reloadThread = 20;

	public CacheConfig(String business, RedisConfig redisConfig) {
		this.business = business;
		this.redisConfig = redisConfig;
	}

	public CacheConfig(String business, RedisConfig redisConfig, int maxWaitedRefreshSize, int refreshAfterWriteMs,
			int expireAfterAccessMs, int rollbackRefreshMs, int refreshIntervalMs, int syncRedisThread,
			int reloadThread) {
		super();
		this.business = business;
		this.redisConfig = redisConfig;
		this.maxWaitedRefreshSize = maxWaitedRefreshSize;
		this.refreshAfterWriteMs = refreshAfterWriteMs;
		this.expireAfterAccessMs = expireAfterAccessMs;
		this.rollbackRefreshMs = rollbackRefreshMs;
		this.refreshIntervalMs = refreshIntervalMs;
		this.syncRedisThread = syncRedisThread;
		this.reloadThread = reloadThread;
	}

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
