package com.chengjungao.base.service.router;

import com.chengjungao.base.service.Params;
import com.chengjungao.base.service.params.CommonParams;

public interface ServiceRouter {

    public <T> T execute(String serviceName, CommonParams params);

    public void destroy();
}
