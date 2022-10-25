package com.chengjungao.beehive.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.redisson.RedissonLock;

/**
 * 定义BeehiveCache的redis操作接口
 * 
 * @author wolf
 *
 * @param <K> 用户缓存键类型 K
 * @param <V> 用户缓存值类型 V
 */
public interface RedisCacheHandler<K,V> {
	
	/**
	 * 通过Beehive键获取Redis里缓存的值
	 * @param business
	 * @param key
	 * @return
	 */
	public Value<K,V> get(String business,Key<K> key);
	
	/**
	 * 通过Beehive键获取Redis里缓存的值批量方法
	 * @param keys
	 * @return
	 */
	public Map<String, Value<K,V>> mget(Collection<String> keys);
	
	/**
	 * 将Beehive的键值对写入Redis
	 * @param business
	 * @param key
	 * @param value
	 * @param expireMs
	 */
	public void set(String business,Key<K> key,Value<K,V> value,int expireMs);
	
	/**
	 * 通过Beehive键设置键的过期时间
	 * @param business
	 * @param key
	 * @param expireMs
	 */
	public void expire(String business,Key<K> key,int expireMs);
	
	/**
	 * 获取key剩余存活时间
	 * @param key
	 * @return
	 */
	public long ttl(String key);
	
	/**
	 * 刷新Redis缓存值
	 * @param business
	 * @param key
	 * @param loader
	 */
	public void refresh(String business,Key<K> key,CacheLoader<K, V> loader);
	
	/**
	 * Redis smembers
	 * @param key
	 * @return
	 */
	public Set<String> smembers(String key);
	
	/**
	 * Redis sadd
	 * @param key
	 * @param values
	 */
	public void sadd(String key,Set<String> values);
	
	/**
	 * Redis sadd
	 * @param key
	 * @param value
	 */
	public void sadd(String key,String value);
	
	/**
	 * Redis exists
	 * @param key
	 * @return
	 */
	public boolean exists(String key);
	
	/**
	 * 获取Redis 分布式锁
	 * @param key
	 * @return
	 */
	public RedissonLock getLock(String key);
	
	/**
	 * Redis delete
	 * @param key
	 */
	public void delete(String key);
	
}
