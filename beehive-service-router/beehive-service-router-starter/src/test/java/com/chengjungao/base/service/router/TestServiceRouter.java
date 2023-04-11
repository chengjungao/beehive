package com.chengjungao.base.service.router;

import com.chengjungao.base.service.Params;
import com.chengjungao.base.service.params.CommonParams;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@ComponentScan
@SpringBootConfiguration
@RunWith(SpringRunner.class)
public class TestServiceRouter {

    @Resource
    private ServiceRouter serviceRouter;

    @Test
    public void test() {
        CommonParams params = new CommonParams();
        params.addParam("bogId", "11");
        String res = serviceRouter.execute("serviceTest",params);
        Assert.assertNotNull(res);
        System.out.println(res);
    }
}
