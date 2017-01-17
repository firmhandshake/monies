package com.piotrglazar.wellpaidwork.api

import spock.lang.Specification

class NoFluffJobEssentialsTest extends Specification {

    def "should build when salaryFrom and salaryTo are provided"() {
        given:
        def essentials = new NoFluffJobEssentials("permanent", "pln", "month", 12000, 16000, 0)

        expect:
        with(essentials) {
            salaryFrom == 12000
            salaryTo == 16000
        }
    }

    def "should build when only salary is provided"() {
        given:
        def essentials = new NoFluffJobEssentials("permanent", "pln", "month", 0, 0, 15000)

        expect:
        with(essentials) {
            salaryFrom == 15000
            salaryTo == 15000
        }
    }
}
