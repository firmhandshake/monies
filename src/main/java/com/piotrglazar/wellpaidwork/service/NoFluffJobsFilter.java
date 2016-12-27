package com.piotrglazar.wellpaidwork.service;

import com.google.common.collect.ImmutableSet;
import com.piotrglazar.wellpaidwork.api.NoFluffJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class NoFluffJobsFilter {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int LIMIT = 20;

    private final Set<String> categories;
    private final Set<String> cities;

    @Autowired
    public NoFluffJobsFilter(@Value("${sources.nofluffjobs.categories}") String categories,
                             @Value("${sources.nofluffjobs.cities}") String cities) {
        this(splitAndTrim(categories), splitAndTrim(cities));
    }

    public NoFluffJobsFilter(Set<String> categories, Set<String> cities) {
        this.categories = ImmutableSet.copyOf(categories);
        this.cities = ImmutableSet.copyOf(cities);
    }

    public List<NoFluffJob> filterRelevantJobs(List<NoFluffJob> jobs) {
        logger.info("Using limit of {} jobs", LIMIT);
        return jobs.stream()
                .filter(this::shouldProcess)
                .limit(LIMIT)
                .collect(Collectors.toList());
    }

    private static ImmutableSet<String> splitAndTrim(String raw) {
        return ImmutableSet.copyOf(Stream.of(raw.split(",")).map(String::trim).collect(Collectors.toSet()));
    }

    private boolean shouldProcess(NoFluffJob job) {
        return categories.contains(job.getCategory().toLowerCase()) && cities.contains(job.getCity().toLowerCase());
    }
}
