package com.piotrglazar.wellpaidwork.util;

import com.google.common.collect.ImmutableSet;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtils {

    private StringUtils() {
        // utility class
    }

    public static Set<String> splitAndTrim(String value) {
        return ImmutableSet.copyOf(Stream.of(value.split(",")).map(String::trim).collect(Collectors.toSet()));
    }
}
