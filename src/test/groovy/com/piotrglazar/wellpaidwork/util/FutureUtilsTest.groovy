package com.piotrglazar.wellpaidwork.util

import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutionException

import static java.util.concurrent.CompletableFuture.completedFuture
import static java.util.concurrent.CompletableFuture.supplyAsync

class FutureUtilsTest extends Specification {

    def "should produce empty list when there is empty list of futures"() {
        given:
        def futures = []

        when:
        def result = FutureUtils.asList(futures)

        then:
        result.isDone()
        result.get().isEmpty()
    }

    def "should concat futures into one future - already completed futures"() {
        given:
        def futures = [completedFuture("abc"), completedFuture("def")]

        when:
        def result = FutureUtils.asList(futures)

        then:
        result.isDone()
        result.get() == ["abc", "def"]
    }

    def "should concat futures into one future - async suppliers"() {
        given:
        def latch = new CountDownLatch(1)
        def futures = [supplyAsync({latch.await(); "abc"}), supplyAsync({latch.await(); "def"})]

        when:
        def result = FutureUtils.asList(futures)
        latch.countDown()

        then:
        result.get() == ["abc", "def"]
    }

    def "should fail the resulting future if one of sub-futures fails"() {
        given:
        def futures = [completedFuture("abc"), supplyAsync({throw new IllegalStateException("failed future")})]

        when:
        def result = FutureUtils.asList(futures)
        result.get()

        then:
        def exception = thrown(ExecutionException)
        exception.getCause().getMessage() == "failed future"
    }
}
