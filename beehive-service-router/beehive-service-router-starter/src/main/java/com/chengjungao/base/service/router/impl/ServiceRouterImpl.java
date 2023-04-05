package com.chengjungao.base.service.router.impl;

import com.chengjungao.base.service.*;
import com.chengjungao.base.service.autoconfigure.ServiceConfig;
import com.chengjungao.base.service.autoconfigure.ServiceRouterConfiguration;
import com.chengjungao.base.service.params.CommonParams;
import com.chengjungao.base.service.router.ServiceRouter;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceRouterImpl implements ServiceRouter {
    private final HashMap<String,ServiceConfig> serviceConfigHashMap = new HashMap<>();
    public ServiceRouterImpl(ServiceRouterConfiguration serviceRouterConfiguration) {
        if (serviceRouterConfiguration == null) {
            throw new IllegalArgumentException("serviceRouterConfiguration is null");
        }
        for (ServiceConfig serviceConfig : serviceRouterConfiguration.getServices()) {
            ServiceMapper.register(serviceConfig);
            serviceConfigHashMap.put(serviceConfig.getName(),serviceConfig);
        }
    }

    @Override
    public <T> T execute(String serviceName, CommonParams params) {
        ServiceClientFactory<CloseableHttpClient> serviceClientFactory = ServiceMapper.getServiceClientFactory(serviceName);
        ServiceConfig serviceConfig = serviceConfigHashMap.get(serviceName);
        if (serviceClientFactory == null || serviceConfig == null) {
            throw new IllegalArgumentException("serviceName is not exist");
        }
        //merge params
        params.addParam(serviceConfig.getParams());
        ServiceClient<CloseableHttpClient> serviceClient = serviceClientFactory.getServiceClient(getPayload(params));
        Command<CloseableHttpClient> command = new Command<>(params, Method.getMethod(serviceConfig.getMethod()),serviceClient,serviceConfig.getPath());
        return (T)command.execute(serviceConfig.getResponseClass());
    }

    private String getPayload(CommonParams params){
        List<Integer> values = new ArrayList<>();
        for (Map.Entry<String,List<Object>> entry : params.getParams().entrySet()) {
            for (Object value : entry.getValue()) {
                values.add(value.hashCode());
            }
        }
        values.add(params.getBody().toString().hashCode());
        return String.valueOf(values.hashCode());
    }

    @Override
    public void destroy() {
        for (ServiceConfig serviceConfig : serviceConfigHashMap.values()) {
            ServiceMapper.unregister(serviceConfig.getName());
        }
    }
}