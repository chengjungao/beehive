package com.chengjungao.beehive.common.loadbalance;

import java.util.List;

/**
 * 负载均衡接口的父接口，能动态调整Alive Node
 * @author wolf
 */
public interface NodeChangeable {
	
	/**
	 * 获取所有的节点
	 * @return
	 */
	public List<Node> getAllNodes();
	
	/**
	 * 获取所有的存活节点
	 * @return
	 */
	public List<Node> getAliveNodes();
	
	/**
	 * 节点down机处理
	 * @param node
	 */
	public void nodeFail(Node node);
	
	/**
	 * 节点恢复处理
	 * @param node
	 */
	public void nodeRecover(Node node);
	
	/**
	 * 停止所有的节点和节点监听
	 */
	public void shutdown();

}
