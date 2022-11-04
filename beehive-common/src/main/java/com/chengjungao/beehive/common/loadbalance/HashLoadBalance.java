package com.chengjungao.beehive.common.loadbalance;

public interface HashLoadBalance {
	
	public Node selectNode(String identify);
	
	
	public void shutdown();
}
