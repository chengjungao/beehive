package com.chengjungao.beehive.cache.config;

/**
 * 缓存配置
 * @author wolf
 */
public class CacheConfig {
	
	/**
	 * 缓存业务域
	 */
	private String business;
	
	/**
	 * RedisCluster配置
	 */
	private RedisConfig redisConfig;
	
	/**
	 * 最大等待阻塞键数量
	 */
	private int maxWaitedRefreshSize = 10000;
	
	/**
	 * 写入{@value refreshAfterWriteMs}毫秒后，刷新键值对
	 */
	private int refreshAfterWriteMs = 1000 * 300;
	
	/**
	 * 在最后访问{@value expireAfterAccessMs}毫秒后键值对失效
	 */
	private int expireAfterAccessMs = 1000 * 900;
	
	/**
	 * 程序启动时，将监听器刷新往回追溯{@value rollbackRefreshMs}毫秒，防止漏掉刷新
	 */
	private int rollbackRefreshMs = 1000 * 60;
	
	/**
	 * 自动刷新键值对的时间间隔，默认1s
	 */
	private int refreshIntervalMs = 1000;
	
	/**
	 * 将没有命中的键值对，通过异步线程刷入Redis的线程池大小
	 */
	private int syncRedisThread = 20;
	
	/**
	 * 自动刷新键值对的线程池大小
	 */
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
