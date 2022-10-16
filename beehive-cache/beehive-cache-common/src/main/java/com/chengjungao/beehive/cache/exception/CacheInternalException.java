package com.chengjungao.beehive.cache.exception;

public class CacheInternalException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CacheInternalException() {
		super();
	}

	public CacheInternalException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public CacheInternalException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CacheInternalException(String arg0) {
		super(arg0);
	}

	public CacheInternalException(Throwable arg0) {
		super(arg0);
	}
	
}
