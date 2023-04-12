package com.chengjungao.base.service;

import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public interface ResponseParser<Response> {

    public Response parse(HttpEntity entity);

    public class StringResponseParser implements ResponseParser<String> {
        private final AbstractResponseHandler<String> handler;
        public StringResponseParser() {
                this.handler = new BasicResponseHandler();
        }

        @Override
        public String parse(HttpEntity entity) {
                try {
                    return this.handler.handleEntity(entity);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    public class JsonResponseParser implements ResponseParser<JSONObject> {
        private final AbstractResponseHandler<String> handler;
        public JsonResponseParser() {
                this.handler = new BasicResponseHandler();
        }

        @Override
        public JSONObject parse(HttpEntity entity) {
                try {
                    String response = this.handler.handleEntity(entity);
                    return JSONObject.parseObject(response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    public class ObjectResponseParser<Response> implements ResponseParser<Response> {
        private final AbstractResponseHandler<String> handler;
        private final Class<Response> clazz;
        public ObjectResponseParser(Class<Response> clazz) {
            this.handler = new BasicResponseHandler();
            this.clazz = clazz;
        }

        @Override
        public Response parse(HttpEntity entity) {
            try {
                String response = this.handler.handleEntity(entity);
                return JSONObject.parseObject(response, clazz);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class XmlResponseParser implements ResponseParser<Document> {
        private final AbstractResponseHandler<String> handler;
        public XmlResponseParser() {
            this.handler = new BasicResponseHandler();
        }

        @Override
        public Document parse(HttpEntity entity) {
            try {
                String response = this.handler.handleEntity(entity);
                return Jsoup.parse(response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
