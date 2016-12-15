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
        def filtered = filter.filterRelevantJobs([noFluffJob("aa", "dd"), noFluffJob("bb", "cc"), noFluffJob("xx", "cc"),
                                                  noFluffJob("xx", "dd"), noFluffJob("xx", "xx")])

        then:
        assert filtered.category == ["aa", "bb"]
        assert filtered.city == ["dd", "cc"]
    }
}
