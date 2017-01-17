package com.piotrglazar.wellpaidwork;

import com.piotrglazar.wellpaidwork.util.DateTimeProvider;
import org.joda.time.DateTime;

import java.util.Optional;

public class TestDateTimeProvider implements DateTimeProvider {

    private Optional<DateTime> dateTimeInTest = Optional.empty();

    public TestDateTimeProvider set(DateTime dateTime) {
        dateTimeInTest = Optional.ofNullable(dateTime);
        return this;
    }

    public TestDateTimeProvider reset() {
        dateTimeInTest = Optional.empty();
        return this;
    }

    public TestDateTimeProvider withDate(String date) {
        return set(DateTime.parse(date));
    }

    @Override
    public DateTime get() {
        return dateTimeInTest.orElseGet(DateTime::new);
    }
}
