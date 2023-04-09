package com.chengjungao.base.service.router;

import com.chengjungao.base.service.Params;
import com.chengjungao.base.service.params.CommonParams;

/**
 * @author chengjungao
 * @date 2023/4/6
 * @description Service Router
 */
public interface ServiceRouter {
    /**
     * 执行Service请求
     * @param serviceName Service名称，映射yml中的service.name
     * @param params 请求参数
     * @param <T> 返回类型
     * @return
     */
    public <T> T execute(String serviceName, CommonParams params);

    /**
     * 销毁ServiceRouter
     * @return
     */
    public void destroy();
}
