package com.chengjungao.beehive.common.loadbalance;

/**
 * 负载均衡接口
 * @author wolf
 */
public interface LoadBalance extends NodeChangeable{
	
	/**
	 * 获取一个有效节点
	 * @return
	 */
	public Node selectNode();
	
}
