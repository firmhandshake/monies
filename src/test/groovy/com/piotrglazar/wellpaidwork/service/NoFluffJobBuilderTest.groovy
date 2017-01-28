package com.piotrglazar.wellpaidwork.service

import com.google.common.collect.ImmutableMap
import com.piotrglazar.wellpaidwork.TestCreators
import com.piotrglazar.wellpaidwork.TestDateTimeProvider
import com.piotrglazar.wellpaidwork.api.*
import com.piotrglazar.wellpaidwork.model.*
import com.piotrglazar.wellpaidwork.model.db.JobOfferSource
import com.piotrglazar.wellpaidwork.util.CategoryNotFoundException
import com.piotrglazar.wellpaidwork.util.EmploymentTypeNotFoundException
import com.piotrglazar.wellpaidwork.util.SalaryCurrencyNotFoundException
import com.piotrglazar.wellpaidwork.util.SalaryPeriodNotFoundException
import org.joda.time.DateTime
import spock.lang.Specification

class NoFluffJobBuilderTest extends Specification implements TestCreators {

    def testDateTimeProvider = new TestDateTimeProvider().withDate("2017-01-01")

    def jobPostedDate = DateTime.parse("2017-01-01")

    def builder = new NoFluffJobBuilder(dummyTitleTags(), dummyTechnologyTags(), testDateTimeProvider,
            new SalaryConversionService([new DailyToMonthlySalaryConverter()],
                    new CurrencyConversionService(new CurrencyConfig("PLN", ImmutableMap.of("EUR", new BigDecimal(4))))))

    def "should build job from no fluff jobs data"() {
        given:
        def job = new NoFluffJob("id1", "name", "city", "backend", "title", "developer", 100, 2, 1)
        def details = new NoFluffJobDetails("id1", jobPostedDate, new NoFluffJobEssentials("permanent", "eur", "day", 10, 100, 0),
                new NoFluffJobTitle("backend", "title", "developer"), Collections.singletonList(new NoFluffTechnology(0, "t")), Collections.emptyList(),
                Collections.emptyList())

        when:
        def offer = builder.toJobOffer(job, details)

        then:
        offer.isSuccess()
        with(offer.get()) {
            externalId == "id1"
            name == "name"
            city == "city"
            category == Category.BACKEND
            position == Position.DEVELOPER
            title == "title"
            titleTags == ["titleTag"].toSet()
            salary == new Salary(832, 8332, Period.MONTH, Currency.PLN)
            employmentType == EmploymentType.PERMANENT
            posted == jobPostedDate
            remotePossible
            technologyTags == ["technologyTag"].toSet()
            source == JobOfferSource.NO_FLUFF_JOBS
            createdAt == DateTime.parse("2017-01-01")
            originalSalary == Optional.of(new Salary(10, 100, Period.DAY, Currency.EUR))
            id == Optional.empty()
        }
    }

    def "should have empty original salary if salary doesn't need conversion"() {
        given:
        def job = noFluffJob("id1")
        def details = noFluffJobDetails("id1", new NoFluffJobEssentials("permanent", "pln", "month", 10, 100, 0))

        when:
        def offer = builder.toJobOffer(job, details)

        then:
        offer.isSuccess()
        with(offer.get()) {
            salary == new Salary(10, 100, Period.MONTH, Currency.PLN)
            originalSalary == Optional.empty()
        }
    }

    def "should return failed try when employment type is unknown"() {
        given:
        def job = noFluffJob("id1")
        def details = noFluffJobDetails("id1", new NoFluffJobEssentials("unknown", "pln", "month", 10, 100, 0))

        when:
        def offer = builder.toJobOffer(job, details)

        then:
        offer.isFailure()
        offer.getException() instanceof EmploymentTypeNotFoundException
    }

    def "should return failed try when salary period is unknown"() {
        given:
        def job = noFluffJob("id1")
        def details = noFluffJobDetails("id1", new NoFluffJobEssentials("permanent", "pln", "unknown", 10, 100, 0))

        when:
        def offer = builder.toJobOffer(job, details)

        then:
        offer.isFailure()
        offer.getException() instanceof SalaryPeriodNotFoundException
    }

    def "should return failed try when salary currency is unknown"() {
        given:
        def job = noFluffJob("id1")
        def details = noFluffJobDetails("id1", new NoFluffJobEssentials("permanent", "unknown", "month", 10, 100, 0))

        when:
        def offer = builder.toJobOffer(job, details)

        then:
        offer.isFailure()
        offer.getException() instanceof SalaryCurrencyNotFoundException
    }

    def "should return failed try when category is unknown"() {
        given:
        def job = noFluffJob("id1", "unknown", "city")
        def details = noFluffJobDetails("id1", "unknown")

        when:
        def offer = builder.toJobOffer(job, details)

        then:
        offer.isFailure()
        offer.getException() instanceof CategoryNotFoundException
    }

    private static dummyTitleTags() {
        new TitleTags([].toSet(), [:]) {
            @Override
            Set<String> tags(String rawTitle) {
                return ["titleTag"].toSet()
            }
        }
    }

    private static dummyTechnologyTags() {
        new TechnologyTags([].toSet(), [:]) {
            @Override
            Set<String> tags(String rawTechnology) {
                return ["technologyTag"].toSet()
            }
        }
    }
}
