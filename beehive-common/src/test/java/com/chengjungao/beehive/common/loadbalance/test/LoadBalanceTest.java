package com.chengjungao.beehive.common.loadbalance.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.chengjungao.beehive.common.loadbalance.ConsistentHashLoadBalance;
import org.junit.Test;

import com.chengjungao.beehive.common.loadbalance.LoadBalanceById;
import com.chengjungao.beehive.common.loadbalance.Node;

public class LoadBalanceTest {
	
	@Test
	public void test() {
		List<Node> nodes = new ArrayList<>();
		nodes.add(new NodeTest("127.0.0.1:8080"));
		nodes.add(new NodeTest("127.0.0.1:8081"));
		nodes.add(new NodeTest("127.0.0.1:8082"));
		LoadBalanceById loadBalance = new ConsistentHashLoadBalance(nodes);
		for (int i = 0; i < 30; i++) {
			System.out.println(loadBalance.selectNode("request" + i+i));
		}
		
	}
	
	
	class NodeTest implements Node{
		private String host;
		
		public NodeTest(String host) {
			super();
			this.host = host;
		}

		@Override
		public void close() throws IOException {
			
		}

		@Override
		public String getNodeName() {
			return host;
		}

		@Override
		public int getWeight() {
			return 1;
		}

		@Override
		public Boolean healthCheck() {
			return true;
		}

		@Override
		public String toString() {
			return "NodeTest [host=" + host + "]";
		}
	}
}
