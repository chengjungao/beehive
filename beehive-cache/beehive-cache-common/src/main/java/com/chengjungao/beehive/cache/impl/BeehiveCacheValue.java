package com.chengjungao.beehive.cache.impl;

import com.chengjungao.beehive.cache.Value;

public class BeehiveCacheValue<V> implements Value<V> {
	private V datum;
	
	public BeehiveCacheValue(V datum) {
		this.datum = datum;
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
