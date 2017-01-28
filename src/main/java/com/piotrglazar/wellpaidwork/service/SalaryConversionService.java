package com.piotrglazar.wellpaidwork.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.piotrglazar.wellpaidwork.model.Currency;
import com.piotrglazar.wellpaidwork.model.Period;
import com.piotrglazar.wellpaidwork.model.Salary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SalaryConversionService {

    private final Map<Period, SalaryConverter> periodConverters;
    private final SalaryConverter defaultConverter = new NoOpSalaryConverter();
    private final CurrencyConversionService currencyConversionService;

    @Autowired
    public SalaryConversionService(List<SalaryConverter> foundConverters, CurrencyConversionService currencyConversionService) {
        this.periodConverters = buildPeriodConverters(foundConverters);
        this.currencyConversionService = currencyConversionService;
    }

    public Optional<Salary> convertSalary(Salary salary) {
        SalaryConverter periodConverter = periodConverters.getOrDefault(salary.getPeriod(), defaultConverter);
        Salary salaryWithConvertedPeriod = periodConverter.convert(salary);
        Salary salaryWithConvertedPeriodAndCurrency = convertCurrency(salaryWithConvertedPeriod);
        if (salaryWithConvertedPeriodAndCurrency.equals(salary)) {
            return Optional.empty();
        } else {
            return Optional.of(salaryWithConvertedPeriodAndCurrency);
        }
    }

    private Salary convertCurrency(Salary salary) {
        Currency currency = salary.getCurrency();
        if (currencyConversionService.getBase() != currency) {
            return currencyConversionService.convertToBase(currency, Lists.newArrayList(salary.getLowerBound(), salary.getUpperBound()))
                .map(conversionResult ->
                        new Salary(
                            conversionResult.getValues().get(0),
                            conversionResult.getValues().get(1),
                            salary.getPeriod(),
                            conversionResult.getCurrency()
                ))
                .orElse(salary);
        } else {
            return salary;
        }
    }

    private Map<Period, SalaryConverter> buildPeriodConverters(List<SalaryConverter> foundConverters) {
        ImmutableMap.Builder<Period, SalaryConverter> converterBuilder = ImmutableMap.builder();
        foundConverters.forEach(s -> s.supports().forEach(p -> converterBuilder.put(p, s)));
        return converterBuilder.build();
    }
}
