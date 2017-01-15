package com.piotrglazar.wellpaidwork.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.piotrglazar.wellpaidwork.model.db.JobOfferSource;
import org.joda.time.DateTime;

import java.util.Set;

public class JobOffer {

    private final String externalId;
    private final String name;
    private final String city;
    private final Category category;
    private final String title;
    private final Set<String> titleTags;
    private final Position position;
    private final Salary salary;
    private final EmploymentType employmentType;
    private final DateTime posted;
    private final boolean remotePossible;
    private final Set<String> technologyTags;
    private final JobOfferSource source;

    public JobOffer(String externalId, String name, String city, Category category, String title, Set<String> titleTags,
                    Position position, Salary salary, EmploymentType employmentType, DateTime posted, boolean remotePossible,
                    Set<String> technologyTags, JobOfferSource source) {
        this.externalId = externalId;
        this.name = name;
        this.city = city;
        this.category = category;
        this.title = title;
        this.titleTags = ImmutableSet.copyOf(titleTags);
        this.position = position;
        this.salary = salary;
        this.employmentType = employmentType;
        this.posted = posted;
        this.remotePossible = remotePossible;
        this.technologyTags = technologyTags;
        this.source = source;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public Category getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getTitleTags() {
        return titleTags;
    }

    public Salary getSalary() {
        return salary;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public DateTime getPosted() {
        return posted;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isRemotePossible() {
        return remotePossible;
    }

    public Set<String> getTechnologyTags() {
        return technologyTags;
    }

    public JobOfferSource getSource() {
        return source;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof JobOffer)) return false;
        JobOffer jobOffer = (JobOffer) o;
        return remotePossible == jobOffer.remotePossible &&
                Objects.equal(externalId, jobOffer.externalId) &&
                Objects.equal(name, jobOffer.name) &&
                Objects.equal(city, jobOffer.city) &&
                category == jobOffer.category &&
                Objects.equal(title, jobOffer.title) &&
                Objects.equal(titleTags, jobOffer.titleTags) &&
                position == jobOffer.position &&
                Objects.equal(salary, jobOffer.salary) &&
                employmentType == jobOffer.employmentType &&
                Objects.equal(posted, jobOffer.posted) &&
                Objects.equal(technologyTags, jobOffer.technologyTags) &&
                source == jobOffer.source;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(externalId, name, city, category, title, titleTags, position, salary, employmentType,
                posted, remotePossible, technologyTags, source);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("externalId", externalId)
                .add("name", name)
                .add("city", city)
                .add("category", category)
                .add("title", title)
                .add("titleTags", titleTags)
                .add("position", position)
                .add("salary", salary)
                .add("employmentType", employmentType)
                .add("posted", posted)
                .add("remotePossible", remotePossible)
                .add("technologyTags", technologyTags)
                .add("source", source)
                .toString();
    }
}
