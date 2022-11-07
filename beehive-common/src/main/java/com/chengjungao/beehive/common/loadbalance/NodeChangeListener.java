package com.chengjungao.beehive.common.loadbalance;

/**
 * 节点监听器，节点发生变化时进行处理
 * @author wolf
 *
 */
public interface NodeChangeListener {
	
	public void start(NodeChangeable nodeChangeAble);
	
	public void shutdown();

}
