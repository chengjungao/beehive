package com.chengjungao.base.service.factory;

import org.apache.http.conn.HttpClientConnectionManager;

public class IdleConnectionMonitor extends Thread{
    private final HttpClientConnectionManager connectionManager;

    public IdleConnectionMonitor(String name, HttpClientConnectionManager connectionManager) {
        super(name);
        super.setDaemon(true);
        this.connectionManager = connectionManager;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (this) {
                    wait(5000);
                    connectionManager.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // terminate
        }
    }
}
