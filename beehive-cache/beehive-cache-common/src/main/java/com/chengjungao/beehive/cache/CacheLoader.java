package com.chengjungao.beehive.cache;

import java.util.Map;

/**
 * 定义缓存获取的接口，用户需要实现此接口
 * @author wolf
 * @param <K> 用户缓存键类型 K
 * @param <V> 用户缓存值类型 V
 */
public interface CacheLoader<K, V>  {
	 
	/**
	 * 单个键Load
	 * @param key
	 * @return V value
	 * @throws Exception
	 */
	  public V load(K key) throws Exception;
	  
	  
	  /**
	   * 多个键Load
	   * @param keys
	   * @return
	   */
	  public Map<K, V> loadAll(Iterable<? extends K> keys) ;
	  
	  
}
