package com.chengjungao.base.service;

import com.chengjungao.beehive.common.loadbalance.Node;
import org.apache.http.client.HttpClient;

/**
 * @author chengjungao
 * @description 服务客户端接口
 */
public interface ServiceClient<T extends HttpClient> extends Node {

    /**
     * 执行Service请求
     * @param command
     * @return
     */
    public String execute(Command<T> command);

    /**
     * 执行Service请求
     * @param command
     * @param responseClass
     * @return
     */
    public <Response>Response execute(Command<T> command,Class<Response> responseClass);

    /**
     * 执行Service请求
     * @param command
     * @param responseParser
     * @return
     */
    public <Response>Response execute(Command<T> command,ResponseParser<Response> responseParser);

    /**
     * 获取服务地址
     * @return
     */
    public String getURL();

}
