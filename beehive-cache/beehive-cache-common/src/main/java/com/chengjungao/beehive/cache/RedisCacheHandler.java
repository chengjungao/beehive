package com.chengjungao.beehive.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.redisson.RedissonLock;

public interface RedisCacheHandler<K,V> {
	
	public Value<K,V> get(Key<K> key);
	
	public Map<Key<K>, Value<K,V>> mget(Collection<Key<K>> keys);
	
	public void set(Key<K> key,Value<K,V> value,int expireMs);
	
	public void expire(String key,int expireMs);
	
	public long ttl(String key);
	
	public void refresh(Key<K> key);
	
	public Set<String> smembers(String key);
	
	public void sadd(String key,Set<String> values);
	
	public void sadd(String key,String value);
	
	public void exists(String key);
	
	public RedissonLock getLock(String key);
	
	public void delete(String key);
	
}
