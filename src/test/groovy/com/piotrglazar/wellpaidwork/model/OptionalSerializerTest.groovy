package com.piotrglazar.wellpaidwork.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.piotrglazar.wellpaidwork.service.ApplicationConfiguration
import spock.lang.Specification

class OptionalSerializerTest extends Specification {

    def objectMapper = new ApplicationConfiguration().objectMapper()

    def "should serialize present optional as normal field"() {
        given:
        def obj = new TestObject("james", Optional.of("bond"))

        when:
        def json = objectMapper.writer().writeValueAsString(obj)

        then:
        json.contains("\"name\":\"james\"")
        json.contains("\"nick\":\"bond\"")
    }

    def "should serialize empty optional as null"() {
        given:
        def obj = new TestObject("james", Optional.empty())

        when:
        def json = objectMapper.writer().writeValueAsString(obj)

        then:
        json.contains("\"name\":\"james\"")
        json.contains("\"nick\":null")
    }

    static class TestObject {
        private String name
        private Optional<String> nick

        TestObject(String name, Optional<String> nick) {
            this.name = name
            this.nick = nick
        }

        String getName() {
            return name
        }

        @JsonSerialize(using = OptionalSerializer)
        Optional<String> getNick() {
            return nick
        }
    }
}
