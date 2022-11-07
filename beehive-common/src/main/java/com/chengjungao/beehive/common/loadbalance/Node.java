package com.chengjungao.beehive.common.loadbalance;

import java.io.Closeable;

/**
 * 节点定义接口，节点定义一定要重写hashCode和equals方法
 * @author wolf
 */
public interface Node extends Closeable{
	
	public String getNodeName();
	
	public int hashCode();
	
	public boolean equals(Object obj);

	public Boolean healthCheck();
}
