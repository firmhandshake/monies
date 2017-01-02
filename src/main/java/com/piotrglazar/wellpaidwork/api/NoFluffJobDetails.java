package com.piotrglazar.wellpaidwork.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoFluffJobDetails {

    private final String id;
    private final DateTime posted;
    private final NoFluffJobEssentials essentials;
    private final NoFluffJobTitle title;

    @JsonCreator
    public NoFluffJobDetails(@JsonProperty("id") String id,
                             @JsonProperty("posted") DateTime posted,
                             @JsonProperty("essentials") NoFluffJobEssentials essentials,
                             @JsonProperty("title") NoFluffJobTitle title) {
        this.id = id;
        this.posted = posted;
        this.essentials = essentials;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public DateTime getPosted() {
        return posted;
    }

    public NoFluffJobEssentials getEssentials() {
        return essentials;
    }

    public NoFluffJobTitle getTitle() {
        return title;
    }
}
