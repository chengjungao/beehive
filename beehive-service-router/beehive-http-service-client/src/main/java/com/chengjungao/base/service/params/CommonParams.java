package com.chengjungao.base.service.params;

import com.alibaba.fastjson2.JSONObject;
import com.chengjungao.base.service.Params;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

public class CommonParams extends Params<JSONObject> {

    @Override
    public HttpEntity buildEntity() {
        addHeader("Content-Type", "application/json");
        return new StringEntity(body.toJSONString(), "UTF-8");
    }
}
