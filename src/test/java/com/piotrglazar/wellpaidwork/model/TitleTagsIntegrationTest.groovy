package com.piotrglazar.wellpaidwork.model

import com.piotrglazar.wellpaidwork.util.TagFileReader
import spock.lang.Shared
import spock.lang.Specification

class TitleTagsIntegrationTest extends Specification {

    @Shared
    TitleTags titleTags

    def "should tokenize typical job title"() {
        given:
        def title = "Senior Java Developer"

        when:
        def tags = titleTags.tags(title)

        then:
        tags == ["senior", "java", "developer"].toSet()
    }

    def "should use synonyms"() {
        given:
        def title = "QA"

        when:
        def tags = titleTags.tags(title)

        then:
        tags == ["quality assurance"].toSet()
    }

    def "should also split by '.'"() {
        given:
        def title = "Sr. engineer"

        when:
        def tags = titleTags.tags(title)

        then:
        tags == ["senior", "engineer"].toSet()
    }

    def "should also split by '/'"() {
        given:
        def title = "Java/Scala Developer"

        when:
        def tags = titleTags.tags(title)

        then:
        tags == ["java", "scala", "developer"].toSet()
    }

    def "should also split by ','"() {
        given:
        def title = "C++,C Developer"

        when:
        def tags = titleTags.tags(title)

        then:
        tags == ["c++", "c", "developer"].toSet()
    }

    def setupSpec() {
        def file = "titleTags.txt"
        def reader = new TagFileReader()
        def fetched = reader.readTagsFrom(file)
        def foundTags = fetched.get()
        titleTags = new TitleTags(foundTags.tags, foundTags.synonyms)
    }
}
