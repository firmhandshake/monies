package com.piotrglazar.wellpaidwork.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoFluffJobPostings {

    private final List<NoFluffJob> postings;

    @JsonCreator
    public NoFluffJobPostings(@JsonProperty("postings") List<NoFluffJob> postings) {
        this.postings = ImmutableList.copyOf(postings);
    }

    public List<NoFluffJob> getPostings() {
        return postings;
    }
}
