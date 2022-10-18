package com.chengjungao.beehive.cache;

import org.redisson.api.RedissonClient;

import com.chengjungao.beehive.cache.config.CacheConfig;

/**
 * 
 * @author wolf
 *
 * @param <K>
 * @param <V>
 */
public interface Cache<K,V> {
	
	/**
	 * 
	 * @param k
	 * @return
	 */
	public Value<V> get(Key<K> key) throws Exception ;
	
	/**
	 * 
	 * @param k
	 */
	public void refresh(Key<K> key);
	
	
	/**
	 * 
	 * @param k
	 */
	public void invalid(Key<K> key);
	
	/**
	 * 
	 */
	public CacheStats stats() ;

}
