package com.chengjungao.beehive.cache;

import java.util.Map;

/**
 * 
 * @author wolf
 * @param <K>
 * @param <V>
 */
public interface CacheLoader<K, V>  {
	  /**
	   * Computes or retrieves the value corresponding to {@code key}.
	   *
	   * @param key the non-null key whose value should be loaded
	   * @return the value associated with {@code key}; <b>must not be null</b>
	   * @throws Exception if unable to load the result
	   * @throws InterruptedException if this method is interrupted. {@code InterruptedException} is
	   *     treated like any other {@code Exception} in all respects except that, when it is caught,
	   *     the thread's interrupt status is set
	   */
	  public V load(K key) throws Exception;
	  
	  
	  /**
	   * Computes or retrieves the values corresponding to {@code keys}. This method is called by
	   * {@link LoadingCache#getAll}.
	   *
	   * <p>If the returned map doesn't contain all requested {@code keys} then the entries it does
	   * contain will be cached, but {@code getAll} will throw an exception. If the returned map
	   * contains extra keys not present in {@code keys} then all returned entries will be cached,
	   * but only the entries for {@code keys} will be returned from {@code getAll}.
	   *
	   * <p>This method should be overriden when bulk retrieval is significantly more efficient than
	   * many individual lookups. Note that {@link LoadingCache#getAll} will defer to individual calls
	   * to {@link LoadingCache#get} if this method is not overriden.
	   *
	   * @param keys the unique, non-null keys whose values should be loaded
	   * @return a map from each key in {@code keys} to the value associated with that key;
	   *     <b>may not contain null values</b>
	   * @throws Exception if unable to load the result
	   * @throws InterruptedException if this method is interrupted. {@code InterruptedException} is
	   *     treated like any other {@code Exception} in all respects except that, when it is caught,
	   *     the thread's interrupt status is set
	   * @since 11.0
	   */
	  public Map<K, V> loadAll(Iterable<? extends K> keys) ;
	  
	  
}
