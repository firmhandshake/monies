package com.piotrglazar.wellpaidwork.util

import spock.lang.Specification

class StringUtilsTest extends Specification {

    def "should split by comma and trim tokens"() {
        given:
        def value = "james, bond, super, spy,"

        when:
        def result = StringUtils.splitAndTrim(value)

        then:
        result == ["james", "bond", "super", "spy"].toSet()
    }
}
