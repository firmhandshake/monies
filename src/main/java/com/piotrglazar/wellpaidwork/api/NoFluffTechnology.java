package com.piotrglazar.wellpaidwork.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoFluffTechnology {

    private final int rank;
    private final String value;

    @JsonCreator
    public NoFluffTechnology(@JsonProperty("rank") int rank, @JsonProperty("value") String value) {
        this.rank = rank;
        this.value = value;
    }

    public int getRank() {
        return rank;
    }

    public String getValue() {
        return value;
    }
}
