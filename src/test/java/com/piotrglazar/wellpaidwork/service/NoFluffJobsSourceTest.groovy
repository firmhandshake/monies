package com.piotrglazar.wellpaidwork.service

import com.piotrglazar.wellpaidwork.TestCreators
import com.piotrglazar.wellpaidwork.api.NoFluffJobsClient
import com.piotrglazar.wellpaidwork.model.TechnologyTags
import com.piotrglazar.wellpaidwork.model.TitleTags
import com.piotrglazar.wellpaidwork.util.Try
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

import static com.piotrglazar.wellpaidwork.model.Category.BACKEND

class NoFluffJobsSourceTest extends Specification implements TestCreators {

    def backendJob = noFluffJob("id1", "backend", "warsaw")
    def hrJob = noFluffJob("id2", "hr", "warsaw")
    def backendJobDetails = noFluffJobDetails("id1", "backend")

    def "should fetch jobs from no fluff page"() {
        given:
        def client = Mock(NoFluffJobsClient)
        def source = new NoFluffJobsSource(client, filter(),
                new NoFluffJobBuilder(new TitleTags([].toSet(), [:]), new TechnologyTags([].toSet(), [:])))

        when:
        def found = source.fetch()

        then:
        1 * client.getJobPostings() >> Optional.of(
                jobPostings(backendJob, hrJob)
        )
        1 * client.getJobDetailsAsync(backendJob) >> CompletableFuture.completedFuture(Optional.of(backendJobDetails))
        0 * client.getJobDetails(hrJob)
        found.size() == 1
        found.category == [BACKEND]
        found.city == ["warsaw"]
    }

    def "should not fail when it was unable to build job details"() {
        given:
        def client = Mock(NoFluffJobsClient)
        def builder = Mock(NoFluffJobBuilder)
        def source = new NoFluffJobsSource(client, filter(), builder)

        when:
        def found = source.fetch()

        then:
        1 * client.getJobPostings() >> Optional.of(
                jobPostings(backendJob, hrJob)
        )
        1 * client.getJobDetailsAsync(backendJob) >> CompletableFuture.completedFuture(Optional.of(backendJobDetails))
        1 * builder.toJobOffer(backendJob, backendJobDetails) >> Try.failed(new RuntimeException("test - building exception"))
        found.isEmpty()
    }

    def "should return empty list when client failed to fetch jobs"() {
        given:
        def client = Mock(NoFluffJobsClient)
        def jobBuilder = Mock(NoFluffJobBuilder)
        def source = new NoFluffJobsSource(client, filter(), jobBuilder)

        when:
        def found = source.fetch()

        then:
        found.size() == 0
        0 * jobBuilder.toJobOffer(_, _)
        1 * client.getJobPostings() >> Optional.empty()
    }

    private static filter() {
        new NoFluffJobsFilter("backend", "warsaw")
    }
}
