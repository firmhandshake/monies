package com.piotrglazar.wellpaidwork.model

import spock.lang.Specification

class CityTagsTest extends Specification {

    def cityTags = new CityTags(["Warsaw"].toSet(), ["warszawa": "Warsaw"])

    def "should use synonyms"() {
        given:
        def city = "Warszawa"

        when:
        def tag = cityTags.cityTag(city)

        then:
        tag == "Warsaw"
    }

    def "should find city by exact match"() {
        given:
        def city = "warsaw"

        when:
        def tag = cityTags.cityTag(city)

        then:
        tag == "Warsaw"
    }

    def "should return unknown city as-is"() {
        given:
        def city = "unknown"

        when:
        def tag = cityTags.cityTag(city)

        then:
        tag == "unknown"
    }
}
