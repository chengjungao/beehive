package com.chengjungao.base.service.router;

import com.chengjungao.base.service.autoconfigure.ServiceRouterConfiguration;
import com.chengjungao.base.service.router.impl.ServiceRouterImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ServiceRouterConfiguration.class)
@ConditionalOnClass(ServiceRouter.class)
public class RouterAutoconfiguration {

    @Bean
    public ServiceRouter serviceRouter(ServiceRouterConfiguration serviceRouterConfiguration) {
        return new ServiceRouterImpl(serviceRouterConfiguration);
    }
}
