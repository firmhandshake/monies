package com.piotrglazar.wellpaidwork.util;

import org.joda.time.DateTime;

@FunctionalInterface
public interface DateTimeProvider {

    DateTime get();
}
