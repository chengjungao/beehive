package com.chengjungao.beehive.cache;

/**
 * 
 * @author wolf
 *
 */
public interface Key {

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
}
