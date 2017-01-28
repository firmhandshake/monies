package com.piotrglazar.wellpaidwork.service;

import com.google.common.collect.ImmutableMap;
import com.piotrglazar.wellpaidwork.model.Currency;
import com.piotrglazar.wellpaidwork.util.SalaryCurrencyNotFoundException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@ConfigurationProperties("currencies")
@Component
public class CurrencyConfig {

    private String base;

    private Map<String, BigDecimal> conversions;

    public CurrencyConfig() {
        // spring constructor
    }

    public CurrencyConfig(String base, Map<String, BigDecimal> conversions) {
        this.base = base;
        this.conversions = conversions;
    }

    public Currency getBase() {
        return Currency.fromString(base).orElseThrow(SalaryCurrencyNotFoundException::new);
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, BigDecimal> getConversions() {
        return conversions;
    }

    public Map<Currency, BigDecimal> getCurrencyConversions() {
        ImmutableMap.Builder<Currency, BigDecimal> builder = ImmutableMap.builder();
        conversions.forEach((rawCurrency, conversion) -> {
            Currency currency = Currency.fromString(rawCurrency).orElseThrow(SalaryCurrencyNotFoundException::new);
            builder.put(currency, conversion);
        });
        return builder.build();
    }

    public void setConversions(Map<String, BigDecimal> conversions) {
        this.conversions = conversions;
    }
}
