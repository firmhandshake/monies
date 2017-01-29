package com.piotrglazar.wellpaidwork.model;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CityTags {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Map<String, String> synonyms;
    private final Map<String, String> cities;

    public CityTags(Set<String> tags, Map<String, String> synonyms) {
        this.cities = ImmutableMap.copyOf(tags.stream().collect(Collectors.toMap(String::toLowerCase, Function.identity())));
        this.synonyms = ImmutableMap.copyOf(synonyms);
    }

    public String cityTag(String rawTag) {
        return cityTagOpt(rawTag)
                .orElseGet(() -> {
                    LOGGER.error("Unknown city tag " + rawTag);
                    return rawTag;
                });
    }

    public Optional<String> cityTagOpt(String rawTag) {
        String lowerCaseTag = rawTag.toLowerCase();
        Optional<String> exact = Optional.ofNullable(cities.get(lowerCaseTag));
        if (exact.isPresent()) {
            return exact;
        } else {
            return Optional.ofNullable(synonyms.get(lowerCaseTag));
        }
    }
}
