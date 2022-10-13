package com.chengjungao.beehive.cache;

/**
 * 
 * @author wolf
 *
 * @param <K>
 * @param <V>
 */
public interface Cache {
	
	/**
	 * 
	 * @param k
	 * @return
	 */
	public Value get(Key key);
	
	/**
	 * 
	 * @param k
	 */
	public void refresh(Key key);
	
	
	/**
	 * 
	 * @param k
	 */
	public void invalid(Key key);
	
	/**
	 * 
	 */
	public CacheStats stats() ;

}
