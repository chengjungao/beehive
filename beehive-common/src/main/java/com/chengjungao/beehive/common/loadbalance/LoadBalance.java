package com.chengjungao.beehive.common.loadbalance;

public interface LoadBalance extends NodeChangeAble{

	public Node selectNode();
	
}
