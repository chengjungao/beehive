package com.chengjungao.beehive.cache.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisFactory {
	private static final Map<String, RedissonClient> REDISCLUSTERS = new ConcurrentHashMap<>();
	
	public static void registe(RedisConfig redisConfig) {
		Config config = new Config();
		config.useClusterServers()
	    .addNodeAddress(redisConfig.getNodeAddress());
		RedissonClient redisson =  Redisson.create(config);
		REDISCLUSTERS.put(redisConfig.getName(), redisson);
	}
}
