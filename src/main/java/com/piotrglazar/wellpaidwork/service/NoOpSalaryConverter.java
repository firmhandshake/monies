package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.model.Period;
import com.piotrglazar.wellpaidwork.model.Salary;

import java.util.EnumSet;
import java.util.Set;

public class NoOpSalaryConverter implements SalaryConverter {

    @Override
    public Set<Period> supports() {
        return EnumSet.allOf(Period.class);
    }

    @Override
    public Salary convert(Salary salary) {
        return salary;
    }
}
