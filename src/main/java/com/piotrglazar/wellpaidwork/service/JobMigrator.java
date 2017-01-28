package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.model.Currency;
import com.piotrglazar.wellpaidwork.model.JobOffer;
import com.piotrglazar.wellpaidwork.model.Period;
import com.piotrglazar.wellpaidwork.model.Salary;
import com.piotrglazar.wellpaidwork.model.dao.JobOfferDao;
import com.piotrglazar.wellpaidwork.util.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JobMigrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final JobOfferDao dao;
    private final SalaryConversionService conversionService;

    private static final Set<Period> periodsNeedingConversion = EnumSet.of(Period.DAY, Period.YEAR);
    private static final Set<Currency> currenciesNeedingConversion = EnumSet.of(Currency.EUR, Currency.USD, Currency.GBP);

    @Autowired
    public JobMigrator(JobOfferDao dao, SalaryConversionService conversionService) {
        this.dao = dao;
        this.conversionService = conversionService;
    }

    public List<JobOffer> migrate() {
        return dao.all()
            .stream()
            .filter(this::salaryNeedsConversion)
            .map(this::updatePeriod)
            .map(this::save)
            .collect(Collectors.toList());
    }

    private boolean salaryNeedsConversion(JobOffer jobOffer) {
        Salary salary = jobOffer.getSalary();
        return periodsNeedingConversion.contains(salary.getPeriod()) ||
                currenciesNeedingConversion.contains(salary.getCurrency());
    }

    private JobOffer updatePeriod(JobOffer jobOffer) {
        Optional<Salary> updatedSalaryOpt = conversionService.convertSalary(jobOffer.getSalary());
        if (!updatedSalaryOpt.isPresent()) {
            LOGGER.error("Cannot convert salary for job {} {}", jobOffer.getExternalId(), jobOffer.getSource());
            return jobOffer;
        } else {
            Salary updatedSalary = updatedSalaryOpt.get();
            return jobOffer.updateSalary(updatedSalary, Optional.of(jobOffer.getSalary()));
        }
    }

    private JobOffer save(JobOffer jobOffer) {
        Try<Long> saveResult = dao.save(jobOffer);
        if (saveResult.isSuccess()) {
            LOGGER.info("Successfully updated job offer {} {} {}", jobOffer.getId().orElse(null),
                    jobOffer.getExternalId(), jobOffer.getSource());
        } else {
            LOGGER.error(String.format("Failed to update job offer %s %s %s", jobOffer.getId().orElse(null),
                    jobOffer.getExternalId(), jobOffer.getSource()), saveResult.getException());
        }
        return jobOffer;
    }
}
