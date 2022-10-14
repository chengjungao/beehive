package com.chengjungao.beehive.cache.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;

public class RedisFactory {
	private static volatile Map<String, RedissonClient> REDISCLUSTERS = new ConcurrentHashMap<>();
	
	private static void registe(RedisConfig redisConfig) {
		Config config = new Config();
		config.setCodec(StringCodec.INSTANCE);
		config.useClusterServers()
	    .addNodeAddress(redisConfig.getNodeAddress())
	    .setReadMode(ReadMode.SLAVE)
	    .setSlaveConnectionMinimumIdleSize(redisConfig.getMaxConnection() / 3)
	    .setSlaveConnectionPoolSize(redisConfig.getMaxConnection())
	    .setMasterConnectionMinimumIdleSize(redisConfig.getMaxConnection() / 3)
	    .setMasterConnectionPoolSize(redisConfig.getMaxConnection())
	    .setClientName(redisConfig.getName())
	    .setTimeout(redisConfig.getTimeout())
		.setPassword(redisConfig.getPassword());
	    
		RedissonClient redisson =  Redisson.create(config);
		REDISCLUSTERS.put(redisConfig.getName(), redisson);
	}
	
	public static RedissonClient getClient(RedisConfig redisConfig) {
		RedissonClient redissonClient = REDISCLUSTERS.get(redisConfig.getName());
		if (redissonClient == null) {
			synchronized (REDISCLUSTERS) {
				if (REDISCLUSTERS.get(redisConfig.getName()) == null) {
					registe(redisConfig);
					redissonClient = REDISCLUSTERS.get(redisConfig.getName());
				}
			}
		}
		return redissonClient;
	}
	
}
