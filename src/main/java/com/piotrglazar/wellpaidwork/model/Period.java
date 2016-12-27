package com.piotrglazar.wellpaidwork.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;

public enum Period {
    DAY,
    MONTH,
    YEAR;

    private static final Map<String, Period> names = ImmutableMap.of("day", DAY, "month", MONTH, "year", YEAR);

    public static Optional<Period> fromString(String period) {
        return Optional.ofNullable(names.get(period.toLowerCase()));
    }
}
