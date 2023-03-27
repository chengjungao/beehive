package com.chengjungao.beehive.common.loadbalance;

import java.util.List;

/**
 * 负载均衡接口的父接口，能动态调整Alive Node
 * @author wolf
 */
public interface NodeChangeable<T extends Node>{
	
	/**
	 * 获取所有的节点
	 * @return
	 */
	public List<T> getAllNodes();
	
	/**
	 * 获取所有的存活节点
	 * @return
	 */
	public List<T> getAliveNodes();
	
	/**
	 * 节点down机处理
	 * @param node
	 */
	public void nodeFail(T node);
	
	/**
	 * 节点恢复处理
	 * @param node
	 */
	public void nodeRecover(T node);
	
	/**
	 * 停止所有的节点和节点监听
	 */
	public void shutdown();

}
