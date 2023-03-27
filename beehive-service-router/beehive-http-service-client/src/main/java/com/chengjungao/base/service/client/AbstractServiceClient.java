package com.chengjungao.base.service.client;

import com.chengjungao.base.service.*;
import com.chengjungao.base.service.config.ProxyConfig;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServiceClient implements ServiceClient<CloseableHttpClient> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServiceClient.class);

    private final CloseableHttpClient httpClient;

    private final String url;

    protected String pingPath;

    private int weight = 1;

    public AbstractServiceClient(HttpClientConnectionManager httpClientConnectionManager, String url, String pingPath, int weight,
                                 HttpRequestRetryHandler httpRequestRetryHandler, ProxyConfig proxy) {
        HttpClientBuilder builder = HttpClients.custom().
                setConnectionManager(httpClientConnectionManager).
                setRetryHandler(httpRequestRetryHandler).
                setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        if (proxy != null) {
            CredentialsProvider cp = new BasicCredentialsProvider();
            if (proxy.getUsername() != null && proxy.getPassword() != null) {
                cp.setCredentials(new AuthScope(AuthScope.ANY_HOST,AuthScope.ANY_PORT),
                        new NTCredentials(proxy.getUsername(), proxy.getPassword(), null, null));
            }
            builder.setDefaultCredentialsProvider(cp);
            builder.setProxy(new HttpHost(proxy.getHost(), proxy.getPort()));
        }
        this.httpClient = builder.build();
        this.url = url;
        this.pingPath = pingPath;
        this.weight = weight;
    }

    public AbstractServiceClient(HttpClientConnectionManager httpClientConnectionManager, String url, String pingPath, int weight,
                                 HttpRequestRetryHandler httpRequestRetryHandler) {
        this(httpClientConnectionManager, url, pingPath, weight,httpRequestRetryHandler, null);
    }

    public AbstractServiceClient(HttpClientConnectionManager httpClientConnectionManager, String url, String pingPath) {
        this(httpClientConnectionManager, url, pingPath, 1,new DefaultHttpRequestRetryHandler(2, false));
    }

    @Override
    public String execute(Command<CloseableHttpClient> command) {
        return execute(command, new ResponseParser.StringResponseParser());
    }

    @Override
    public <Response> Response execute(Command<CloseableHttpClient> command, Class<Response> responseClass) {
        return execute(command, new ResponseParser.ObjectResponseParser<Response>(responseClass));
    }

    @Override
    public <Response> Response execute(Command<CloseableHttpClient> command,
                                       ResponseParser<Response> responseParser) {
        HttpRequestBase request = null;
        try{
            Params<?> params = command.getParams();
            if (command.getMethod() == Method.POST) {
                HttpPost post = new HttpPost(params.buildUrl(url,command.getPath()));
                post.setEntity(params.buildEntity());
                request = post;
            }else if (command.getMethod() == Method.GET) {
                request = new HttpGet(params.buildUrl(url,command.getPath()));
            }
            if (params.getHeaders() != null && params.getHeaders().size() > 0) {
                request.setHeaders(params.buildHeaders());
            }
            configTimeout(request,command.getTimeout());
            return request(request, responseParser);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
        }

    }

    private void configTimeout(HttpRequestBase request,int timeout) {
        request.setConfig(RequestConfig.custom().
                setConnectTimeout(timeout).
                setSocketTimeout(timeout).
                setConnectionRequestTimeout(timeout).
                build());

    }

    private <Response> Response request(HttpRequestBase request, ResponseParser<Response> responseParser) {
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request, HttpClientContext.create());
            return responseParser.parse(response.getEntity());
        } catch (Exception e) {
            LOGGER.error("request error", e);
            throw new RuntimeException(e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    LOGGER.info("close response error", e);
                }
            }
        }
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public void close() {
        try {
            httpClient.close();
        } catch (Exception e) {
            LOGGER.info("close http client error", e);
        }
    }
}
