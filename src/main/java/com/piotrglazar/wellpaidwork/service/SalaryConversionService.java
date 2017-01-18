package com.piotrglazar.wellpaidwork.service;

import com.google.common.collect.ImmutableMap;
import com.piotrglazar.wellpaidwork.model.Period;
import com.piotrglazar.wellpaidwork.model.Salary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SalaryConversionService {

    private final Map<Period, SalaryConverter> converters;
    private final SalaryConverter defaultConverter = new NoOpSalaryConverter();

    @Autowired
    public SalaryConversionService(List<SalaryConverter> foundConverters) {
        ImmutableMap.Builder<Period, SalaryConverter> converterBuilder = ImmutableMap.builder();

        foundConverters.forEach(s -> s.supports().forEach(p -> converterBuilder.put(p, s)));

        converters = converterBuilder.build();
    }

    public Optional<Salary> convertSalary(Salary salary) {
        SalaryConverter converter = converters.getOrDefault(salary.getPeriod(), defaultConverter);
        Salary convertedSalary = converter.convert(salary);
        if (convertedSalary.equals(salary)) {
            return Optional.empty();
        } else {
            return Optional.of(convertedSalary);
        }
    }
}
