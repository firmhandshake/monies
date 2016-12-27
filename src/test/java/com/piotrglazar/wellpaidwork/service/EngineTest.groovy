package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.TestCreators
import com.piotrglazar.wellpaidwork.model.JobOffer
import com.piotrglazar.wellpaidwork.model.JobSource
import spock.lang.Specification

class EngineTest extends Specification implements TestCreators {

    def engine = new Engine([firstJobSource(), secondJobSource()])

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
