package com.piotrglazar.wellpaidwork.service;

import com.google.common.collect.ImmutableSet;
import com.piotrglazar.wellpaidwork.api.NoFluffJob;
import com.piotrglazar.wellpaidwork.model.dao.JobOfferDao;
import com.piotrglazar.wellpaidwork.model.db.JobOfferEntity;
import com.piotrglazar.wellpaidwork.model.db.JobOfferSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.piotrglazar.wellpaidwork.util.StringUtils.splitAndTrim;

@Component
public class NoFluffJobsFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int LIMIT = 20;

    private final Set<String> categories;
    private final Set<String> cities;
    private final JobOfferDao jobOfferDao;

    @Autowired
    public NoFluffJobsFilter(@Value("${sources.nofluffjobs.categories}") String categories,
                             @Value("${sources.nofluffjobs.cities}") String cities,
                             JobOfferDao jobOfferDao) {
        this(splitAndTrim(categories), splitAndTrim(cities), jobOfferDao);
    }

    public NoFluffJobsFilter(Set<String> categories, Set<String> cities, JobOfferDao jobOfferDao) {
        this.categories = ImmutableSet.copyOf(categories);
        this.cities = ImmutableSet.copyOf(cities);
        this.jobOfferDao = jobOfferDao;
    }

    public List<NoFluffJob> filterRelevantJobs(List<NoFluffJob> jobs) {
        LOGGER.info("Using limit of {} jobs", LIMIT);
        return jobs.stream()
                .filter(this::shouldProcess)
                .limit(LIMIT)
                .collect(Collectors.toList());
    }

    private boolean shouldProcess(NoFluffJob job) {
        return (job.isRemoteWorkPossible() || hasRequiredCityAndCategory(job)) &&
                notAlreadyProcessed(job);
    }

    private boolean hasRequiredCityAndCategory(NoFluffJob job) {
        return categories.contains(job.getCategory().toLowerCase()) && cities.contains(job.getCity().toLowerCase());
    }

    private boolean notAlreadyProcessed(NoFluffJob job) {
        Optional<JobOfferEntity> jobOfferEntity = jobOfferDao.findRaw(job.getId(), JobOfferSource.NO_FLUFF_JOBS);
        LOGGER.info("Is job with external id {} found? {}", job.getId(), jobOfferEntity.isPresent());
        return !jobOfferEntity.isPresent();
    }
}
