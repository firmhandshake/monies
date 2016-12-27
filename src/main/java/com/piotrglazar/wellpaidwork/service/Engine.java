package com.piotrglazar.wellpaidwork.service;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.piotrglazar.wellpaidwork.model.JobOffer;
import com.piotrglazar.wellpaidwork.model.JobResults;
import com.piotrglazar.wellpaidwork.model.JobSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Engine {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final List<JobSource> sources;

    @Autowired
    public Engine(List<JobSource> sources) {
        this.sources = ImmutableList.copyOf(sources);
    }

    public JobResults fetchJobs() {
        List<String> descriptions = sources.stream().map(JobSource::description).collect(Collectors.toList());
        String descriptionsMessage = Joiner.on(", ").join(descriptions);
        logger.info("About to fetch jobs from {} sources: {}", sources.size(), descriptionsMessage);

        List<JobOffer> jobs = findJobs();

        return new JobResults(sources.size(), descriptions, jobs);
    }

    private List<JobOffer> findJobs() {
        return sources.stream().flatMap(s -> s.fetch().stream()).collect(Collectors.toList());
    }
}
