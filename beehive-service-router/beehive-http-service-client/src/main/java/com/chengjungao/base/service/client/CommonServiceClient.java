package com.chengjungao.base.service.client;

import com.chengjungao.base.service.Command;
import com.chengjungao.base.service.Method;
import com.chengjungao.base.service.config.ProxyConfig;
import com.chengjungao.base.service.params.CommonParams;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;

public class CommonServiceClient extends AbstractServiceClient{


    public CommonServiceClient(HttpClientConnectionManager httpClientConnectionManager,
                               String url, String pingPath, int weight,
                               HttpRequestRetryHandler httpRequestRetryHandler, ProxyConfig proxy) {
        super(httpClientConnectionManager, url, pingPath, weight, httpRequestRetryHandler, proxy);
    }

    public CommonServiceClient(HttpClientConnectionManager httpClientConnectionManager,
                               String url, String pingPath, int weight,
                               HttpRequestRetryHandler httpRequestRetryHandler) {
        super(httpClientConnectionManager, url, pingPath, weight, httpRequestRetryHandler);
    }

    public CommonServiceClient(HttpClientConnectionManager httpClientConnectionManager,
                               String url, String pingPath) {
        super(httpClientConnectionManager, url, pingPath);
    }

    @Override
    public String getNodeName() {
        return this.url;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public Boolean healthCheck() {
        if (this.pingPath == null) {
            return true;
        }

        Command<CloseableHttpClient> pingCommand = new Command(new CommonParams(), Method.GET,
                this, this.pingPath);

        String response = pingCommand.execute();
        return response.contains("OK") || response.contains("ok") || response.contains("200");
    }
}
