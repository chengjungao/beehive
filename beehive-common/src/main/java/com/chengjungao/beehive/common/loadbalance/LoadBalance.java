package com.chengjungao.beehive.common.loadbalance;

public interface LoadBalance<I> {
	
	public Node selectNode(I identify);
	
	
	public void shutdown();
}
