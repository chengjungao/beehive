package com.chengjungao.beehive.cache;


/**
 * 缓存监听器，负责监听和自动后台刷新缓存
 * @author wolf
 *
 * @param <K> 用户缓存键类型 K
 * @param <V> 用户缓存值类型 V
 */
public interface CacheListener<K,V> {

	/**
	 * 启动监听器
	 */
	public void startListen();
}
