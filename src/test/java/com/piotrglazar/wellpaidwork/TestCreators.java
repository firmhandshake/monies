package com.piotrglazar.wellpaidwork;

import com.piotrglazar.wellpaidwork.api.NoFluffJob;
import com.piotrglazar.wellpaidwork.api.NoFluffJobDetails;
import com.piotrglazar.wellpaidwork.api.NoFluffJobEssentials;
import com.piotrglazar.wellpaidwork.api.NoFluffJobPostings;
import com.piotrglazar.wellpaidwork.api.NoFluffJobTitle;
import com.piotrglazar.wellpaidwork.model.*;
import com.piotrglazar.wellpaidwork.model.db.JobOfferEntity;
import com.piotrglazar.wellpaidwork.model.db.JobOfferSource;
import com.piotrglazar.wellpaidwork.model.db.SalaryEntity;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public interface TestCreators {

    default NoFluffJobDetails noFluffJobDetails(String id, NoFluffJobEssentials essentials) {
        return new NoFluffJobDetails(id, DateTime.parse("2017-01-01"),
                essentials,
                new NoFluffJobTitle("backend", "title", "developer"), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList());
    }

    default NoFluffJobDetails noFluffJobDetails(String id, String category) {
        return new NoFluffJobDetails(id, DateTime.parse("2017-01-01"),
                new NoFluffJobEssentials("permanent", "pln", "month", 100, 1000, 0),
                new NoFluffJobTitle(category, "title", "developer"), Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList());
    }

    default NoFluffJob noFluffJob(String id) {
        return noFluffJob(id, "category", "city");
    }

    default NoFluffJob noFluffJob(String id, String category, String city) {
        return new NoFluffJob(id, "name", city, category, "", "", 0, 1, 1);
    }

    default NoFluffJobPostings jobPostings(NoFluffJob... noFluffJobs) {
        return new NoFluffJobPostings(Arrays.asList(noFluffJobs));
    }

    default JobOffer jobOffer(String externalId) {
        return jobOffer(externalId, new Salary(10, 100, Period.MONTH, Currency.PLN));
    }

    default JobOffer jobOffer(String externalId, Salary salary) {
        return new JobOffer(externalId, "name", "city", Category.BACKEND, "title", Collections.emptySet(),
                Position.DEVELOPER, salary,
                EmploymentType.PERMANENT, DateTime.parse("2017-01-01"), false, Collections.emptySet(),
                JobOfferSource.TEST, DateTime.parse("2017-01-01"), Optional.empty());
    }

    default JobResults jobResults(JobOffer... jobOffers) {
        return new JobResults(1, Collections.singletonList("test source"), Arrays.asList(jobOffers));
    }

    default JobOfferEntity jobOfferEntity(String externalId) {
        return new JobOfferEntity(externalId, "name", "city", Category.BACKEND,
                "title", "backend", Position.DEVELOPER, new SalaryEntity(14000, 18000, Period.MONTH, Currency.PLN),
                EmploymentType.PERMANENT, "2017-01-01", false, "scala;java", JobOfferSource.TEST, "2017-01-01",
                Optional.empty(), Optional.empty());
    }
}
