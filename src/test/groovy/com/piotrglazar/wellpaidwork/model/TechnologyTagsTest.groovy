package com.piotrglazar.wellpaidwork.model

import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import spock.lang.Specification

class TechnologyTagsTest extends Specification {

    def technologyTags = new TechnologyTags(
            ImmutableSet.of("machine learning", "pl/sql", "aws", "openstack", "html5", "c", "c++", "springboot"),
            ImmutableMap.of("ecmascript 2015", "javascript6")
    )

    def "should process multi string tags"() {
        given:
        def technology = "machine learning"

        when:
        def tags = technologyTags.tags(technology)

        then:
        tags == ["machine learning"].toSet()
    }

    def "should process synonyms"() {
        given:
        def technology = "ECMAScript 2015"

        when:
        def tags = technologyTags.tags(technology)

        then:
        tags == ["javascript6"].toSet()
    }

    def "should process tags with split chars"() {
        given:
        def technology = "pl/sql"

        when:
        def tag = technologyTags.tags(technology)

        then:
        tag == ["pl/sql"].toSet()
    }

    def "should also split by colon"() {
        given:
        def technology = "cloud: aws or openstack"

        when:
        def tags = technologyTags.tags(technology)

        then:
        tags == ["aws", "openstack"].toSet()
    }

    def "should remove brackets"() {
        given:
        def technology = "(c or c++)"

        when:
        def tags = technologyTags.tags(technology)

        then:
        tags == ["c", "c++"].toSet()
    }

    def "should process normal tags"() {
        when:
        def tags = technologyTags.tags(technology)

        then:
        tags == result

        where:
        technology   || result
        "html5"      || ["html5"].toSet()
        "c/c++"      || ["c", "c++"].toSet()
        "springboot" || ["springboot"].toSet()
    }
}
