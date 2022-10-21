package com.chengjungao.beehive.cache;

import java.io.Serializable;

/**
 * 
 * @author wolf
 *
 */
public interface Value<K,T> extends Serializable {
	
	public void readFrom(byte[] values); 
	
	public byte[] writeTo();
	
	public boolean isValid();
	
	public K getDataKey();
	
	public T getDataValue();
}
