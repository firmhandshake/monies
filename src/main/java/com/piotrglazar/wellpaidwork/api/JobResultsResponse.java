package com.piotrglazar.wellpaidwork.api;

import com.google.common.collect.ImmutableList;
import com.piotrglazar.wellpaidwork.model.JobOffer;

import java.util.List;

public class JobResultsResponse {

    private final int numberOfSources;

    private final List<String> descriptions;

    private final List<JobOffer> jobs;

    public JobResultsResponse(int numberOfSources, List<String> descriptions, List<JobOffer> jobs) {
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
