package com.piotrglazar.wellpaidwork.api

import spock.lang.Specification

class NoFluffJobTest extends Specification {

    def "should find offers with remote work possible"() {
        given:
        def job = new NoFluffJob("id1", "name", "city", "category", "title", "level", remoteLevel, locations,
                nonRemoteLocations)

        expect:
        job.isRemoteWorkPossible() == result

        where:
        remoteLevel | locations | nonRemoteLocations || result
        100         | 1         | 1                  || false
        100         | 2         | 1                  || true
        50          | 2         | 0                  || false
        100         | 2         | 0                  || true

    }
}
