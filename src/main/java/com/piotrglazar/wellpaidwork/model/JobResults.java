package com.piotrglazar.wellpaidwork.model;

import com.google.common.collect.ImmutableList;
import rx.Observable;

import java.util.List;

public class JobResults {

    private final int numberOfSources;

    private final List<String> descriptions;

    private final Observable<JobOffer> jobs;

    public JobResults(int numberOfSources, List<String> descriptions, Observable<JobOffer> jobs) {
        this.numberOfSources = numberOfSources;
        this.descriptions = ImmutableList.copyOf(descriptions);
        this.jobs = jobs;
    }

    public int getNumberOfSources() {
        return numberOfSources;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public Observable<JobOffer> getJobs() {
        return jobs;
    }
}
