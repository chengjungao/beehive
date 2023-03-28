import com.alibaba.fastjson2.JSONObject;
import com.chengjungao.base.service.Command;
import com.chengjungao.base.service.Method;
import com.chengjungao.base.service.Params;
import com.chengjungao.base.service.ServiceClientFactory;
import com.chengjungao.base.service.config.ServerConfig;
import com.chengjungao.base.service.config.ServiceFactoryConfig;
import com.chengjungao.base.service.factory.CommonServiceClientFactory;
import com.chengjungao.base.service.params.CommonParams;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestServiceClient {
    ServiceClientFactory<CloseableHttpClient> serviceClientFactory;
    @Before
    public void init() {
        ServiceFactoryConfig config = new ServiceFactoryConfig();
        config.setConnectTimeout(1000);
        config.setSocketTimeout(1000);
        config.setConnectionRequestTimeout(1000);
        config.setMaxRetry(3);
        config.setValidateAfterInactivity(1000);

        List<ServerConfig> serverConfigs = new ArrayList<>();
        serverConfigs.add(new ServerConfig("http://www.chengjungao.cn",1));

        serviceClientFactory = new CommonServiceClientFactory("test", serverConfigs, config);
    }

    @Test
    public void test() {
        Params<JSONObject> params = new CommonParams();
        params.addParam("bogId", "12");
        Command<CloseableHttpClient> command = new Command(params, Method.GET,
                serviceClientFactory.getRandomServiceClient(), "/blog/${bogId}");

        String response = command.execute();
        System.out.println(response);
        Assert.assertNotNull(response);
    }

    @After
    public void destroy() {
        serviceClientFactory.close();
    }
}
