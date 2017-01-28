package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.TestCreators
import com.piotrglazar.wellpaidwork.api.NoFluffJob
import com.piotrglazar.wellpaidwork.model.*
import com.piotrglazar.wellpaidwork.model.dao.JobOfferDao
import com.piotrglazar.wellpaidwork.model.db.JobOfferEntity
import com.piotrglazar.wellpaidwork.model.db.JobOfferSource
import com.piotrglazar.wellpaidwork.model.db.SalaryEntity
import spock.lang.Specification

class NoFluffJobsFilterTest extends Specification implements TestCreators {

    def "should construct filters from comma separated values"() {
        given:
        def categories = "aa, bb"
        def cities = "cc, dd"
        def dao = Mock(JobOfferDao)
        def filter = new NoFluffJobsFilter(categories, cities, dao)

        when:
        def filtered = filter.filterRelevantJobs([noFluffJob("1", "aa", "dd"), noFluffJob("2", "bb", "cc"),
                                                  noFluffJob("3", "xx", "cc"), noFluffJob("4", "xx", "dd"),
                                                  noFluffJob("5", "xx", "xx")])

        then:
        filtered.category == ["aa", "bb"]
        filtered.city == ["dd", "cc"]
        2 * dao.findRaw(_, _) >> Optional.empty()
    }

    def "should pass job offer with remote work possible"() {
        given:
        def dao = Mock(JobOfferDao)
        def filter = new NoFluffJobsFilter(Collections.emptySet(), Collections.emptySet(), dao)

        when:
        def filtered = filter.filterRelevantJobs([new NoFluffJob("1", "", "", "", "", "", 100, 2, 1)])

        then:
        filtered.id == ["1"]
        1 * dao.findRaw(_, _) >> Optional.empty()
    }

    def "should not pass a job offer when it was already processed"() {
        given:
        def dao = Mock(JobOfferDao)
        def filter = new NoFluffJobsFilter("aa", "cc", dao)

        when:
        def filtered = filter.filterRelevantJobs([noFluffJob("1", "aa", "cc")])

        then:
        filtered.empty
        1 * dao.findRaw(_, _) >> Optional.of(jobOfferEntity("externalId"))
    }
}
