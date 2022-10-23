package com.chengjungao.beehive.cache.impl;

import java.util.Objects;

import com.chengjungao.beehive.cache.Key;

public class BeehiveCacheKey<K> implements Key<K> {
	private static final String KEY = "%s:%s";
	
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
		return String.valueOf(hashCode());
	}

	@Override
	public String getRedisKey(String business) {
		return String.format(KEY, business, hash());
	}

	@Override
	public int hashCode() {
		return Objects.hash(datum);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		BeehiveCacheKey<K> other = (BeehiveCacheKey<K>) obj;
		return Objects.equals(datum, other.datum);
	}
}
