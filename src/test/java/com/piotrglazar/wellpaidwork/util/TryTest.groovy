package com.piotrglazar.wellpaidwork.util

import spock.lang.Specification

class TryTest extends Specification {

    def "should compute successful value"() {
        given:
        def value = "abc"

        when:
        def result = Try.of({value})

        then:
        result.isSuccess()
        result.get() == "abc"
    }

    def "should compute failed value"() {
        given:
        def exception = new IllegalStateException("expected test exception")

        when:
        def result = Try.of({throw exception})

        then:
        result.isFailure()
        result.getException() == exception
    }

    def "should create successful value"() {
        given:
        def value = "abc"

        when:
        def result = Try.success(value)

        then:
        result.isSuccess()
        result.get() == "abc"
    }

    def "should create failed value"() {
        given:
        def exception = new IllegalStateException("expected test exception")

        when:
        def result = Try.failed(exception)

        then:
        result.isFailure()
        result.getException() == exception
    }

    def "should map successful value"() {
        given:
        def tryValue = Try.success("abc")

        when:
        def mapped = tryValue.map({s -> s + "123"})

        then:
        mapped.isSuccess()
        mapped.get() == "abc123"
    }

    def "should not map failed value"() {
        given:
        def tryValue = Try.failed(new IllegalStateException("expected test exception"))

        when:
        def mapped = tryValue.map({s -> "mapped"})

        then:
        tryValue == mapped
        mapped.isFailure()
    }

    def "should flat map successful value"() {
        given:
        def tryValue = Try.success("abc")

        when:
        def mapped = tryValue.flatMap({s -> Try.of({s + "123"})})

        then:
        mapped.isSuccess()
        mapped.get() == "abc123"
    }

    def "should not flat map failed value"() {
        given:
        def tryValue = Try.failed(new IllegalStateException("expected test exception"))

        when:
        def mapped = tryValue.flatMap({s -> "mapped"})

        then:
        mapped == tryValue
        mapped.isFailure()
    }

    def "should use default value for failed try"() {
        given:
        def tryValue = Try.failed(new IllegalStateException("expected test exception"))

        when:
        def result = tryValue.getOrElse("abc")

        then:
        result == "abc"
    }

    def "should fail when trying to get result from failed try"() {
        given:
        def tryValue = Try.failed(new IllegalStateException("expected test exception"))

        when:
        tryValue.get()

        then:
        thrown NoSuchElementException
    }

    def "should fail when trying to get exception from successful try"() {
        given:
        def tryValue = Try.success("abc")

        when:
        tryValue.getException()

        then:
        thrown NoSuchElementException
    }

    def "should not recover when try is successful"() {
        given:
        def tryValue = Try.success("abc")

        when:
        def result = tryValue.recover({"def"})

        then:
        result == "abc"
    }

    def "should recover from failed try"() {
        given:
        def tryValue = Try.failed(new RuntimeException("test exception - failed try"))

        when:
        def result = tryValue.recover({"abc"})

        then:
        result == "abc"
    }
}
