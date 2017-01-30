package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.api.NoFluffJob;
import com.piotrglazar.wellpaidwork.api.NoFluffJobPostings;
import com.piotrglazar.wellpaidwork.api.NoFluffJobsClient;
import com.piotrglazar.wellpaidwork.model.JobOffer;
import com.piotrglazar.wellpaidwork.model.JobSource;
import com.piotrglazar.wellpaidwork.util.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class NoFluffJobsSource implements JobSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final NoFluffJobsClient client;
    private final NoFluffJobsFilter filter;
    private final NoFluffJobBuilder jobBuilder;

    @Autowired
    public NoFluffJobsSource(NoFluffJobsClient client, NoFluffJobsFilter filter, NoFluffJobBuilder jobBuilder) {
        this.client = client;
        this.filter = filter;
        this.jobBuilder = jobBuilder;
    }

    @Override
    public String description() {
        return "NoFluffJobs - polish job board";
    }

    @Override
    public Observable<JobOffer> fetch() {
        Optional<NoFluffJobPostings> jobPostings = client.getJobPostings();
        if (jobPostings.isPresent()) {
            NoFluffJobPostings noFluffJobPostings = jobPostings.get();
            List<NoFluffJob> jobs = filter.filterRelevantJobs(noFluffJobPostings.getPostings());
            LOGGER.info("Found {} jobs, {} are relevant", noFluffJobPostings.getPostings().size(), jobs.size());

            return Observable.from(jobs)
                .map(this::fetchJobOffer)
                .flatMap(Observable::from)
                .filter(Optional::isPresent)
                .map(Optional::get);
        } else {
            return Observable.empty();
        }
    }

    private CompletableFuture<Optional<JobOffer>> fetchJobOffer(NoFluffJob job) {
        return client.getJobDetailsAsync(job).thenApply(detailsOpt -> detailsOpt.flatMap(details -> {
            LOGGER.info("Fetched job details for {}", details.getId());
            Try<JobOffer> jobOfferTry = jobBuilder.toJobOffer(job, details);
            if (jobOfferTry.isSuccess()) {
                LOGGER.info("Successfully build job offer for {}", details.getId());
                return Optional.of(jobOfferTry.get());
            } else {
                LOGGER.error("Failed to build job offer for " + details.getId(), jobOfferTry.getException());
                return Optional.empty();
            }
        }));
    }
}
