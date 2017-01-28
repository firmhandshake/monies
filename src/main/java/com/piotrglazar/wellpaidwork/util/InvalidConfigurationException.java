package com.piotrglazar.wellpaidwork.util;

import com.google.common.base.Joiner;

import java.util.List;

public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException(List<String> cities) {
        super(buildMessage(cities));
    }

    private static String buildMessage(List<String> cities) {
        return "Cities needing config: ".concat(Joiner.on(", ").join(cities));
    }
}
