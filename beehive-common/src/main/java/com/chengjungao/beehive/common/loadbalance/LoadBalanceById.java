package com.chengjungao.beehive.common.loadbalance;

public interface LoadBalanceById extends NodeChangeAble{
	
	public Node selectNode(String identify);
	
}
