package com.chengjungao.base.service;

import org.apache.http.client.HttpClient;

public abstract class Command <T extends HttpClient>{

    private Params params;

    private Method method;

    private ServiceClient<T> serviceClient;

    private int timeout;

    private String path;

    public Command(Params params, Method method, ServiceClient<T> serviceClient, int timeout, String path) {
        this.params = params;
        this.method = method;
        this.serviceClient = serviceClient;
        this.timeout = timeout;
        this.path = path;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public ServiceClient<T> getServiceClient() {
        return serviceClient;
    }

    public void setServiceClient(ServiceClient<T> serviceClient) {
        this.serviceClient = serviceClient;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
