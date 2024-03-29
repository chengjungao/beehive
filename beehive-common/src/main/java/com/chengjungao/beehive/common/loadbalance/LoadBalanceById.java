package com.chengjungao.beehive.common.loadbalance;

/**
 * 通过唯一标识获取节点方式的负载均衡
 * @author wolf
 *
 */
public interface LoadBalanceById<T extends Node> extends NodeChangeable<T>{
	
	/**
	 * 通过负载因子获取一个有效节点
	 * @param identify
	 * @return
	 */
	public T selectNode(String identify);
	
}
