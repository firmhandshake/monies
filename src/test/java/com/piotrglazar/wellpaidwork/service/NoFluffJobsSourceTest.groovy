package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.TestCreators
import com.piotrglazar.wellpaidwork.api.NoFluffJobsClient
import spock.lang.Specification

class NoFluffJobsSourceTest extends Specification implements TestCreators {

    def "should fetch jobs from no fluff page"() {
        given:
        def client = Mock(NoFluffJobsClient)
        def source = new NoFluffJobsSource(config(), client, filter())

        when:
        def found = source.fetch()

        then:
        1 * client.getJobPostings(URI.create("http://localhost:8888/jobs")) >> Optional.of(
                jobPostings(noFluffJob("backend", "warsaw"), noFluffJob("hr", "warsaw"))
        )
        assert found.size() == 1
        assert found.category == ["backend"]
        assert found.city == ["warsaw"]
    }

    def "should return empty list when client failed to fetch jobs"() {
        given:
        def client = Mock(NoFluffJobsClient)
        def source = new NoFluffJobsSource(config(), client, filter())

        when:
        def found = source.fetch()

        then:
        assert found.size() == 0
        1 * client.getJobPostings(URI.create("http://localhost:8888/jobs")) >> Optional.empty()
    }

    private static config() {
        new NoFluffJobsConfig() {
            @Override
            String scheme() {
                "http"
            }

            @Override
            String host() {
                "localhost"
            }

            @Override
            int port() {
                8888
            }

            @Override
            String path() {
                "jobs"
            }
        }
    }

    private static filter() {
        new NoFluffJobsFilter("backend", "warsaw")
    }
}
