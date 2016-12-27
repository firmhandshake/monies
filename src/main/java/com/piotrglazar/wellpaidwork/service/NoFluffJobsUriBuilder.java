package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.api.NoFluffJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class NoFluffJobsUriBuilder {

    private final NoFluffJobsConfig config;

    @Autowired
    public NoFluffJobsUriBuilder(NoFluffJobsConfig config) {
        this.config = config;
    }

    public URI buildJobUri(NoFluffJob job) {
        return UriComponentsBuilder.newInstance()
                .port(config.port())
                .host(config.host())
                .path(config.path() + "/" + job.getId())
                .scheme(config.scheme())
                .build().toUri();
    }

    public URI buildUri() {
        return UriComponentsBuilder.newInstance()
                .port(config.port())
                .host(config.host())
                .path(config.path())
                .scheme(config.scheme())
                .build().toUri();
    }
}
