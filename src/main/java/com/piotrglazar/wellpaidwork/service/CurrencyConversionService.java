package com.piotrglazar.wellpaidwork.service;

import com.google.common.collect.ImmutableMap;
import com.piotrglazar.wellpaidwork.model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CurrencyConversionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Map<Currency, BigDecimal> currencyConverters;
    private final Currency baseCurrency;

    @Autowired
    public CurrencyConversionService(CurrencyConfig config) {
        this.currencyConverters = buildCurrencyConverters(config.getBase(), config.getCurrencyConversions());
        this.baseCurrency = config.getBase();
    }

    public Optional<ConversionResult> convertToBase(Currency sourceCurrency, List<Integer> values) {
        Optional<ConversionResult> result = Optional.ofNullable(currencyConverters.get(sourceCurrency))
                .map(exchangeRate -> {
                    List<Integer> newValues = values.stream()
                            .map(BigDecimal::new)
                            .map(exchangeRate::multiply)
                            .map(v -> v.setScale(0, RoundingMode.HALF_UP))
                            .map(BigDecimal::intValue)
                            .collect(Collectors.toList());
                    return new ConversionResult(baseCurrency, newValues);
                });

        if (!result.isPresent()) {
            LOGGER.error("There is no exchange rate for currency " + sourceCurrency);
        }

        return result;
    }

    public Currency getBase() {
        return baseCurrency;
    }

    private Map<Currency, BigDecimal> buildCurrencyConverters(Currency baseCurrency,
                                                                     Map<Currency, BigDecimal> exchangeRates) {
        ImmutableMap.Builder<Currency, BigDecimal> currencyConverterBuilder = ImmutableMap.builder();
        currencyConverterBuilder.put(baseCurrency, BigDecimal.ONE);
        exchangeRates.forEach(currencyConverterBuilder::put);
        return currencyConverterBuilder.build();
    }
}
