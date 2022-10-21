package com.chengjungao.beehive.cache.impl;

import java.util.Objects;

import com.chengjungao.beehive.cache.Key;

public class BeehiveCacheKey<K> implements Key<K> {
	private static final String KEY = "%s:%s";
	
	private String business;
	private K datum;
	
	public BeehiveCacheKey(String business, K datum) {
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
	public String getBusiness() {
		return this.business;
	}
	
	@Override
	public String getRedisKey() {
		return String.format(KEY, getBusiness(), hash());
	}

	@Override
	public int hashCode() {
		return Objects.hash(business, datum);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		BeehiveCacheKey other = (BeehiveCacheKey) obj;
		return Objects.equals(business, other.business) && Objects.equals(datum, other.datum);
	}

}
