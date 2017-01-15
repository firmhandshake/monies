package com.piotrglazar.wellpaidwork.model

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class JobOfferTest extends Specification {

    def "should meet equals and hashCode contract"() {
        expect:
        EqualsVerifier.forClass(JobOffer).verify()
    }
}
