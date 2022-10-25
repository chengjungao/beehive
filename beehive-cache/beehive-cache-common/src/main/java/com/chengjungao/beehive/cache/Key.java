package com.chengjungao.beehive.cache;

/**
 * 定义BeehiveCache使用的键接口规范
 * 
 * @author wolf
 *
 * @param <T> 范型T为用户缓存键类型
 */
public interface Key<T> {
	
	

	/**
	 * BeehiveCache的键必须实现hash方法
	 * @return
	 */
	public String hash();
	

	/**
	 * BeehiveCache键获取用户键对象
	 * @return
	 */
	public T getDatum();
	
	
	/**
	 * BeehiveCahce键生成Redis key
	 * @return
	 */
	public String getRedisKey(String business);
}
