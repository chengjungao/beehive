package com.chengjungao.base.service.autoconfigure;

import java.util.List;
import java.util.Map;

public class ServiceConfig {
    String name;

    String path;

    String method;

    String pingPath;

    int timeout;

    int maxTotal;

    int maxPerRoute;

    int maxRetry;

    DeserializeType deserializeType;

    String responseClass;

    String responseParser;

    List<String> urls;

    Map<String,Object> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPingPath() {
        return pingPath;
    }

    public void setPingPath(String pingPath) {
        this.pingPath = pingPath;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
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

    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    public DeserializeType getDeserializeType() {
        return deserializeType;
    }

    public void setDeserializeType(DeserializeType deserializeType) {
        this.deserializeType = deserializeType;
    }

    public Class<?> getResponseClass() {
        try {
            return Class.forName(responseClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Response class not find!",e);
        }
    }

    public String getResponseClassName() {
        return responseClass;
    }

    public void setResponseClass(String responseClass) {
        this.responseClass = responseClass;
    }

    public String getResponseParser() {
        return responseParser;
    }

    public void setResponseParser(String responseParser) {
        this.responseParser = responseParser;
    }

    public Class<?> getResponseParserClass() {
        try {
            return Class.forName(responseParser);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ResponseParser class not find!",e);
        }
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
