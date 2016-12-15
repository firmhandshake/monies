package com.piotrglazar.wellpaidwork.model;

import com.google.common.collect.ImmutableList;
import com.piotrglazar.wellpaidwork.api.NoFluffJob;

import java.util.List;

public class JobResults {

    private final int numberOfSources;

    private final List<String> descriptions;

    private final List<NoFluffJob> jobs;

    public JobResults(int numberOfSources, List<String> descriptions, List<NoFluffJob> jobs) {
        this.numberOfSources = numberOfSources;
        this.descriptions = ImmutableList.copyOf(descriptions);
        this.jobs = ImmutableList.copyOf(jobs);
    }

    public int getNumberOfSources() {
        return numberOfSources;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public List<NoFluffJob> getJobs() {
        return jobs;
    }
}
