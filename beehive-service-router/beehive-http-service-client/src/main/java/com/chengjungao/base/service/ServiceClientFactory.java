package com.chengjungao.base.service;


import com.chengjungao.base.service.config.ProxyConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.HttpClientConnectionManager;

public interface ServiceClientFactory<T extends HttpClient> {
    /**
     * 创建服务客户端
     * @return
     */
    public ServiceClient<T> createServiceClient(HttpClientConnectionManager httpClientConnectionManager, String url, String pingPath, int weight,
                                                HttpRequestRetryHandler httpRequestRetryHandler, ProxyConfig proxy);

    /**
     * 根据负载因子获取服务客户端
     * @param payload
     * @return
     */
    public ServiceClient<T> getServiceClient(String payload);

    /**
     * 随机获取服务客户端
     * @return
     */
    public ServiceClient<T> getRandomServiceClient();

    /**
     * 销毁服务客户端
     * @param serviceClient
     */
    public void destoryServiceClient(ServiceClient<T> serviceClient);

    /**
     * 关闭服务客户端工厂
     */
    public void close();
}
