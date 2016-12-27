package com.piotrglazar.wellpaidwork.util;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FutureUtils {

    private FutureUtils() {
        // utility class
    }

    public static <T> CompletableFuture<List<T>> asList(List<CompletableFuture<T>> futures) {
        return futures.stream()
                .map(FutureUtils::toListFuture)
                .reduce(emptyListFuture(), FutureUtils::concatFutures);
    }

    private static <T> CompletableFuture<List<T>> toListFuture(CompletableFuture<T> future) {
        return future.thenApply(Collections::singletonList);
    }

    private static <T> CompletableFuture<List<T>> emptyListFuture() {
        return CompletableFuture.completedFuture(Lists.newArrayList());
    }

    private static <T> List<T> concat(List<T> first, List<T> second) {
        return Stream.concat(first.stream(), second.stream()).collect(Collectors.toList());
    }

    private static <T> CompletableFuture<List<T>> concatFutures(CompletableFuture<List<T>> first,
                                                                CompletableFuture<List<T>> second) {
        return first.thenCombine(second, FutureUtils::concat);
    }
}
