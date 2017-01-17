package com.piotrglazar.wellpaidwork.api

import com.piotrglazar.wellpaidwork.TestCreators
import com.piotrglazar.wellpaidwork.service.NoFluffJobsUriBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class NoFluffJobsClientTest extends Specification implements TestCreators {

    def uri = URI.create("http://localhost/jobs")

    def "should fetch jobs when server successfully returns response"() {
        given:
        def restTemplate = Mock(RestTemplate)
        def uriBuilder = Mock(NoFluffJobsUriBuilder)
        def client = new NoFluffJobsClient(restTemplate, uriBuilder)

        when:
        def response = client.getJobPostings()

        then:
        1 * uriBuilder.buildUri() >> uri
        1 * restTemplate.getForEntity(uri, NoFluffJobPostings) >> okJobsResponse()
        response.isPresent()
    }

    def "should return empty job optional when something goes wrong"() {
        given:
        def restTemplate = Mock(RestTemplate)
        def uriBuilder = Mock(NoFluffJobsUriBuilder)
        def client = new NoFluffJobsClient(restTemplate, uriBuilder)

        when:
        def response = client.getJobPostings()

        then:
        1 * uriBuilder.buildUri() >> uri
        1 * restTemplate.getForEntity(uri, NoFluffJobPostings) >> errorResponse()
        !response.isPresent()
    }

    def "should fetch job details"() {
        given:
        def job = noFluffJob("id1", "category", "city")
        def restTemplate = Mock(RestTemplate)
        def uriBuilder = Mock(NoFluffJobsUriBuilder)
        def client = new NoFluffJobsClient(restTemplate, uriBuilder)

        when:
        def response = client.getJobDetails(job)

        then:
        response.isPresent()
        response.get().id == "id1"
        1 * uriBuilder.buildJobUri(job) >> uri
        1 * restTemplate.getForEntity(uri, NoFluffJobDetails) >> okDetailsResponse()
    }

    def "should return empty job details optional when something goes wrong"() {
        given:
        def job = noFluffJob("id1", "category", "city")
        def restTemplate = Mock(RestTemplate)
        def uriBuilder = Mock(NoFluffJobsUriBuilder)
        def client = new NoFluffJobsClient(restTemplate, uriBuilder)

        when:
        def response = client.getJobDetails(job)

        then:
        !response.isPresent()
        1 * uriBuilder.buildJobUri(job) >> uri
        1 * restTemplate.getForEntity(uri, NoFluffJobDetails) >> errorResponse()
    }

    private okJobsResponse() {
        new ResponseEntity<>(new NoFluffJobPostings([noFluffJob("externalId", "category", "city")]), HttpStatus.OK)
    }

    private okDetailsResponse() {
        new ResponseEntity<>(noFluffJobDetails("id1", "category"), HttpStatus.OK)
    }

    private static errorResponse() {
        new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
