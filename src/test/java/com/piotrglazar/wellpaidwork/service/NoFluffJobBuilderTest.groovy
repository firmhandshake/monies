package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.api.NoFluffJob
import com.piotrglazar.wellpaidwork.api.NoFluffJobDetails
import com.piotrglazar.wellpaidwork.api.NoFluffJobEssentials
import com.piotrglazar.wellpaidwork.api.NoFluffJobTitle
import com.piotrglazar.wellpaidwork.model.Currency
import com.piotrglazar.wellpaidwork.model.EmploymentType
import com.piotrglazar.wellpaidwork.model.Period
import com.piotrglazar.wellpaidwork.model.Salary
import com.piotrglazar.wellpaidwork.util.EmploymentTypeNotFoundException
import com.piotrglazar.wellpaidwork.util.SalaryCurrencyNotFoundException
import com.piotrglazar.wellpaidwork.util.SalaryPeriodNotFoundException
import org.joda.time.DateTime
import spock.lang.Specification

class NoFluffJobBuilderTest extends Specification {

    def jobPostedDate = DateTime.parse("2017-01-01")

    def builder = new NoFluffJobBuilder()

    def "should build job from no fluff jobs data"() {
        given:
        def job = new NoFluffJob("id1", "name", "city", "category", "title", "level")
        def details = new NoFluffJobDetails("id1", jobPostedDate, new NoFluffJobEssentials("permanent", "pln", "month", 10, 100),
            new NoFluffJobTitle("category", "title", "level"))

        when:
        def offer = builder.toJobOffer(job, details)

        then:
        offer.isSuccess()
        with(offer.get()) {
            id == "id1"
            name == "name"
            city == "city"
            category == "category"
            title == "title"
            level == "level"
            salary == new Salary(10, 100, Period.MONTH, Currency.PLN)
            employmentType == EmploymentType.PERMANENT
            posted == jobPostedDate
        }
    }

    def "should return failed try when employment type is unknown"() {
        given:
        def job = new NoFluffJob("id1", "name", "city", "category", "title", "level")
        def details = new NoFluffJobDetails("id1", jobPostedDate, new NoFluffJobEssentials("unknown", "pln", "month", 10, 100),
                new NoFluffJobTitle("category", "title", "level"))

        when:
        def offer = builder.toJobOffer(job, details)

        then:
        offer.isFailure()
        offer.getException() instanceof EmploymentTypeNotFoundException
    }

    def "should return failed try when salary period is unknown"() {
        given:
        def job = new NoFluffJob("id1", "name", "city", "category", "title", "level")
        def details = new NoFluffJobDetails("id1", jobPostedDate, new NoFluffJobEssentials("permanent", "pln", "unknown", 10, 100),
                new NoFluffJobTitle("category", "title", "level"))

        when:
        def offer = builder.toJobOffer(job, details)

        then:
        offer.isFailure()
        offer.getException() instanceof SalaryPeriodNotFoundException
    }

    def "should return failed try when salary currency is unknown"() {
        given:
        def job = new NoFluffJob("id1", "name", "city", "category", "title", "level")
        def details = new NoFluffJobDetails("id1", jobPostedDate, new NoFluffJobEssentials("permanent", "unknown", "month", 10, 100),
                new NoFluffJobTitle("category", "title", "level"))

        when:
        def offer = builder.toJobOffer(job, details)

        then:
        offer.isFailure()
        offer.getException() instanceof SalaryCurrencyNotFoundException
    }
}
