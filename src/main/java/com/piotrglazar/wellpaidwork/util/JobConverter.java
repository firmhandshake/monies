package com.piotrglazar.wellpaidwork.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.piotrglazar.wellpaidwork.model.JobOffer;
import com.piotrglazar.wellpaidwork.model.Salary;
import com.piotrglazar.wellpaidwork.model.db.JobOfferEntity;
import com.piotrglazar.wellpaidwork.model.db.SalaryEntity;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JobConverter {

    private static final DateTimeFormatter dateTimeFormat = ISODateTimeFormat.dateTime();

    public JobOfferEntity toDb(JobOffer jobOffer) {
        return new JobOfferEntity(jobOffer.getExternalId(),
                jobOffer.getName(),
                jobOffer.getCity(),
                jobOffer.getCategory(),
                jobOffer.getTitle(),
                serializeTags(jobOffer.getTitleTags()), jobOffer.getPosition(),
                new SalaryEntity(jobOffer.getSalary().getLowerBound(),
                        jobOffer.getSalary().getUpperBound(),
                        jobOffer.getSalary().getPeriod(),
                        jobOffer.getSalary().getCurrency()), jobOffer.getEmploymentType(),
                jobOffer.getPosted().toString(dateTimeFormat),
                jobOffer.isRemotePossible(),
                serializeTags(jobOffer.getTechnologyTags()), jobOffer.getSource()
        );
    }

    public JobOffer fromDb(JobOfferEntity jobOfferEntity) {
        return new JobOffer(
                jobOfferEntity.getExternalId(),
                jobOfferEntity.getName(),
                jobOfferEntity.getCity(),
                jobOfferEntity.getCategory(),
                jobOfferEntity.getTitle(),
                deserializeTags(jobOfferEntity.getTitleTags()),
                jobOfferEntity.getPosition(),
                fromDb(jobOfferEntity.getSalary()),
                jobOfferEntity.getEmploymentType(),
                DateTime.parse(jobOfferEntity.getPosted(), dateTimeFormat),
                jobOfferEntity.getRemotePossible(),
                deserializeTags(jobOfferEntity.getTechnologyTags()),
                jobOfferEntity.getSource()
        );
    }

    private String serializeTags(Set<String> tags) {
        return Joiner.on(";").join(Sets.newTreeSet(tags));
    }

    private Set<String> deserializeTags(String serialized) {
        return Stream.of(serialized.split(";"))
                .map(String::trim)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet());
    }

    private Salary fromDb(SalaryEntity salaryEntity) {
        return new Salary(
                salaryEntity.getLowerBound(),
                salaryEntity.getUpperBound(),
                salaryEntity.getPeriod(),
                salaryEntity.getCurrency()
        );
    }
}
