package com.chengjungao.beehive.cache.impl;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.cache.CacheException;

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

/**
 * Beehive Cache Redis缓存操作实现类
 * @author wolf
 *
 * @param <K>
 * @param <V>
 */
public class BeehiveRedisHandler<K,V> implements RedisCacheHandler<K, V> {
	private RedissonClient redissonClient;
	
	public  BeehiveRedisHandler(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}
	
	@Override
	public Value<K, V> get(String business,Key<K> key) {
		//query redis
		RBucket<byte[]> cacheBucket = redissonClient.getBucket(key.getRedisKey(business));
		byte[] redisValue = cacheBucket.get();
		
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
	public void set(String business,Key<K> key, Value<K, V> value, int expireMs) {
		RBucket<byte[]> object = redissonClient.getBucket(key.getRedisKey(business));
		if (expireMs > 0) {
			object.set(value.writeTo(),expireMs,TimeUnit.MILLISECONDS);
		}else {
			throw new CacheException("expireMs must be great than 0!");
		}
	}

	@Override
	public void expire(String business,Key<K> key, int expireMs) {
		if (expireMs > 0) {
			RBucket<byte[]> object = redissonClient.getBucket(key.getRedisKey(business));
			object.expire(Duration.ofMillis(expireMs));
		}
	}

	@Override
	public long ttl(String key) {
		RBucket<byte[]> object = redissonClient.getBucket(key);
		return object.remainTimeToLive();
	}

	@Override
	public void refresh(String business,Key<K> key,CacheLoader<K, V> loader) {
		try {
			V value = loader.load(key.getDatum());
			RBucket<byte[]> object = redissonClient.getBucket(key.getRedisKey(business));
			long ttl = object.remainTimeToLive();
			if (object.remainTimeToLive() > 0) {
				object.set(new BeehiveCacheValue<K, V>(key.getDatum(), value).writeTo(),ttl,TimeUnit.MILLISECONDS);
			}
			/**
			 * require redis 6.0 or higher
			 */
			//object.setAndKeepTTL(new BeehiveCacheValue<K, V>(key.getDatum(), value).writeTo());
		} catch (Exception e) {
			throw new CacheException("Refresh cache error!", e);
		}
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
