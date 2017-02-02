package com.piotrglazar.wellpaidwork.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Category {
    BACKEND,
    FRONTEND,
    MOBILE,
    FULLSTACK,
    HR,
    SUPPORT,
    TESTING,
    DEVOPS,
    PROJECT_MANAGER,
    TRAINEE,
    UX,
    BUSINESS_ANALYST,
    BUSINESS_INTELLIGENCE;

    private static final Map<String, Category> names = ImmutableMap.copyOf(Stream.of(Category.values())
            .collect(Collectors.toMap(Category::name, Function.identity())));

    public static Optional<Category> fromString(String category) {
        return Optional.ofNullable(names.get(category.toUpperCase()));
    }
}
