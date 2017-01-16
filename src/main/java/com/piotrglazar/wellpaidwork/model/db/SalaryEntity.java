package com.piotrglazar.wellpaidwork.model.db;

import com.piotrglazar.wellpaidwork.model.Currency;
import com.piotrglazar.wellpaidwork.model.Period;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class SalaryEntity {

    @Column(nullable = false)
    private Integer lowerBound;

    @Column(nullable = false)
    private Integer upperBound;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Period period;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    protected SalaryEntity() {
        // persistence constructor
    }

    public SalaryEntity(Integer lowerBound, Integer upperBound, Period period, Currency currency) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.period = period;
        this.currency = currency;
    }

    public Integer getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Integer lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Integer getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Integer upperBound) {
        this.upperBound = upperBound;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
