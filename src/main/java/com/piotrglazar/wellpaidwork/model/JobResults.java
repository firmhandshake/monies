package com.piotrglazar.wellpaidwork.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class JobResults {

    private final int numberOfSources;

    private final List<String> descriptions;

    private final List<JobOffer> jobs;

    public JobResults(int numberOfSources, List<String> descriptions, List<JobOffer> jobs) {
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

    public List<JobOffer> getJobs() {
        return jobs;
    }
}
