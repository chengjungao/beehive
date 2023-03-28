package com.chengjungao.base.service.factory;

import com.chengjungao.base.service.ServiceClient;
import com.chengjungao.base.service.client.CommonServiceClient;
import com.chengjungao.base.service.config.ProxyConfig;
import com.chengjungao.base.service.config.ServerConfig;
import com.chengjungao.base.service.config.ServiceFactoryConfig;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.List;

public class CommonServiceClientFactory extends AbstractServiceClientFactory{
    public CommonServiceClientFactory(String serviceName, List<ServerConfig> serverConfigs, ServiceFactoryConfig serviceFactoryConfig) {
        super(serviceName, serverConfigs, serviceFactoryConfig);
    }

    @Override
    public ServiceClient<CloseableHttpClient> createServiceClient(HttpClientConnectionManager httpClientConnectionManager, String url, String pingPath, int weight, HttpRequestRetryHandler httpRequestRetryHandler, ProxyConfig proxy) {
        return new CommonServiceClient(httpClientConnectionManager,url,pingPath,weight,httpRequestRetryHandler,proxy);
    }
}
