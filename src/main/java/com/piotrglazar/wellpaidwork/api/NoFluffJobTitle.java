package com.piotrglazar.wellpaidwork.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoFluffJobTitle {

    private final String category;
    private final String title;
    private final String level;

    @JsonCreator
    public NoFluffJobTitle(@JsonProperty("category") String category,
                           @JsonProperty("title") String title,
                           @JsonProperty("level") String level) {
        this.category = category;
        this.title = title;
        this.level = level;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getLevel() {
        return level;
    }
}
