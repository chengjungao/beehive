package com.chengjungao.base.service.factory;

import com.chengjungao.base.service.ServiceClient;
import com.chengjungao.base.service.ServiceClientFactory;
import com.chengjungao.base.service.config.ServerConfig;
import com.chengjungao.base.service.config.ServiceFactoryConfig;
import com.chengjungao.beehive.common.loadbalance.ConsistentHashLoadBalance;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractServiceClientFactory implements ServiceClientFactory<CloseableHttpClient> {
    private String serviceName;

    private HttpClientConnectionManager connectionManager;

    private ServiceFactoryConfig serviceFactoryConfig;

    private ConsistentHashLoadBalance loadBalance;

    public AbstractServiceClientFactory(String serviceName, List<ServerConfig>  serverConfigs, ServiceFactoryConfig serviceFactoryConfig) {
        this.serviceName = serviceName;
        this.serviceFactoryConfig = serviceFactoryConfig;
        this.connectionManager = buildConnectionManager(serviceName, serviceFactoryConfig);
        List<ServiceClient<CloseableHttpClient>> serviceClients = new ArrayList<>();
        for (ServerConfig serverConfig : serverConfigs) {
            serviceClients.add(createServiceClient(connectionManager, serverConfig.getUrl(), serviceFactoryConfig.getPingPath(),
                    serverConfig.getWeight(), new DefaultHttpRequestRetryHandler(), serviceFactoryConfig.getProxyConfig()));
        }
        this.loadBalance = new ConsistentHashLoadBalance(serviceClients);

    }

    protected HttpClientConnectionManager buildConnectionManager(String serviceName,ServiceFactoryConfig serviceFactoryConfig) {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(serviceFactoryConfig.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(serviceFactoryConfig.getMaxPerRoute());
        connectionManager.setDefaultSocketConfig(SocketConfig.custom().
                setSoTimeout(serviceFactoryConfig.getSocketTimeout()).
                setSoKeepAlive(true).
                build());
        connectionManager.setValidateAfterInactivity(serviceFactoryConfig.getValidateAfterInactivity());

        IdleConnectionMonitor idleConnectionMonitor = new IdleConnectionMonitor(serviceName + "-IdleConnectionMonitor", connectionManager);
        idleConnectionMonitor.start();
        return connectionManager;
    }

    @Override
    public ServiceClient<CloseableHttpClient> getServiceClient(String payload) {
        return (ServiceClient<CloseableHttpClient>)loadBalance.selectNode(payload);
    }

    @Override
    public ServiceClient<CloseableHttpClient> getRandomServiceClient() {
        return (ServiceClient<CloseableHttpClient>)loadBalance.selectNode(System.currentTimeMillis() + "");
    }

    @Override
    public void destoryServiceClient(ServiceClient<CloseableHttpClient> serviceClient) {
        try {
            serviceClient.close();
            loadBalance.nodeFail(serviceClient);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() {
        connectionManager.shutdown();
        loadBalance.shutdown();
    }
}
