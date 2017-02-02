package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.TestCreators
import com.piotrglazar.wellpaidwork.model.JobOffer
import com.piotrglazar.wellpaidwork.model.JobResults
import com.piotrglazar.wellpaidwork.model.JobSource
import org.springframework.context.ApplicationEventPublisher
import rx.Observable
import spock.lang.Specification

import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger

class EngineTest extends Specification implements TestCreators {

    def eventPublisher = Mock(ApplicationEventPublisher)

    def "should fetch jobs from all sources"() {
        given:
        def engine = new Engine([firstJobSource(), secondJobSource()], eventPublisher)

        when:
        def fetchedJobs = engine.fetchJobs()

        then:
        with(fetchedJobs) {
            numberOfSources == 2
            descriptions == ["first", "second"]
            jobs.toList().toBlocking().single().size() == 2
        }
    }

    def "should publish fetched jobs"() {
        given:
        def engine = new Engine([firstJobSource(), secondJobSource()], eventPublisher)

        when:
        engine.fetchJobs()

        then:
        1 * eventPublisher.publishEvent(_ as JobResults)
    }

    def "should fetch jobs once - hot observable"() {
        given:
        def itemCounter = new AtomicInteger()
        def callCounter = new AtomicInteger()
        def engine = new Engine([countingJobSource(callCounter)], eventPublisher)

        when:
        def fetchedJobs = engine.fetchJobs()
        fetchedJobs.jobs.subscribe({itemCounter.incrementAndGet()})
        fetchedJobs.jobs.subscribe({itemCounter.incrementAndGet()})

        then:
        itemCounter.intValue() == 2
        callCounter.intValue() == 1
    }

    def countingJobSource(AtomicInteger counter) {
        new JobSource() {
            @Override
            String description() {
                return "counting"
            }

            @Override
            Observable<JobOffer> fetch() {
                return Observable.from(CompletableFuture.supplyAsync({
                    counter.incrementAndGet()
                    jobOffer("id3")
                }))
            }
        }
    }

    def firstJobSource() {
        new JobSource() {
            @Override
            String description() {
                return "first"
            }

            @Override
            Observable<JobOffer> fetch() {
                return Observable.just(jobOffer("id1"))
            }
        }
    }

    def secondJobSource() {
        new JobSource() {
            @Override
            String description() {
                return "second"
            }

            @Override
            Observable<JobOffer> fetch() {
                return Observable.just(jobOffer("id2"))
            }
        }
    }
}
