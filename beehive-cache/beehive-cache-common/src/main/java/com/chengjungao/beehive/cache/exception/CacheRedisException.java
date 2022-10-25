package com.chengjungao.beehive.cache.exception;

/**
 * Beehive Cache 所依赖Redis异常
 * @author wolf
 *
 */
public class CacheRedisException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CacheRedisException() {
		super();
	}

	public CacheRedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CacheRedisException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheRedisException(String message) {
		super(message);
	}

	public CacheRedisException(Throwable cause) {
		super(cause);
	}
	
}
