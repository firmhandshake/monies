package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.api.NoFluffJob;
import com.piotrglazar.wellpaidwork.api.NoFluffJobPostings;
import com.piotrglazar.wellpaidwork.api.NoFluffJobsClient;
import com.piotrglazar.wellpaidwork.model.JobSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class NoFluffJobsSource implements JobSource {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final NoFluffJobsConfig config;
    private final NoFluffJobsClient client;
    private final NoFluffJobsFilter filter;

    @Autowired
    public NoFluffJobsSource(NoFluffJobsConfig config, NoFluffJobsClient client, NoFluffJobsFilter filter) {
        this.config = config;
        this.client = client;
        this.filter = filter;
    }

    @Override
    public String description() {
        return "NoFluffJobs - polish job board";
    }

    @Override
    public List<NoFluffJob> fetch() {
        URI uri = buildUri();
        Optional<NoFluffJobPostings> jobPostings = client.getJobPostings(uri);
        if (jobPostings.isPresent()) {
            NoFluffJobPostings noFluffJobPostings = jobPostings.get();
            List<NoFluffJob> jobs = filter.filterRelevantJobs(noFluffJobPostings.getPostings());
            logger.info("Found {} jobs, {} are relevant", noFluffJobPostings.getPostings().size(), jobs.size());
            return jobs;
        } else {
            logger.info("No jobs found at {}", uri);
            return Collections.emptyList();
        }
    }

    private URI buildUri() {
        return UriComponentsBuilder.newInstance()
                    .port(config.port())
                    .host(config.host())
                    .path(config.path())
                    .scheme(config.scheme())
                    .build().toUri();
    }
}
