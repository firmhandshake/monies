package com.piotrglazar.wellpaidwork.api;

import com.piotrglazar.wellpaidwork.model.JobResults;
import com.piotrglazar.wellpaidwork.service.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
public class JobsEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Engine engine;

    @Autowired
    public JobsEndpoint(Engine engine) {
        this.engine = engine;
    }

    @RequestMapping("/jobs")
    public JobResults fetchJobs() {
        logger.info("About to fetch jobs");
        return engine.fetchJobs();
    }
}
