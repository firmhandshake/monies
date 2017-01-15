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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class NoFluffJobBuilder {

    private final TitleTags titleTags;

    private final TechnologyTags technologyTags;

    private static final Map<String, String> CATEGORY_MAPPING = ImmutableMap.of("projectManager", "PROJECT_MANAGER",
            "businessAnalyst", "BUSINESS_ANALYST");

    @Autowired
    public NoFluffJobBuilder(TitleTags titleTags, TechnologyTags technologyTags) {
        this.titleTags = titleTags;
        this.technologyTags = technologyTags;
    }

    public Try<JobOffer> toJobOffer(NoFluffJob job, NoFluffJobDetails details) {
        return employmentType(details).flatMap(employmentType ->
                salary(details).flatMap(salary ->
                    category(details).flatMap(category ->
                        position(details).map(position -> new JobOffer(
                            job.getId(),
                            job.getName(),
                            job.getCity(),
                            category,
                            details.getTitle().getTitle(),
                            titleTags.tags(details.getTitle().getTitle()),
                            position,
                            salary,
                            employmentType,
                            details.getPosted(),
                            job.isRemoteWorkPossible(),
                            technologyTags(details),
                            JobOfferSource.NO_FLUFF_JOBS)
                        )
                    )
                )
        );
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
                .map(technologyTags::tags)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
