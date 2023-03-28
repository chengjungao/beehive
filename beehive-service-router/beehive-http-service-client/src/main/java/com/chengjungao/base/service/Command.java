package com.chengjungao.base.service;

import org.apache.http.client.HttpClient;

public class Command <T extends HttpClient>{

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

    public Command(Params params, Method method, ServiceClient<T> serviceClient, String path) {
        this.params = params;
        this.method = method;
        this.serviceClient = serviceClient;
        this.path = path;
    }

    public String execute(){
        return serviceClient.execute(this);
    }

    public <Response> Response execute(Class<Response> responseClass){
        return serviceClient.execute(this,responseClass);
    }

    public <Response> Response execute(ResponseParser<Response> responseParser){
        return serviceClient.execute(this,responseParser);
    }

    public Method getMethod() {
        return method;
    }

    public Params getParams() {
        return params;
    }

    public ServiceClient<T> getServiceClient() {
        return serviceClient;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getPath() {
        return path;
    }

}
