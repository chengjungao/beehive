package com.chengjungao.base.service;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Params<Body> {
    private static final Pattern PATTERN = Pattern.compile("(\\$\\{.*?\\})");

    private Map<String, List<Object>> params;

    protected Body body;

    private List<Header> headers;

    public Params(Map<String, List<Object>> params, Body body, List<Header> headers) {
        this.params = params;
        this.body = body;
        this.headers = headers;
    }

    public Params(Map<String, List<Object>> params) {
        this.params = params;
    }

    public Params() {
        this.params = new HashMap<>();
        this.headers = new ArrayList<>();
    }

    public Map<String, List<Object>> getParams() {
        return params;
    }

    public void setParams(Map<String, List<Object>> params) {
        this.params = params;
    }

    public abstract HttpEntity buildEntity();

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public void addParam(String key, Object value) {
        List<Object> values = this.params.get(key);
        if (values == null){
            values = new ArrayList<>();
            this.params.put(key,values);
        }
        values.add(value);
    }

    public void addParam(String key, List<Object> values) {
        this.params.put(key, values);
    }

    public void addParams(Map<String, List<Object>> params) {
        this.params.putAll(params);
    }

    public void addParam(Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            this.addParam(entry.getKey(), entry.getValue());
        }
    }

    public void addHeader(Header header) {
        this.headers.add(header);
    }

    public void addHeader(String name, String value) {
        this.headers.add(new BasicHeader(name, value));
    }

    /**
     * 构建url
     * @param url
     * @param path
     * @return
     */
    public String buildUrl(String url,String path) {
        if (this.params == null || this.params.isEmpty()){
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (path != null && !path.isEmpty()){
            path = dealPathPlaceholder(path);
            if (!path.startsWith("/")){
                sb.append("/");
            }
            sb.append(path);
        }

        if (!url.contains("?")){
            sb.append("?");
        }else {
            sb.append("&");
        }

        for (Map.Entry<String, List<Object>> entry : this.params.entrySet()) {
            String key = null;
            try {
                key = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            List<Object> values = entry.getValue();
            for (Object value : values) {
                String valueStr = null;
                try {
                    valueStr = URLEncoder.encode(value.toString(), StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                sb.append(key).append("=").append(valueStr).append("&");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 处理路径中的占位符
     * @param path
     * @return
     */
    private String dealPathPlaceholder(String path){
        Matcher matcher = PATTERN.matcher(path);

        while (matcher.find()){
            String group = matcher.group();
            String key = group.substring(2,group.length() - 1);
            List<Object> values = this.params.get(key);
            if (values != null && !values.isEmpty()){
                path = path.replace(group,values.get(0).toString());
            }
        }
        return path;
    }

    public Header[] buildHeaders() {
        return this.headers.toArray(new Header[headers.size()]);
    }
}
