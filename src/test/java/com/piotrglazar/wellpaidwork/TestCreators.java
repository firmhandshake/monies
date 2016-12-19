package com.piotrglazar.wellpaidwork;

import com.piotrglazar.wellpaidwork.api.NoFluffJob;
import com.piotrglazar.wellpaidwork.api.NoFluffJobPostings;

import java.util.Arrays;

public interface TestCreators {

    default NoFluffJob noFluffJob(String category, String city) {
        return new NoFluffJob("id", "name", city, category, "", "");
    }

    default NoFluffJobPostings jobPostings(NoFluffJob... noFluffJobs) {
        return new NoFluffJobPostings(Arrays.asList(noFluffJobs));
    }
}
