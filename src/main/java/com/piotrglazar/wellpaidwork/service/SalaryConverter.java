package com.piotrglazar.wellpaidwork.service;

import com.piotrglazar.wellpaidwork.model.Period;
import com.piotrglazar.wellpaidwork.model.Salary;

import java.util.Set;

public interface SalaryConverter {

    Set<Period> supports();

    Salary convert(Salary salary);
}
