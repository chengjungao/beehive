package com.chengjungao.beehive.common.loadbalance;

/**
 * 通过唯一标识获取节点方式的负载均衡
 * @author wolf
 *
 */
public interface LoadBalanceById extends NodeChangeable{
	
	/**
	 * 通过负载因子获取一个有效节点
	 * @param identify
	 * @return
	 */
	public Node selectNode(String identify);
	
}
