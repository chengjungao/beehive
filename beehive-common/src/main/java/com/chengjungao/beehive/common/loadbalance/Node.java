package com.chengjungao.beehive.common.loadbalance;

import java.io.Closeable;

public interface Node extends Closeable{

	public Boolean healthCheck();
}
