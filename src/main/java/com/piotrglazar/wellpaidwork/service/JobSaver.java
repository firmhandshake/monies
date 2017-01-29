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
import rx.Observable;

import java.lang.invoke.MethodHandles;

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

        Observable<Try<Long>> savedJobs = jobResults.getJobs()
                .map(jobOfferDao::save);
        savedJobs.toList().subscribe(tries -> {
            long successfullySaved = tries.stream().filter(Try::isSuccess).count();
            long duplicated = tries.stream().filter(Try::isFailure).map(Try::getException).filter(e -> e instanceof DataIntegrityViolationException).count();
            long failed = tries.stream().filter(Try::isFailure).map(Try::getException).filter(e -> !(e instanceof DataIntegrityViolationException)).count();

            LOGGER.info("{} jobs successfully saved, {} duplicated and {} failed", successfullySaved, duplicated, failed);
        }, this::errorHandler);
    }

    private void errorHandler(Throwable t) {
        LOGGER.error("Failed to process job results", t);
    }
}
