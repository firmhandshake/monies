package com.piotrglazar.wellpaidwork.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoFluffJob {

    private static final int FULL_REMOTE_WORK_PERCENTAGE = 100;

    private final String id;
    private final String name;
    private final String city;
    private final String category;
    private final String title;
    private final String level;
    private final int remote;
    private final int locationCount;
    private final int notRemoteLocationCount;

    @JsonCreator
    public NoFluffJob(@JsonProperty("id") String id, @JsonProperty("name") String name,
                      @JsonProperty("city") String city, @JsonProperty("category") String category,
                      @JsonProperty("title") String title, @JsonProperty("level") String level,
                      @JsonProperty("remote") int remote, @JsonProperty("locationCount") int locationCount,
                      @JsonProperty("notRemoteLocationCount") int notRemoteLocationCount) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.category = category;
        this.title = title;
        this.level = level;
        this.remote = remote;
        this.locationCount = locationCount;
        this.notRemoteLocationCount = notRemoteLocationCount;
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

    public int getRemote() {
        return remote;
    }

    public int getLocationCount() {
        return locationCount;
    }

    public int getNotRemoteLocationCount() {
        return notRemoteLocationCount;
    }

    public boolean isRemoteWorkPossible() {
        return remote == FULL_REMOTE_WORK_PERCENTAGE && locationCount - notRemoteLocationCount > 0;
    }
}
