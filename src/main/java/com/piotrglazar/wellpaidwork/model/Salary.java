package com.piotrglazar.wellpaidwork.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Salary {

    private final int lowerBound;
    private final int upperBound;
    private final Period period;
    private final Currency currency;

    public Salary(int lowerBound, int upperBound, Period period, Currency currency) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.period = period;
        this.currency = currency;
    }

    public Salary(int salary, Period period, Currency currency) {
        this(salary, salary, period, currency);
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public Period getPeriod() {
        return period;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("lowerBound", lowerBound)
                .add("upperBound", upperBound)
                .add("period", period)
                .add("currency", currency)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Salary salary = (Salary) o;
        return lowerBound == salary.lowerBound &&
                upperBound == salary.upperBound &&
                period == salary.period &&
                currency == salary.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lowerBound, upperBound, period, currency);
    }
}
