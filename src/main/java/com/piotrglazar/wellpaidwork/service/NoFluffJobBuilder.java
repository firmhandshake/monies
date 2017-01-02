package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.api.NoFluffJob;
import com.piotrglazar.wellpaidwork.api.NoFluffJobDetails;
import com.piotrglazar.wellpaidwork.model.EmploymentType;
import com.piotrglazar.wellpaidwork.model.JobOffer;
import com.piotrglazar.wellpaidwork.model.Period;
import com.piotrglazar.wellpaidwork.model.Salary;
import com.piotrglazar.wellpaidwork.model.Currency;
import com.piotrglazar.wellpaidwork.util.EmploymentTypeNotFoundException;
import com.piotrglazar.wellpaidwork.util.SalaryCurrencyNotFoundException;
import com.piotrglazar.wellpaidwork.util.SalaryPeriodNotFoundException;
import com.piotrglazar.wellpaidwork.util.Try;
import org.springframework.stereotype.Component;

@Component
public class NoFluffJobBuilder {

    public Try<JobOffer> toJobOffer(NoFluffJob job, NoFluffJobDetails details) {
        return employmentType(details).flatMap(employmentType ->
                salary(details).map(salary -> new JobOffer(
                    job.getId(),
                    job.getName(),
                    job.getCity(),
                    details.getTitle().getCategory(),
                    details.getTitle().getTitle(),
                    details.getTitle().getLevel(),
                    salary,
                    employmentType,
                    details.getPosted()
        )));
    }

    private Try<Salary> salary(NoFluffJobDetails details) {
        return Try.of(() -> {
            Period period = Period.fromString(details.getEssentials().getSalaryDuration())
                    .orElseThrow(SalaryPeriodNotFoundException::new);
            Currency currency = Currency.fromString(details.getEssentials().getSalaryCurrency())
                    .orElseThrow(SalaryCurrencyNotFoundException::new);
            return new Salary(
                    details.getEssentials().getSalaryFrom(),
                    details.getEssentials().getSalaryTo(),
                    period, currency);
        });
    }

    private Try<EmploymentType> employmentType(NoFluffJobDetails details) {
        return Try.of(() ->
            EmploymentType.fromString(details.getEssentials().getEmploymentType())
                    .<EmploymentTypeNotFoundException>orElseThrow(EmploymentTypeNotFoundException::new)
        );
    }
}
