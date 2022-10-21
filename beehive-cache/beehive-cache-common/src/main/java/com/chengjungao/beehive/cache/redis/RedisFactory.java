package com.chengjungao.beehive.cache.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;

import com.chengjungao.beehive.cache.RedisCacheHandler;
import com.chengjungao.beehive.cache.config.RedisConfig;
import com.chengjungao.beehive.cache.impl.BeehiveRedisHandler;

public class RedisFactory {
	private static volatile Map<String, RedisCacheHandler<?, ?>> REDISCLUSTERS = new ConcurrentHashMap<>();
	
	private static void registe(RedisConfig redisConfig) {
		Config config = new Config();
		config.setCodec(ByteArrayCodec.INSTANCE);
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
		REDISCLUSTERS.put( redisConfig.getName(), new BeehiveRedisHandler<>(redisson));
	}
	
	@SuppressWarnings("unchecked")
	public static <K,V> RedisCacheHandler<K, V> getClient(RedisConfig redisConfig) {
		RedisCacheHandler<?, ?> redissonClient = REDISCLUSTERS.get(redisConfig.getName());
		if (redissonClient == null) {
			synchronized (REDISCLUSTERS) {
				if (REDISCLUSTERS.get(redisConfig.getName()) == null) {
					registe(redisConfig);
					redissonClient = REDISCLUSTERS.get(redisConfig.getName());
				}
			}
		}
		return (RedisCacheHandler<K, V>) redissonClient;
	}
	
}
