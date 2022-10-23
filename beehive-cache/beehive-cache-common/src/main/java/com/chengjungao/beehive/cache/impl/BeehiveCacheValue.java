package com.chengjungao.beehive.cache.impl;

import com.chengjungao.beehive.cache.Value;
import com.chengjungao.beehive.cache.util.ProtostuffUtil;

public class BeehiveCacheValue<K,V> implements Value<K,V> {
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
		if (values != null && values.length != 0) {
			@SuppressWarnings("unchecked")
			BeehiveCacheValue<K,V> cacheValue = ProtostuffUtil.deserialize(values, this.getClass());
			this.key = cacheValue.getDataKey();
			this.value = cacheValue.getDataValue();
		}
		
	}

	@Override
	public byte[] writeTo() {
		return ProtostuffUtil.serialize(this);
	}

	@Override
	public boolean isValid() {
		return key != null && value != null;
	}

	@Override
	public K getDataKey() {
		return key;
	}

	@Override
	public V getDataValue() {
		return value;
	}
}