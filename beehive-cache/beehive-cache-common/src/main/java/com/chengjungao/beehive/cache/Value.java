package com.chengjungao.beehive.cache;

/**
 * 
 * @author wolf
 *
 */
public interface Value {
	
	public void readFrom(byte[] values); 
	
	public byte[] writeTo();
	
}
