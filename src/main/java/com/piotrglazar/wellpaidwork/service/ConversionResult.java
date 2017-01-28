package com.piotrglazar.wellpaidwork.service;

import com.google.common.collect.ImmutableList;
import com.piotrglazar.wellpaidwork.model.Currency;

import javax.annotation.concurrent.Immutable;
import java.util.List;

@Immutable
public final class ConversionResult {

    private final Currency currency;
    private final List<Integer> values;

    public ConversionResult(Currency currency, List<Integer> values) {
        this.currency = currency;
        this.values = ImmutableList.copyOf(values);
    }

    public Currency getCurrency() {
        return currency;
    }

    public List<Integer> getValues() {
        return values;
    }
}
