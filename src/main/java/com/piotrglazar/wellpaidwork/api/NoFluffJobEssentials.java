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
                                @JsonProperty("salaryTo") int salaryTo,
                                @JsonProperty("salary") int salary) {
        this.employmentType = employmentType;
        this.salaryCurrency = salaryCurrency;
        this.salaryDuration = salaryDuration;
        this.salaryFrom = chooseSalary(salary, salaryFrom);
        this.salaryTo = chooseSalary(salary, salaryTo);
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

    private static int chooseSalary(int singleValue, int fallback) {
        return (singleValue != 0) ? singleValue : fallback;
    }
}
