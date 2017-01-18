package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.model.Period;
import com.piotrglazar.wellpaidwork.model.Salary;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class DailyToMonthlySalaryConverter implements SalaryConverter {

    private static final int WORK_DAYS_IN_YEAR = 250; // in 2017
    private static final int MONTHS_IN_YEAR = 12;

    private static final float CONVERSION_RATE = (float) WORK_DAYS_IN_YEAR / MONTHS_IN_YEAR;

    @Override
    public Set<Period> supports() {
        return EnumSet.of(Period.DAY);
    }

    @Override
    public Salary convert(Salary salary) {
        if (supports().contains(salary.getPeriod())) {
            return convertToMonthly(salary);
        } else {
            return salary;
        }
    }

    private Salary convertToMonthly(Salary salary) {
        return new Salary(
                convertValue(salary.getLowerBound()),
                convertValue(salary.getUpperBound()),
                Period.MONTH,
                salary.getCurrency()
        );
    }

    private int convertValue(int value) {
        return Math.round(value * CONVERSION_RATE);
    }
}
