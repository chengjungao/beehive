package com.chengjungao.beehive.common.loadbalance;

public interface LoadBalance {

	public Node selectNode();
	
	public void shutdown();
}
