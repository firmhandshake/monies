package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.TestCreators
import com.piotrglazar.wellpaidwork.model.dao.JobOfferDao
import com.piotrglazar.wellpaidwork.util.Try
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Specification

class JobSaverTest extends Specification implements TestCreators {

    def dao = Mock(JobOfferDao)
    def jobSaver = new JobSaver(dao)

    def "should save job offer"() {
        given:
        def jobResults = jobResults(jobOffer("id"))

        when:
        jobSaver.processJobs(jobResults)

        then:
        1 * dao.save(_) >> Try.success(1L)
    }

    def "should not fail when saving a job fails"() {
        given:
        def jobResults = jobResults(jobOffer("id"))

        when:
        jobSaver.processJobs(jobResults)

        then:
        1 * dao.save(_) >> Try.failed(new RuntimeException("test exception"))
    }

    def "should not fail when duplicated offer is inserted"() {
        given:
        def jobResults = jobResults(jobOffer("id"))

        when:
        jobSaver.processJobs(jobResults)

        then:
        1 * dao.save(_) >> Try.failed(new DataIntegrityViolationException(constraintViolationMessage))
    }

    private constraintViolationMessage = """ could not execute statement; SQL [n/a]; constraint 
        [\\"IDX_EID_SOURCE_INDEX_E ON PUBLIC.JOB_OFFER_ENTITY(EXTERNAL_ID, SOURCE) VALUES ('x', 'y', 1)\\"; 
        SQL statement: """.stripMargin().stripIndent().replace("\n", "")
}
