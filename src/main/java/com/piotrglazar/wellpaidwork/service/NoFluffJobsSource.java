package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.api.NoFluffJob;
import com.piotrglazar.wellpaidwork.api.NoFluffJobPostings;
import com.piotrglazar.wellpaidwork.api.NoFluffJobsClient;
import com.piotrglazar.wellpaidwork.model.JobOffer;
import com.piotrglazar.wellpaidwork.model.JobSource;
import com.piotrglazar.wellpaidwork.util.FutureUtils;
import com.piotrglazar.wellpaidwork.util.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class NoFluffJobsSource implements JobSource {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
    public List<JobOffer> fetch() {
        Optional<NoFluffJobPostings> jobPostings = client.getJobPostings();
        if (jobPostings.isPresent()) {
            NoFluffJobPostings noFluffJobPostings = jobPostings.get();
            List<NoFluffJob> jobs = filter.filterRelevantJobs(noFluffJobPostings.getPostings());
            logger.info("Found {} jobs, {} are relevant", noFluffJobPostings.getPostings().size(), jobs.size());

            List<CompletableFuture<Optional<JobOffer>>> jobOffersFuture = jobs.stream()
                    .map(this::fetchJobOffer)
                    .collect(Collectors.toList());

            return getJobOffers(jobOffersFuture)
                    .stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private List<Optional<JobOffer>> getJobOffers(List<CompletableFuture<Optional<JobOffer>>> jobOffersFuture) {
        return Try.of(() -> FutureUtils.asList(jobOffersFuture).get())
                .recover(error -> {
                    logger.error("Waiting for async job details fetch failed", error);
                    return Collections.emptyList();
                });
    }

    private CompletableFuture<Optional<JobOffer>> fetchJobOffer(NoFluffJob job) {
        return client.getJobDetailsAsync(job).thenApply(detailsOpt -> detailsOpt.flatMap(details -> {
            logger.info("Fetched job details for {}", details.getId());
            Try<JobOffer> jobOfferTry = jobBuilder.toJobOffer(job, details);
            if (jobOfferTry.isSuccess()) {
                logger.info("Successfully build job offer for {}", details.getId());
                return Optional.of(jobOfferTry.get());
            } else {
                logger.info("Failed to build job offer for {}", details.getId());
                return Optional.empty();
            }
        }));
    }
}
