package com.chengjungao.beehive.cache;

import java.io.Serializable;

/**
 * 
 * @author wolf
 *
 * @param <K> 用户缓存键类型 K
 * @param <T> 用户缓存值类型 T
 */
public interface Value<K,T> extends Serializable {
	
	/**
	 * 反序列化方法
	 * @param values
	 */
	public void readFrom(byte[] values); 
	
	/**
	 * 序列化方法
	 * @return
	 */
	public byte[] writeTo();
	
	/**
	 * 值对象是否有效
	 * @return
	 */
	public boolean isValid();
	
	/**
	 * 获取用户缓存键对象
	 * @return
	 */
	public K getDataKey();
	
	/**
	 * 获取用户值对象
	 * @return
	 */
	public T getDataValue();
}
