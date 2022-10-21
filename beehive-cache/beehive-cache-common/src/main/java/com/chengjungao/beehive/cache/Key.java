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
	public String getBusiness();
	
	/**
	 * 
	 * @return
	 */
	public String getRedisKey();
}
