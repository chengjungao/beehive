package com.chengjungao.base.service.router.impl;

import com.chengjungao.base.service.ServiceClientFactory;
import com.chengjungao.base.service.autoconfigure.ServiceConfig;
import com.chengjungao.base.service.config.ServerConfig;
import com.chengjungao.base.service.config.ServiceFactoryConfig;
import com.chengjungao.base.service.factory.CommonServiceClientFactory;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceMapper {
    private static Map<String, ServiceClientFactory<CloseableHttpClient>>  serviceClientFactoryMap = new ConcurrentHashMap<>();

    public static void register(ServiceConfig serviceConfig){
        ServiceFactoryConfig config = new ServiceFactoryConfig();
        config.setConnectTimeout(serviceConfig.getTimeout());
        config.setSocketTimeout(serviceConfig.getTimeout());
        config.setConnectionRequestTimeout(serviceConfig.getTimeout());
        config.setMaxRetry(serviceConfig.getMaxRetry());
        config.setMaxPerRoute(serviceConfig.getMaxPerRoute());
        config.setMaxTotal(serviceConfig.getMaxTotal());
        config.setPingPath(serviceConfig.getPingPath());
        config.setValidateAfterInactivity(1000);
        List<ServerConfig> serverConfigs = new ArrayList<>();
        for (String server : serviceConfig.getUrls()) {
            serverConfigs.add(new ServerConfig(server,1));
        }
        ServiceClientFactory<CloseableHttpClient> serviceClientFactory = new CommonServiceClientFactory(serviceConfig.getName(), serverConfigs, config);
        register(serviceConfig.getName(),serviceClientFactory);
    }

    private static void register(String serviceName, ServiceClientFactory<CloseableHttpClient> serviceClientFactory){
        serviceClientFactoryMap.put(serviceName,serviceClientFactory);
    }

    public static ServiceClientFactory<CloseableHttpClient> getServiceClientFactory(String serviceName){
        return serviceClientFactoryMap.get(serviceName);
    }

    public static void unregister(String serviceName){
        ServiceClientFactory<CloseableHttpClient> serviceClientFactory = serviceClientFactoryMap.get(serviceName);
        if(serviceClientFactory != null){
            serviceClientFactory.close();
        }
    }

    public static void shutdown(){
        for (Map.Entry<String, ServiceClientFactory<CloseableHttpClient>> entry : serviceClientFactoryMap.entrySet()) {
            entry.getValue().close();
        }
    }


}
