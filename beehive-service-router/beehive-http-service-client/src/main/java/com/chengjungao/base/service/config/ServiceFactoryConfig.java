package com.chengjungao.base.service.config;

public class ServiceFactoryConfig {
    private int maxTotal;

    private int maxPerRoute;

    private int connectTimeout;

    private int socketTimeout;

    private int connectionRequestTimeout;

    private int maxRetry;

    private int validateAfterInactivity;

    private String pingPath;

    private ProxyConfig proxyConfig;

    public ServiceFactoryConfig() {
        this.maxTotal = 100;
        this.maxPerRoute = 20;
        this.connectTimeout = 5000;
        this.socketTimeout = 5000;
        this.connectionRequestTimeout = 5000;
        this.maxRetry = 3;
        this.validateAfterInactivity = 10000;
    }

    public ServiceFactoryConfig(int maxTotal, int maxPerRoute, int connectTimeout, int socketTimeout, int connectionRequestTimeout, int maxRetry, int validateAfterInactivity, String pingPath, ProxyConfig proxyConfig) {
        this.maxTotal = maxTotal;
        this.maxPerRoute = maxPerRoute;
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.maxRetry = maxRetry;
        this.validateAfterInactivity = validateAfterInactivity;
        this.pingPath = pingPath;
        this.proxyConfig = proxyConfig;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    public int getValidateAfterInactivity() {
        return validateAfterInactivity;
    }

    public void setValidateAfterInactivity(int validateAfterInactivity) {
        this.validateAfterInactivity = validateAfterInactivity;
    }

    public String getPingPath() {
        return pingPath;
    }

    public void setPingPath(String pingPath) {
        this.pingPath = pingPath;
    }

    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public void setProxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }
}
