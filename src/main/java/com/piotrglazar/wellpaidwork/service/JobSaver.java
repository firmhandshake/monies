package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.model.JobResults;
import com.piotrglazar.wellpaidwork.model.dao.JobOfferDao;
import com.piotrglazar.wellpaidwork.util.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobSaver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final JobOfferDao jobOfferDao;

    @Autowired
    public JobSaver(JobOfferDao jobOfferDao) {
        this.jobOfferDao = jobOfferDao;
    }

    @EventListener
    public void processJobs(JobResults jobResults) {
        LOGGER.info("Got job results event!");

        List<Try<Long>> savedJobs = jobResults.getJobs()
                .stream()
                .map(jobOfferDao::save)
                .collect(Collectors.toList());
        long successfullySaved = savedJobs.stream().filter(Try::isSuccess).count();
        long duplicated = savedJobs.stream().filter(Try::isFailure).map(Try::getException).filter(e -> e instanceof DataIntegrityViolationException).count();
        long failed = savedJobs.stream().filter(Try::isFailure).map(Try::getException).filter(e -> !(e instanceof DataIntegrityViolationException)).count();

        LOGGER.info("{} jobs successfully saved, {} duplicated and {} failed", successfullySaved, duplicated, failed);
    }
}
