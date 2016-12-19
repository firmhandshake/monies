package com.piotrglazar.wellpaidwork.model;

public interface SourceConfig {

    default String host() {
        return "localhost";
    }

    int port();

    String path();

    default String scheme() {
        return "http";
    }
}
