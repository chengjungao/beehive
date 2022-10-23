package com.chengjungao.beehive.cache;

/**
 * 
 * @author wolf
 *
 */
public interface Key<T> {
	
	

	/**
	 * 
	 * @return
	 */
	public String hash();
	

	/**
	 * 
	 * @return
	 */
	public T getDatum();
	
	
	/**
	 * 
	 * @return
	 */
	public String getRedisKey(String business);
}
