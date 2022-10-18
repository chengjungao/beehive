package com.chengjungao.beehive.cache;

import java.io.Serializable;

/**
 * 
 * @author wolf
 *
 */
public interface Value<T> extends Serializable {
	
	public void readFrom(byte[] values); 
	
	public byte[] writeTo();
	
}
