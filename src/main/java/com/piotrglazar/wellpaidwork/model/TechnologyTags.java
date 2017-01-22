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

public class TechnologyTags {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Set<String> tags;
    private final Map<String, String> synonyms;

    public TechnologyTags(Set<String> tags, Map<String, String> synonyms) {
        this.tags = ImmutableSet.copyOf(tags);
        this.synonyms = ImmutableMap.copyOf(synonyms);
    }

    public Set<String> tags(String rawTechnology) {
        String lowerCaseTechnology = rawTechnology.toLowerCase();

        Set<String> tokenizedTags = toTechTagsSet(lowerCaseTechnology);

        if (!tokenizedTags.isEmpty()) {
            return tokenizedTags;
        } else {
            return splitAndParse(lowerCaseTechnology);
        }
    }

    private Set<String> splitAndParse(String lowerCaseTechnology) {
        String[] tokens = lowerCaseTechnology.split("[:/\\s\\.()]");
        Set<String> tokenizedTags = Stream.of(tokens)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .flatMap(this::toTitleTags)
                .collect(Collectors.toSet());
        if (tokenizedTags.isEmpty()) {
            logger.info("Unknown technology token {}", lowerCaseTechnology);
        }
        return tokenizedTags;
    }

    private Stream<String> toTitleTags(String rawToken) {
        if (tags.contains(rawToken)) {
            return Stream.of(rawToken);
        } else if (synonyms.containsKey(rawToken)) {
            return Stream.of(synonyms.get(rawToken));
        } else {
            return Stream.empty();
        }
    }

    private Set<String> toTechTagsSet(String rawToken) {
        return toTitleTags(rawToken).collect(Collectors.toSet());
    }
}
