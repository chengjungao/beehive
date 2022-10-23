package com.chengjungao.beehive.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.redisson.RedissonLock;

public interface RedisCacheHandler<K,V> {
	
	public Value<K,V> get(String business,Key<K> key);
	
	public Map<String, Value<K,V>> mget(Collection<String> keys);
	
	public void set(String business,Key<K> key,Value<K,V> value,int expireMs);
	
	public void expire(String business,Key<K> key,int expireMs);
	
	public long ttl(String key);
	
	public void refresh(String business,Key<K> key,CacheLoader<K, V> loader);
	
	public Set<String> smembers(String key);
	
	public void sadd(String key,Set<String> values);
	
	public void sadd(String key,String value);
	
	public boolean exists(String key);
	
	public RedissonLock getLock(String key);
	
	public void delete(String key);
	
}
