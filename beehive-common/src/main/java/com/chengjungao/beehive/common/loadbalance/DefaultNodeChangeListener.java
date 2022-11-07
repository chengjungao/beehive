package com.chengjungao.beehive.common.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * 默认的节点监听器
 * @author wolf
 */
public class DefaultNodeChangeListener implements NodeChangeListener {
	private AtomicBoolean run = new AtomicBoolean(false);
	private ReentrantReadWriteLock reentrantLock;
	private ReadLock readLock;
	private WriteLock writeLock;
	private long healthCheckIntverval;

	public DefaultNodeChangeListener() {
		this(30 * 1000);
	}

	public DefaultNodeChangeListener(long healthCheckIntverval) {
		this.reentrantLock = new ReentrantReadWriteLock();
		this.readLock = reentrantLock.readLock();
		this.writeLock = reentrantLock.writeLock();
		this.healthCheckIntverval = healthCheckIntverval;
	}

	@Override
	public void start(NodeChangeable nodeChangeable) {
		List<Node> allNodes =  nodeChangeable.getAllNodes();
		run.set(true);
		for (Node node : allNodes) {
			Thread healthThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(run.get()) {
						Boolean health =  node.healthCheck();
						readLock.tryLock();
						Boolean needChange = health ^ nodeChangeable.getAliveNodes().contains(node);
						readLock.unlock();
						if (needChange) {
							writeLock.lock();
							if (health) {
								nodeChangeable.nodeRecover(node);
							}else {
								nodeChangeable.nodeFail(node);
							}
							writeLock.unlock();
						}
						try {
							Thread.sleep(healthCheckIntverval);
						} catch (InterruptedException e) {
							// ignore
						}
					}
				}
			});
			healthThread.setDaemon(true);
			healthThread.setName("health-"+node.getNodeName());
			healthThread.start();
		}
	}

	@Override
	public void shutdown() {
		this.run.set(false);
	}
	
}
