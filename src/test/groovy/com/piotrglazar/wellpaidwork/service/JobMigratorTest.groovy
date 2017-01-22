package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.model.Category
import com.piotrglazar.wellpaidwork.model.Currency
import com.piotrglazar.wellpaidwork.model.EmploymentType
import com.piotrglazar.wellpaidwork.model.JobOffer
import com.piotrglazar.wellpaidwork.model.Period
import com.piotrglazar.wellpaidwork.model.Position
import com.piotrglazar.wellpaidwork.model.Salary
import com.piotrglazar.wellpaidwork.model.dao.JobOfferDao
import com.piotrglazar.wellpaidwork.model.db.JobOfferSource
import com.piotrglazar.wellpaidwork.util.Try
import org.joda.time.DateTime
import spock.lang.Specification

class JobMigratorTest extends Specification {

    def dao = Mock(JobOfferDao)
    def conversionService = Mock(SalaryConversionService)
    def jobMigrator = new JobMigrator(dao, conversionService)

    def "should not fail when there are no jobs to convert"() {
        when:
        def result = jobMigrator.migrate()

        then:
        result.isEmpty()
        1 * dao.all() >> Collections.emptyList()
    }

    def "should convert job offer"() {
        given:
        def jobOffer = jobOfferToConvert()

        when:
        def result = jobMigrator.migrate()

        then:
        result.size() == 1
        with(result.get(0)) {
            salary.period == Period.MONTH
            originalSalary.get().period == Period.DAY
        }
        1 * dao.all() >> Collections.singletonList(jobOffer)
        1 * conversionService.convertSalary(_) >> Optional.of(new Salary(210, 2100, Period.MONTH, Currency.PLN))
        1 * dao.save(_) >> Try.success(3L)
    }

    def "should not fail when saving updated job offer fails"() {
        given:
        def jobOffer = jobOfferToConvert()

        when:
        def result = jobMigrator.migrate()

        then:
        result.size() == 1
        1 * dao.all() >> Collections.singletonList(jobOffer)
        1 * conversionService.convertSalary(_) >> Optional.of(new Salary(210, 2100, Period.MONTH, Currency.PLN))
        1 * dao.save(_) >> Try.failed(new RuntimeException("test exception"))
    }

    def "should not fail when salary conversion fails"() {
        given:
        def jobOffer = jobOfferToConvert()

        when:
        def result = jobMigrator.migrate()

        then:
        result.size() == 1
        1 * dao.all() >> Collections.singletonList(jobOffer)
        1 * conversionService.convertSalary(_) >> Optional.empty()
        1 * dao.save(_) >> Try.success(3L)
    }

    private static jobOfferToConvert() {
        new JobOffer(10L, "id", "name", "city", Category.BACKEND, "title", Collections.emptySet(),
                Position.DEVELOPER, new Salary(10, 100, Period.DAY, Currency.PLN),
                EmploymentType.PERMANENT, DateTime.parse("2017-01-01"), false, Collections.emptySet(),
                JobOfferSource.TEST, DateTime.parse("2017-01-01"), Optional.empty())
    }
}
