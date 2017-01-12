package com.piotrglazar.wellpaidwork;

import com.piotrglazar.wellpaidwork.api.NoFluffJob;
import com.piotrglazar.wellpaidwork.api.NoFluffJobDetails;
import com.piotrglazar.wellpaidwork.api.NoFluffJobEssentials;
import com.piotrglazar.wellpaidwork.api.NoFluffJobPostings;
import com.piotrglazar.wellpaidwork.api.NoFluffJobTitle;
import com.piotrglazar.wellpaidwork.model.*;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Collections;

public interface TestCreators {

    default NoFluffJobDetails noFluffJobDetails(String id, String category) {
        return new NoFluffJobDetails(id, DateTime.parse("2017-01-01"),
                new NoFluffJobEssentials("permanent", "pln", "month", 100, 1000),
                new NoFluffJobTitle(category, "title", "developer"), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList());
    }

    default NoFluffJob noFluffJob(String id, String category, String city) {
        return new NoFluffJob(id, "name", city, category, "", "", 0, 1, 1);
    }

    default NoFluffJobPostings jobPostings(NoFluffJob... noFluffJobs) {
        return new NoFluffJobPostings(Arrays.asList(noFluffJobs));
    }

    default JobOffer jobOffer(String id) {
        return new JobOffer(id, "name", "city", Category.BACKEND, "title", Collections.emptySet(),
                Position.DEVELOPER, new Salary(10, 100, Period.MONTH, Currency.PLN),
                EmploymentType.PERMANENT, DateTime.parse("2017-01-01"), false, Collections.emptySet());
    }
}
