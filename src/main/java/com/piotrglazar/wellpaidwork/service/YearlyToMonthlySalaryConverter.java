package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.model.Period;
import com.piotrglazar.wellpaidwork.model.Salary;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class YearlyToMonthlySalaryConverter implements SalaryConverter {

    private static final float MONTHS_IN_YEAR = 12;

    @Override
    public Set<Period> supports() {
        return EnumSet.of(Period.YEAR);
    }

    @Override
    public Salary convert(Salary salary) {
        if (supports().contains(salary.getPeriod())) {
            return convertSalary(salary);
        } else {
            return salary;
        }
    }

    private Salary convertSalary(Salary salary) {
        return new Salary(
                convertValue(salary.getLowerBound()),
                convertValue(salary.getUpperBound()),
                Period.MONTH,
                salary.getCurrency()
        );
    }

    private int convertValue(int value) {
        return Math.round(value / MONTHS_IN_YEAR);
    }
}
