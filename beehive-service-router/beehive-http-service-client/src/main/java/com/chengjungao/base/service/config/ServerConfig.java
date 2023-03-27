package com.chengjungao.base.service.config;

import java.util.Objects;

public class ServerConfig {
    private String url;

    private int weight;

    public ServerConfig(String url, int weight) {
        this.url = url;
        this.weight = weight;
    }

    public ServerConfig(String url) {
        this.url = url;
        this.weight = 1;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerConfig that = (ServerConfig) o;
        return weight == that.weight && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, weight);
    }
}
