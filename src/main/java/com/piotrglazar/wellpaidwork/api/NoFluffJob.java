package com.piotrglazar.wellpaidwork.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoFluffJob {

    private final String id;
    private final String name;
    private final String city;
    private final String category;
    private final String title;
    private final String level;

    @JsonCreator
    public NoFluffJob(@JsonProperty("id") String id, @JsonProperty("name") String name,
                      @JsonProperty("city") String city, @JsonProperty("category") String category,
                      @JsonProperty("title") String title, @JsonProperty("level") String level) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.category = category;
        this.title = title;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
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
