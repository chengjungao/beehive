package com.chengjungao.beehive.cache.impl;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.redisson.RedissonLock;
import org.redisson.api.RBucket;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.redisson.client.codec.StringCodec;

import com.chengjungao.beehive.cache.CacheLoader;
import com.chengjungao.beehive.cache.Key;
import com.chengjungao.beehive.cache.RedisCacheHandler;
import com.chengjungao.beehive.cache.Value;

public class BeehiveRedisHandler<K,V> implements RedisCacheHandler<K, V> {
	private RedissonClient redissonClient;
	
	public  BeehiveRedisHandler(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}
	
	@Override
	public Value<K, V> get(Key<K> key) {
		//query redis
		RBucket<byte[]> cacheBucket = redissonClient.getBucket(key.getRedisKey());
		byte[] redisValue = (byte[]) cacheBucket.get();
		
		BeehiveCacheValue<K,V>  value =  new BeehiveCacheValue<K,V>();
		value.readFrom(redisValue);
		return value;
	}

	@Override
	public Map<String, Value<K, V>> mget(Collection<String> keys) {
		Map<String, Value<K, V>> result = new HashMap<>();
		Map<String, byte[]> values = redissonClient.getBuckets(ByteArrayCodec.INSTANCE).get(keys.toArray(new String[keys.size()]));
		
		for (Entry<String, byte[]> entry : values.entrySet()) {
			Value<K,V>  value =  new BeehiveCacheValue<K,V>();
			value.readFrom(entry.getValue());
			
			result.put(entry.getKey(), value);
		}
		return result;
	}

	@Override
	public void set(Key<K> key, Value<K, V> value, int expireMs) {
		RBucket<byte[]> object = redissonClient.getBucket(key.getRedisKey());
		object.set(value.writeTo());
		object.expire(Duration.ofMillis(expireMs));
		
	}

	@Override
	public void expire(Key<K> key, int expireMs) {
		RBucket<byte[]> object = redissonClient.getBucket(key.getRedisKey());
		object.expire(Duration.ofMillis(expireMs));
	}

	@Override
	public long ttl(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void refresh(Key<K> key,CacheLoader<K, V> loader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> smembers(String key) {
		RSet<String> rset = redissonClient.getSet(key,StringCodec.INSTANCE);
		return rset.readAll();
	}

	@Override
	public void sadd(String key, Set<String> values) {
		RSet<String> rSet = redissonClient.getSet(key, StringCodec.INSTANCE);
		rSet.addAll(values);
	}

	@Override
	public void sadd(String key, String value) {
		RSet<String> listenerSet = redissonClient.getSet(key, StringCodec.INSTANCE);
		listenerSet.add(value);
		
	}

	@Override
	public boolean exists(String key) {
		RBucket<String> object = redissonClient.getBucket(key);
		return object.isExists();
	}

	@Override
	public RedissonLock getLock(String key) {
		return (RedissonLock) redissonClient.getLock(key);
	}

	@Override
	public void delete(String key) {
		redissonClient.getBucket(key).delete();
	}

}
