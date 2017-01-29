package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.model.CityTags
import com.piotrglazar.wellpaidwork.util.InvalidConfigurationException
import spock.lang.Specification

class BusinessConfigValidatorTest extends Specification {

    def "should validate when config is ok"() {
        given:
        def cityTags = new CityTags(["Warsaw", "Cracow"].toSet(), [:])
        def cities = "warsaw, cracow"

        when:
        new BusinessConfigValidator(cityTags, cities).validateConfig()

        then:
        noExceptionThrown()
    }

    def "should throw exception when some cities are unknown"() {
        given:
        def cityTags = new CityTags(["Warsaw"].toSet(), [:])
        def cities = "warsaw, cracow"

        when:
        new BusinessConfigValidator(cityTags, cities).validateConfig()

        then:
        thrown InvalidConfigurationException
    }
}
