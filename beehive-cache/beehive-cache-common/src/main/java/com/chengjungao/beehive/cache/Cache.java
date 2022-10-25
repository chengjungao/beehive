package com.chengjungao.beehive.cache;

/**
 * 缓存对象接口
 * @author wolf
 * @param <K> 用户缓存键类型 K
 * @param <V> 用户缓存值类型 V
 */
public interface Cache<K,V> {
	
	/**
	 * 通过key获取缓存Value
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Value<K,V> get(Key<K> key) throws Exception ;
	
	/**
	 * 通过Key刷新缓存
	 * @param key
	 */
	public void refresh(Key<K> key);
	
	
	/**
	 * 通过Key让缓存失效
	 * @param key
	 */
	public void invalid(Key<K> key);
	
	/**
	 * 获取缓存状态
	 * @return CacheStats 缓存状态
	 */
	public CacheStats stats() ;

}
