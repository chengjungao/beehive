package com.chengjungao.beehive.cache.config;

import java.util.List;

/**
 * 缓存Redis Cluster配置
 * 
 * @author wolf
 *
 */
public class RedisConfig {
	/**
	 * Redis节点，采用Redisson配置方式，eg：redis://127.0.0.1:6379
	 */
	private List<String> nodeAddress;
	
	/**
	 * Redis Cluster名称，用户标识Redis Cluster
	 */
	private String name;
	
	/**
	 * Redis Cluster最大连接数
	 */
	private int maxConnection;
	
	/**
	 * Redis Cluster超时时间
	 */
	private int timeout;
	
	/**
	 * Redis Cluster密码
	 */
	private String password;
	
	public RedisConfig() {
		super();
	}

	public RedisConfig(List<String> nodeAddress, String name, int maxConnection, int timeout, String password) {
		super();
		this.nodeAddress = nodeAddress;
		this.name = name;
		this.maxConnection = maxConnection;
		this.timeout = timeout;
		this.password = password;
	}

	public String[] getNodeAddress() {
		return nodeAddress.toArray(new String[nodeAddress.size()]);
	}

	public String getName() {
		return this.name;
	}

	public int getMaxConnection() {
		return this.maxConnection;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public String getPassword() {
		return this.password;
	}
	
}
