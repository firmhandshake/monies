package com.piotrglazar.wellpaidwork.model;

import com.google.common.collect.ImmutableSet;
import org.joda.time.DateTime;

import java.util.Set;

public class JobOffer {

    private final String id;
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

    public JobOffer(String id, String name, String city, Category category, String title, Set<String> titleTags,
                    Position position, Salary salary, EmploymentType employmentType, DateTime posted, boolean remotePossible, Set<String> technologyTags) {
        this.id = id;
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
    }

    public String getId() {
        return id;
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
}
