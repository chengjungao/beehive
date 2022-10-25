package com.chengjungao.beehive.cache.exception;

/**
 * Beehive Cache所依赖的Loader 服务异常
 * @author wolf
 *
 */
public class CacheServerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CacheServerException() {
		super();
	}

	public CacheServerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CacheServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheServerException(String message) {
		super(message);
	}

	public CacheServerException(Throwable cause) {
		super(cause);
	}
	
	
}
