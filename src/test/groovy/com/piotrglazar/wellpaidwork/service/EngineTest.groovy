package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.TestCreators
import com.piotrglazar.wellpaidwork.model.JobOffer
import com.piotrglazar.wellpaidwork.model.JobResults
import com.piotrglazar.wellpaidwork.model.JobSource
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class EngineTest extends Specification implements TestCreators {

    def eventPublisher = Mock(ApplicationEventPublisher)

    def engine = new Engine([firstJobSource(), secondJobSource()], eventPublisher)

    def "should fetch jobs from all sources"() {
        when:
        def fetchedJobs = engine.fetchJobs()

        then:
        with(fetchedJobs) {
            numberOfSources == 2
            descriptions == ["first", "second"]
            jobs.size() == 2
        }
    }

    def "should publish fetched jobs"() {
        when:
        engine.fetchJobs()

        then:
        1 * eventPublisher.publishEvent(_ as JobResults)
    }

    def firstJobSource() {
        new JobSource() {
            @Override
            String description() {
                return "first"
            }

            @Override
            List<JobOffer> fetch() {
                return [jobOffer("id1")]
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
            List<JobOffer> fetch() {
                return [jobOffer("id2")]
            }
        }
    }
}