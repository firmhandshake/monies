package com.piotrglazar.wellpaidwork;

import com.piotrglazar.wellpaidwork.service.CurrencyConfig;
import com.piotrglazar.wellpaidwork.service.CurrencyConversionService;
import com.piotrglazar.wellpaidwork.service.SalaryConversionService;

import java.util.Collections;

public class DummyConversionService extends SalaryConversionService {
    public DummyConversionService() {
        super(Collections.emptyList(), new CurrencyConversionService(new CurrencyConfig("PLN", Collections.emptyMap())));
    }
}
