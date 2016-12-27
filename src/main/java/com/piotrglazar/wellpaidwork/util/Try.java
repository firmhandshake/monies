package com.piotrglazar.wellpaidwork.util;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class Try<T> {

    private final Optional<T> value;
    private final Optional<Throwable> error;
    private final boolean isSuccess;

    private Try(T value) {
        this.value = Optional.ofNullable(value);
        this.error = Optional.empty();
        this.isSuccess = true;
    }

    private Try(Throwable t) {
        this.value = Optional.empty();
        this.error = Optional.ofNullable(t);
        this.isSuccess = false;
    }

    public static <V> Try<V> success(V value) {
        return new Try<>(value);
    }

    public static <V extends Throwable> Try<?> failed(V failure) {
        return new Try<>(failure);
    }

    public static <V> Try<V> of(Callable<V> supplier) {
        try {
            V value = supplier.call();
            return new Try<>(value);
        } catch (Throwable t) {
            return new Try<>(t);
        }
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    @SuppressWarnings("unchecked")
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        if (isSuccess) {
            return of(() -> mapper.apply(this.value.get()));
        } else {
            return (Try<U>) this;
        }
    }

    @SuppressWarnings("unchecked")
    public <U> Try<U> flatMap(Function<T, Try<U>> mapper) {
        Objects.requireNonNull(mapper);

        if (isSuccess) {
            return mapper.apply(this.value.get());
        } else {
            return (Try<U>) this;
        }
    }

    public T get() {
        if (isSuccess) {
            return value.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    public T getOrElse(T defaultValue) {
        if (isSuccess) {
            return value.get();
        } else {
            return defaultValue;
        }
    }

    public T recover(Function<? super Throwable, ? extends T> recovery) {
        if (isSuccess) {
            return value.get();
        } else {
            return recovery.apply(error.get());
        }
    }

    public Throwable getException() {
        if (isSuccess) {
            throw new NoSuchElementException();
        } else {
            return error.get();
        }
    }
}
