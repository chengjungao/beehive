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
	public String equals();

	/**
	 * 
	 * @return
	 */
	public T getDatum();
}
