package com.piotrglazar.wellpaidwork.api;

import com.piotrglazar.wellpaidwork.service.NoFluffJobsUriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class NoFluffJobsClient {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestTemplate restTemplate;
    private final NoFluffJobsUriBuilder uriBuilder;

    @Autowired
    public NoFluffJobsClient(RestTemplate restTemplate, NoFluffJobsUriBuilder uriBuilder) {
        this.restTemplate = restTemplate;
        this.uriBuilder = uriBuilder;
    }

    public Optional<NoFluffJobPostings> getJobPostings() {
        URI uri = uriBuilder.buildUri();
        ResponseEntity<NoFluffJobPostings> responseEntity = restTemplate.getForEntity(uri, NoFluffJobPostings.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            logger.info("Successful fetch from no fluff jobs page from {}", uri);
            return Optional.of(responseEntity.getBody());
        } else {
            logger.error("Failed to fetch from no fluff jobs page from {}. Status code: {}", uri, responseEntity.getStatusCodeValue());
            return Optional.empty();
        }
    }

    public Optional<NoFluffJobDetails> getJobDetails(NoFluffJob job) {
        URI uri = uriBuilder.buildJobUri(job);
        ResponseEntity<NoFluffJobDetails> responseEntity = restTemplate.getForEntity(uri, NoFluffJobDetails.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            logger.info("Successful fetch no fluff job details for id {}", job.getId());
            return Optional.of(responseEntity.getBody());
        } else {
            logger.error("Failed to fetch no fluff job details for {}. Status code: {}", job.getId(), responseEntity.getStatusCodeValue());
            return Optional.empty();
        }
    }

    public CompletableFuture<Optional<NoFluffJobDetails>> getJobDetailsAsync(NoFluffJob job) {
        return CompletableFuture.supplyAsync(() -> getJobDetails(job));
    }
}
