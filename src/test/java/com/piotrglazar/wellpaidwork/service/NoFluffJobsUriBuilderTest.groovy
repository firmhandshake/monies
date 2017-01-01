package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.TestCreators
import spock.lang.Specification

class NoFluffJobsUriBuilderTest extends Specification implements TestCreators {

    def builder = new NoFluffJobsUriBuilder(config())

    def "should build job postings uri"() {
        when:
        def uri = builder.buildUri()

        then:
        uri == URI.create("http://localhost:8080/jobOffers")
    }

    def "should build job details uri"() {
        given:
        def job = noFluffJob("id1", "", "")

        when:
        def uri = builder.buildJobUri(job)

        then:
        uri == URI.create("http://localhost:8080/jobOffers/id1")
    }

    static config() {
        new NoFluffJobsConfig() {
            @Override
            String host() {
                return "localhost"
            }

            @Override
            int port() {
                return 8080
            }

            @Override
            String path() {
                return "jobOffers"
            }

            @Override
            String scheme() {
                return "http"
            }
        }
    }
}
