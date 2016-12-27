package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.TestCreators
import spock.lang.Specification

class NoFluffJobsFilterTest extends Specification implements TestCreators {

    def "should construct filters from comma separated values"() {
        given:
        def categories = "aa, bb"
        def cities = "cc, dd"
        def filter = new NoFluffJobsFilter(categories, cities)

        when:
        def filtered = filter.filterRelevantJobs([noFluffJob("1", "aa", "dd"), noFluffJob("2", "bb", "cc"),
                                                  noFluffJob("3", "xx", "cc"), noFluffJob("4", "xx", "dd"),
                                                  noFluffJob("5", "xx", "xx")])

        then:
        filtered.category == ["aa", "bb"]
        filtered.city == ["dd", "cc"]
    }
}
