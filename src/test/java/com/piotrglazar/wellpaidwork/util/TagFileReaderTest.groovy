package com.piotrglazar.wellpaidwork.util

import com.google.common.collect.ImmutableMap
import spock.lang.Specification

class TagFileReaderTest extends Specification {

    def reader = new TagFileReader()

    def path = "testTechnologyTags.txt"

    def "should parse tags"() {
        when:
        def tags = reader.readTagsFrom(path)

        then:
        tags.isSuccess()
        tags.get().tags == ["java", "java8", "scala"].toSet()
    }

    def "should parse synonyms"() {
        when:
        def tags = reader.readTagsFrom(path)

        then:
        tags.isSuccess()
        tags.get().synonyms == ImmutableMap.of("ecmascript 2015", "javascript6", "angular", "angularjs")
    }
}
