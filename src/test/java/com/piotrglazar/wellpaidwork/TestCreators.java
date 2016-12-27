package com.piotrglazar.wellpaidwork;

import com.piotrglazar.wellpaidwork.api.NoFluffJob;
import com.piotrglazar.wellpaidwork.api.NoFluffJobDetails;
import com.piotrglazar.wellpaidwork.api.NoFluffJobEssentials;
import com.piotrglazar.wellpaidwork.api.NoFluffJobPostings;
import com.piotrglazar.wellpaidwork.api.NoFluffJobTitle;
import com.piotrglazar.wellpaidwork.model.EmploymentType;
import com.piotrglazar.wellpaidwork.model.JobOffer;
import com.piotrglazar.wellpaidwork.model.Period;
import com.piotrglazar.wellpaidwork.model.Salary;

import java.util.Arrays;

public interface TestCreators {

    default NoFluffJobDetails noFluffJobDetails(String id, String category) {
        return new NoFluffJobDetails(id, 0,
                new NoFluffJobEssentials("permanent", "pln", "month", 100, 1000),
                new NoFluffJobTitle(category, "developer", "senior"));
    }

    default NoFluffJob noFluffJob(String id, String category, String city) {
        return new NoFluffJob(id, "name", city, category, "", "");
    }

    default NoFluffJobPostings jobPostings(NoFluffJob... noFluffJobs) {
        return new NoFluffJobPostings(Arrays.asList(noFluffJobs));
    }

    default JobOffer jobOffer(String id) {
        return new JobOffer(id, "name", "city", "category", "title", "level",
                new Salary(10, 100, Period.MONTH),
                EmploymentType.PERMANENT);
    }
}
