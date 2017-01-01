package com.piotrglazar.wellpaidwork.model;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Currency {
    PLN,
    EUR,
    USD,
    GBP;

    private static final Map<String, Currency> names = Stream.of(Currency.values()).collect(Collectors.toMap(Currency::name, Function.identity()));

    public static Optional<Currency> fromString(String currency) {
        return Optional.ofNullable(names.get(currency.toUpperCase()));
    }
}
