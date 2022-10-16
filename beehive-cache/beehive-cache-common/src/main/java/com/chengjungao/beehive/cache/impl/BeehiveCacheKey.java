package com.chengjungao.beehive.cache.impl;

import com.chengjungao.beehive.cache.Key;

public class BeehiveCacheKey<K> implements Key<K> {
	private K datum;
	
	public BeehiveCacheKey(K datum) {
		this.datum = datum;
	}
	
	public K getDatum() {
		return datum;
	}

	public void setDatum(K datum) {
		this.datum = datum;
	}

	@Override
	public String hash() {
		return String.valueOf(datum.hashCode());
	}

	@Override
	public String equals() {
		return null;
	}

}
