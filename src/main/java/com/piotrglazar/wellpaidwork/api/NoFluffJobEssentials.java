package com.piotrglazar.wellpaidwork.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoFluffJobEssentials {

    private final String employmentType;
    private final String salaryCurrency;
    private final String salaryDuration;
    private final int salaryFrom;
    private final int salaryTo;

    @JsonCreator
    public NoFluffJobEssentials(@JsonProperty("employmentType") String employmentType,
                                @JsonProperty("salaryCurrency") String salaryCurrency,
                                @JsonProperty("salaryDuration") String salaryDuration,
                                @JsonProperty("salaryFrom") int salaryFrom,
                                @JsonProperty("salaryTo") int salaryTo) {
        this.employmentType = employmentType;
        this.salaryCurrency = salaryCurrency;
        this.salaryDuration = salaryDuration;
        this.salaryFrom = salaryFrom;
        this.salaryTo = salaryTo;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getSalaryCurrency() {
        return salaryCurrency;
    }

    public String getSalaryDuration() {
        return salaryDuration;
    }

    public int getSalaryFrom() {
        return salaryFrom;
    }

    public int getSalaryTo() {
        return salaryTo;
    }
}
