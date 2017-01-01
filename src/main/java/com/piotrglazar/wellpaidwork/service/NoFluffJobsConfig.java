package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.model.SourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NoFluffJobsConfig implements SourceConfig {

    @Value("${sources.nofluffjobs.host}")
    private String host;

    @Value("${sources.nofluffjobs.port}")
    private int port;

    @Value("${sources.nofluffjobs.rootPath}")
    private String path;

    @Value("${sources.nofluffjobs.scheme}")
    private String scheme;

    @Override
    public String host() {
        return host;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public String scheme() {
        return scheme;
    }
}
