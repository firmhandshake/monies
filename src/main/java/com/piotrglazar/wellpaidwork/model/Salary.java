package com.piotrglazar.wellpaidwork.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Salary {

    private final int lowerBound;
    private final int upperBound;
    private final Period period;

    public Salary(int lowerBound, int upperBound, Period period) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.period = period;
    }

    public Salary(int salary, Period period) {
        this(salary, salary, period);
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("lowerBound", lowerBound)
                .add("upperBound", upperBound)
                .add("period", period)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salary salary = (Salary) o;
        return lowerBound == salary.lowerBound &&
                upperBound == salary.upperBound &&
                period == salary.period;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lowerBound, upperBound, period);
    }
}
