package com.piotrglazar.wellpaidwork.service;

import com.google.common.base.Joiner;
import com.piotrglazar.wellpaidwork.model.CityTags;
import com.piotrglazar.wellpaidwork.util.InvalidConfigurationException;
import com.piotrglazar.wellpaidwork.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BusinessConfigValidator {

    private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final CityTags cityTags;
    private final Set<String> cities;

    @Autowired
    public BusinessConfigValidator(CityTags cityTags,
                                   @Value("${sources.nofluffjobs.cities}") String cities) {
        this.cityTags = cityTags;
        this.cities = StringUtils.splitAndTrim(cities);
    }

    @PostConstruct
    public void validateConfig() {
        List<String> unknownCities = cities.stream()
                .filter(city -> !cityTags.cityTagOpt(city).isPresent())
                .collect(Collectors.toList());
        if (!unknownCities.isEmpty()) {
            LOGGER.error("Unknown cities that are not present in city tag file: "
                    .concat(Joiner.on(", ").join(unknownCities)));
            throw new InvalidConfigurationException(unknownCities);
        } else {
            LOGGER.info("City config is OK");
        }
    }


}
