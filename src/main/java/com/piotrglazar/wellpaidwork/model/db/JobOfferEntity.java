package com.piotrglazar.wellpaidwork.model.db;

import com.piotrglazar.wellpaidwork.model.Category;
import com.piotrglazar.wellpaidwork.model.EmploymentType;
import com.piotrglazar.wellpaidwork.model.Position;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(name = "IDX_EID_SOURCE", columnList = "external_id,source", unique = true))
public class JobOfferEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String titleTags;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

    @Embedded
    @Column(nullable = false)
    private SalaryEntity salary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employmentType;

    // we have to wait until a bug is fixed
    @Column(nullable = false)
    private String posted;

    @Column(nullable = false)
    private Boolean remotePossible;

    @Column(nullable = false)
    private String technologyTags;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobOfferSource source;

    @Column(nullable = false)
    private String createdAt;

    protected JobOfferEntity() {
        // persistence constructor
    }

    public JobOfferEntity(String externalId, String name, String city, Category category, String title,
                          String titleTags, Position position, SalaryEntity salary, EmploymentType employmentType, String posted, Boolean remotePossible,
                          String technologyTags, JobOfferSource source, String createdAt) {
        this.externalId = externalId;
        this.name = name;
        this.city = city;
        this.category = category;
        this.title = title;
        this.titleTags = titleTags;
        this.position = position;
        this.salary = salary;
        this.employmentType = employmentType;
        this.posted = posted;
        this.remotePossible = remotePossible;
        this.technologyTags = technologyTags;
        this.source = source;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleTags() {
        return titleTags;
    }

    public void setTitleTags(String titleTags) {
        this.titleTags = titleTags;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public SalaryEntity getSalary() {
        return salary;
    }

    public void setSalary(SalaryEntity salary) {
        this.salary = salary;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public Boolean getRemotePossible() {
        return remotePossible;
    }

    public void setRemotePossible(Boolean remotePossible) {
        this.remotePossible = remotePossible;
    }

    public String getTechnologyTags() {
        return technologyTags;
    }

    public void setTechnologyTags(String technologyTags) {
        this.technologyTags = technologyTags;
    }

    public JobOfferSource getSource() {
        return source;
    }

    public void setSource(JobOfferSource source) {
        this.source = source;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
