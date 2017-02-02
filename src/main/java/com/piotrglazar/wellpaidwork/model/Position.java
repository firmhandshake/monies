package com.piotrglazar.wellpaidwork.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Position {
    DEVELOPER,
    ENGINEER,
    DESIGNER,
    MANAGER,
    RECRUITER,
    ARCHITECT,
    CHIEF,
    EXPERT,
    ADMINISTRATOR,
    ANALYST;

    private static final Map<String, Position> names = ImmutableMap.copyOf(Stream.of(Position.values())
            .collect(Collectors.toMap(Position::name, Function.identity())));

    public static Optional<Position> fromString(String position) {
        return Optional.ofNullable(names.get(position.toUpperCase()));
    }
}
