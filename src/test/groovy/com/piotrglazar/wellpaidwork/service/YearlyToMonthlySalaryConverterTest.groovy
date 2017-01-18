package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.model.Currency
import com.piotrglazar.wellpaidwork.model.Period
import com.piotrglazar.wellpaidwork.model.Salary
import spock.lang.Specification

class YearlyToMonthlySalaryConverterTest extends Specification {

    def converter = new YearlyToMonthlySalaryConverter()

    def "should not convert already monthly salary"() {
        given:
        def salary = new Salary(5000, Period.MONTH, Currency.EUR)

        when:
        def convertedSalary = converter.convert(salary)

        then:
        convertedSalary == salary
    }

    def "should not convert daily salary"() {
        given:
        def salary = new Salary(1000, Period.DAY, Currency.PLN)

        when:
        def convertedSalary = converter.convert(salary)

        then:
        convertedSalary == salary
    }

    def "should convert yearly salary to monthly salary"() {
        given:
        def salary = new Salary(45000, 65000, Period.YEAR, Currency.EUR)

        when:
        def convertedSalary = converter.convert(salary)

        then:
        with(convertedSalary) {
            lowerBound == 3750
            upperBound == 5417
        }
    }
}
