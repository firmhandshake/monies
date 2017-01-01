package com.piotrglazar.wellpaidwork.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;

public enum EmploymentType {
    PERMANENT,
    CONTRACT;

    private static final Map<String, EmploymentType> names = ImmutableMap.of("permanent", PERMANENT, "contract", CONTRACT,
            "b2b", CONTRACT);

    public static Optional<EmploymentType> fromString(String employmentType) {
        return Optional.ofNullable(names.get(employmentType.toLowerCase()));
    }
}
