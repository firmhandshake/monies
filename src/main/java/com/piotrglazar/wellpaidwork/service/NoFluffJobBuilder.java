package com.piotrglazar.wellpaidwork.service;

import com.google.common.collect.ImmutableMap;
import com.piotrglazar.wellpaidwork.api.NoFluffJob;
import com.piotrglazar.wellpaidwork.api.NoFluffJobDetails;
import com.piotrglazar.wellpaidwork.api.NoFluffTechnology;
import com.piotrglazar.wellpaidwork.model.*;
import com.piotrglazar.wellpaidwork.model.db.JobOfferSource;
import com.piotrglazar.wellpaidwork.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class NoFluffJobBuilder {

    private static final Map<String, String> CATEGORY_MAPPING = ImmutableMap.of("projectManager", "PROJECT_MANAGER",
            "businessAnalyst", "BUSINESS_ANALYST", "businessIntelligence", "BUSINESS_ANALYST");

    private final DateTimeProvider dateTimeProvider;
    private final SalaryConversionService conversionService;
    private final TagService tagService;

    @Autowired
    public NoFluffJobBuilder(DateTimeProvider dateTimeProvider, SalaryConversionService conversionService,
                             TagService tagService) {
        this.dateTimeProvider = dateTimeProvider;
        this.conversionService = conversionService;
        this.tagService = tagService;
    }

    public Try<JobOffer> toJobOffer(NoFluffJob job, NoFluffJobDetails details) {
        return employmentType(details).flatMap(employmentType ->
                salary(details).flatMap(salary ->
                    category(details).flatMap(category ->
                        position(details).map(position -> buildJobOffer(job, details, employmentType, salary, category,
                                position))
                    )
                )
        );
    }

    private JobOffer buildJobOffer(NoFluffJob job, NoFluffJobDetails details, EmploymentType employmentType, Salary salary, Category category, Position position) {
        Optional<Salary> convertedSalary = conversionService.convertSalary(salary);
        return new JobOffer(
            job.getId(),
            job.getName(),
            tagService.cityTag(job.getCity()),
            category,
            details.getTitle().getTitle(),
            tagService.titleTags(details.getTitle().getTitle()),
            position,
            convertedSalary.orElse(salary),
            employmentType,
            details.getPosted(),
            job.isRemoteWorkPossible(),
            technologyTags(details),
            JobOfferSource.NO_FLUFF_JOBS,
            dateTimeProvider.get(),
            convertedSalary.map(convertedSalaryAlreadyUsed -> salary));
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

    private Try<Category> category(NoFluffJobDetails details) {
        return Try.of(() -> {
            String category = details.getTitle().getCategory();
            String mappedCategory = CATEGORY_MAPPING.getOrDefault(category, category);
            return Category.fromString(mappedCategory)
                .<CategoryNotFoundException>orElseThrow(CategoryNotFoundException::new);
        });
    }

    private Try<Position> position(NoFluffJobDetails details) {
        return Try.of(() -> Position.fromString(details.getTitle().getLevel())
                .<PositionNotFoundException>orElseThrow(PositionNotFoundException::new));
    }

    private Set<String> technologyTags(NoFluffJobDetails details) {
        return Stream.concat(
                Stream.concat(details.getTechnologiesUsed().stream(), details.getMusts().stream()),
                details.getNices().stream())
                .map(NoFluffTechnology::getValue)
                .map(tagService::technologyTags)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
