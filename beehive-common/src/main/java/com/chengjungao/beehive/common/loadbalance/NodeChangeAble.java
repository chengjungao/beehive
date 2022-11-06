package com.chengjungao.beehive.common.loadbalance;

import java.util.List;

public interface NodeChangeAble {
	
	public List<Node> getAllNodes();
	
	public void nodeFail(Node node);
	
	public void nodeRecover(Node node);
	
	public void shutdown();

}
