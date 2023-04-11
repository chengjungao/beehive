package com.chengjungao.base.service.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "beehive.router")
public class ServiceRouterConfiguration {
    private List<ServiceConfig> services;

    public List<ServiceConfig> getServices() {
        return services;
    }

    public void setServices(List<ServiceConfig> services) {
        this.services = services;
    }
}
