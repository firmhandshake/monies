package com.piotrglazar.wellpaidwork.model

import com.piotrglazar.wellpaidwork.util.TagFileReader
import spock.lang.Shared
import spock.lang.Specification

class TechnologyTagsIntegrationTest extends Specification {

    @Shared
    TechnologyTags technologyTags

    def "should parse tokens"() {
        given:
        def technology = "sharepoint"

        when:
        def tags = technologyTags.tags(technology)

        then:
        tags == ["sharepoint"].toSet()
    }

    def "should split by comma"() {
        given:
        def technology = "java, sql, html"

        when:
        def tags = technologyTags.tags(technology)

        then:
        tags == ["java", "sql", "html"].toSet()
    }

    def "should also split by pipe"() {
        given:
        def technology = "|node js|"

        when:
        def tags = technologyTags.tags(technology)

        then:
        tags == ["nodejs", "javascript"].toSet()
    }

    def setupSpec() {
        def file = "technologyTags.txt"
        def reader = new TagFileReader()
        def fetched = reader.readTagsFrom(file)
        def foundTags = fetched.get()
        technologyTags = new TechnologyTags(foundTags.tags, foundTags.synonyms)
    }
}
