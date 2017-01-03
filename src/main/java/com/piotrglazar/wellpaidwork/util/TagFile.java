package com.piotrglazar.wellpaidwork.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TagFile {

    private final Set<String> tags;
    private final Map<String, String> synonyms;

    public TagFile(Set<String> tags, Map<String, String> synonyms) {
        this.tags = ImmutableSet.copyOf(tags);
        this.synonyms = ImmutableMap.copyOf(synonyms);
    }

    public Set<String> getTags() {
        return tags;
    }

    public Map<String, String> getSynonyms() {
        return synonyms;
    }

    public static TagFile empty() {
        return new TagFile(Collections.emptySet(), Collections.emptyMap());
    }
}
