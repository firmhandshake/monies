package com.piotrglazar.wellpaidwork.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TitleTags {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Map<String, String> synonyms;
    private final Set<String> tags;

    public TitleTags(Set<String> tags, Map<String, String> synonyms) {
        this.synonyms = ImmutableMap.copyOf(synonyms);
        this.tags = ImmutableSet.copyOf(tags);
    }

    public Set<String> tags(String rawTitle) {
        String[] tokens = rawTitle.toLowerCase().split("[/\\s\\.,()]");
        return Stream.of(tokens)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .flatMap(this::toTitleTags)
                .collect(Collectors.toSet());
    }

    private Stream<String> toTitleTags(String rawToken) {
        if (tags.contains(rawToken)) {
            return Stream.of(rawToken);
        } else if (synonyms.containsKey(rawToken)) {
            return Stream.of(synonyms.get(rawToken));
        } else {
            logger.info("Unknown title token {}", rawToken);
            return Stream.empty();
        }
    }
}
