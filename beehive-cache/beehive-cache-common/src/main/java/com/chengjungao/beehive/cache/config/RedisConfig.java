package com.chengjungao.beehive.cache.config;

import java.util.List;

public class RedisConfig {
	private List<String> nodeAddress;
	
	private String name;
	
	private int maxConnection;
	
	private int timeout;
	
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
