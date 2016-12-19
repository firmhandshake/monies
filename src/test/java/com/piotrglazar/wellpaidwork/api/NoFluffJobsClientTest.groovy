package com.piotrglazar.wellpaidwork.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class NoFluffJobsClientTest extends Specification {

    def uri = URI.create("http://localhost/jobs")

    def "should fetch jobs when server successfully returns response"() {
        given:
        def restTemplate = Mock(RestTemplate)
        def client = new NoFluffJobsClient(restTemplate)

        when:
        def response = client.getJobPostings(uri)

        then:
        1 * restTemplate.getForEntity(uri, NoFluffJobPostings) >> okResponseWithEntity()
        assert response.isPresent()
    }

    def "should return empty optional when something goes wrong"() {
        given:
        def restTemplate = Mock(RestTemplate)
        def client = new NoFluffJobsClient(restTemplate)

        when:
        def response = client.getJobPostings(uri)

        then:
        1 * restTemplate.getForEntity(uri, NoFluffJobPostings) >> errorResponse()
        assert !response.isPresent()
    }

    private static okResponseWithEntity() {
        new ResponseEntity<>(new NoFluffJobPostings([new NoFluffJob("id", "name", "city", "category", "title", "level")]), HttpStatus.OK)
    }

    private static errorResponse() {
        new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
