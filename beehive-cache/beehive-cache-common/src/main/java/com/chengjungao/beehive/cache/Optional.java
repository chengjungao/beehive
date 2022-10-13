package com.chengjungao.beehive.cache;

import java.io.Serializable;

public class Optional<V> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public Optional(V value) {
		this.value = value;
	}

	private V value;
	
	public static <V> Optional<V> from(V value){
		return new Optional<V>(value);
	}

	public V getValue() {
		return value;
	}
	
}
