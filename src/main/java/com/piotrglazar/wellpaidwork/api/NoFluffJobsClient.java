package com.piotrglazar.wellpaidwork.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Optional;

@Component
public class NoFluffJobsClient {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestTemplate restTemplate;

    @Autowired
    public NoFluffJobsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<NoFluffJobPostings> getJobPostings(URI uri) {
        ResponseEntity<NoFluffJobPostings> responseEntity = restTemplate.getForEntity(uri, NoFluffJobPostings.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            logger.info("Successful fetch from no fluff jobs page");
            return Optional.of(responseEntity.getBody());
        } else {
            logger.error("Failed to fetch from no fluff jobs page", responseEntity.getStatusCodeValue());
            return Optional.empty();
        }
    }
}
