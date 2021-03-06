package com.piotrglazar.wellpaidwork.api;

import com.piotrglazar.wellpaidwork.model.JobOffer;
import com.piotrglazar.wellpaidwork.model.JobResults;
import com.piotrglazar.wellpaidwork.service.Engine;
import com.piotrglazar.wellpaidwork.service.JobMigrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
public class JobsEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Engine engine;
    private final JobMigrator jobMigrator;

    @Autowired
    public JobsEndpoint(Engine engine, JobMigrator jobMigrator) {
        this.engine = engine;
        this.jobMigrator = jobMigrator;
    }

    @RequestMapping("/jobs")
    public DeferredResult<JobResultsResponse> fetchJobs() {
        LOGGER.info("About to fetch jobs");
        DeferredResult<JobResultsResponse> response = new DeferredResult<>();
        JobResults jobResults = engine.fetchJobs();
        jobResults.getJobs()
                .toList()
                .map(jobOffers -> new JobResultsResponse(jobResults.getNumberOfSources(), jobResults.getDescriptions(),
                    jobOffers))
                .single()
                .subscribe(response::setResult, response::setErrorResult);

        return response;
    }

    @RequestMapping("/jobs/migrate")
    public List<JobOffer> migrateJobOffers() {
        return jobMigrator.migrate();
    }
}
