package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.model.Currency
import com.piotrglazar.wellpaidwork.model.Period
import com.piotrglazar.wellpaidwork.model.Salary
import spock.lang.Specification

class SalaryConversionServiceTest extends Specification {

    def conversionService = new SalaryConversionService([new DailyToMonthlySalaryConverter()],
            new CurrencyConversionService(new CurrencyConfig("PLN", ["EUR": new BigDecimal(4)])))

    def "should convert salary period and currency"() {
        given:
        def salary = new Salary(200, Period.DAY, Currency.EUR)

        when:
        def convertedSalaryOpt = conversionService.convertSalary(salary)

        then:
        convertedSalaryOpt.isPresent()
        with(convertedSalaryOpt.get()) {
            lowerBound == 16668
            period == Period.MONTH
            currency == Currency.PLN
        }
    }

    def "should convert only salary period"() {
        given:
        def salary = new Salary(1000, Period.DAY, Currency.PLN)

        when:
        def convertedSalaryOpt = conversionService.convertSalary(salary)

        then:
        convertedSalaryOpt.isPresent()
        with(convertedSalaryOpt.get()) {
            lowerBound == 20833
            period == Period.MONTH
        }
    }

    def "should convert only salary currency"() {
        given:
        def salary = new Salary(1000, Period.MONTH, Currency.EUR)

        when:
        def convertedSalaryOpt = conversionService.convertSalary(salary)

        then:
        convertedSalaryOpt.isPresent()
        with(convertedSalaryOpt.get()) {
            lowerBound == 4000
            currency == Currency.PLN
        }
    }

    def "should return empty optional when conversion is not necessary"() {
        given:
        def salary = new Salary(1000, Period.MONTH, Currency.PLN)

        when:
        def convertedSalaryOpt = conversionService.convertSalary(salary)

        then:
        !convertedSalaryOpt.isPresent()
    }

    def "should do nothing when there are conversion rates missing"() {
        given:
        def salary = new Salary(1000, Period.MONTH, Currency.GBP)

        when:
        def convertedSalaryOpt = conversionService.convertSalary(salary)

        then:
        !convertedSalaryOpt.isPresent()
    }
}
