package com.piotrglazar.wellpaidwork.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoFluffJobDetails {

    private final String id;
    private final DateTime posted;
    private final NoFluffJobEssentials essentials;
    private final NoFluffJobTitle title;
    private final List<NoFluffTechnology> technologiesUsed;
    private final List<NoFluffTechnology> nices;
    private final List<NoFluffTechnology> musts;

    @JsonCreator
    public NoFluffJobDetails(@JsonProperty("id") String id,
                             @JsonProperty("posted") DateTime posted,
                             @JsonProperty("essentials") NoFluffJobEssentials essentials,
                             @JsonProperty("title") NoFluffJobTitle title,
                             @JsonProperty("technologiesUsed") List<NoFluffTechnology> technologiesUsed,
                             @JsonProperty("nices") List<NoFluffTechnology> nices,
                             @JsonProperty("musts") List<NoFluffTechnology> musts) {
        this.id = id;
        this.posted = posted;
        this.essentials = essentials;
        this.title = title;
        this.technologiesUsed = ImmutableList.copyOf(technologiesUsed);
        this.nices = ImmutableList.copyOf(nices);
        this.musts = ImmutableList.copyOf(musts);
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

    public List<NoFluffTechnology> getTechnologiesUsed() {
        return technologiesUsed;
    }

    public List<NoFluffTechnology> getNices() {
        return nices;
    }

    public List<NoFluffTechnology> getMusts() {
        return musts;
    }
}
