package com.chengjungao.beehive.cache.impl;

import com.chengjungao.beehive.cache.Value;

public class BeehiveCacheValue<K,V> implements Value<V> {
	private static final long serialVersionUID = 1L;
	private K key;
	private V value;
	
	public BeehiveCacheValue() {
		super();
	}

	public BeehiveCacheValue(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public void readFrom(byte[] values) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] writeTo() {
		// TODO Auto-generated method stub
		return null;
	}

}
