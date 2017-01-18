package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.model.Currency
import com.piotrglazar.wellpaidwork.model.Period
import com.piotrglazar.wellpaidwork.model.Salary
import spock.lang.Specification

class DailyToMonthlySalaryConverterTest extends Specification {

    def converter = new DailyToMonthlySalaryConverter()

    def "should not convert already monthly salary"() {
        given:
        def salary = new Salary(1000, Period.MONTH, Currency.PLN)

        when:
        def convertedSalary = converter.convert(salary)

        then:
        convertedSalary == salary
    }

    def "should not convert yearly salary"() {
        given:
        def salary = new Salary(1000, Period.YEAR, Currency.PLN)

        when:
        def convertedSalary = converter.convert(salary)

        then:
        convertedSalary == salary
    }

    def "should convert daily salary to monthly salary"() {
        given:
        def salary = new Salary(1000, 2000, Period.DAY, Currency.PLN)

        when:
        def convertedSalary = converter.convert(salary)

        then:
        with(convertedSalary) {
            lowerBound == 20833
            upperBound == 41667
            period == Period.MONTH
        }
    }
}
