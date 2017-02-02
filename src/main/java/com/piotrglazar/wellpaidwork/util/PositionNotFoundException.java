package com.piotrglazar.wellpaidwork.util;

public class PositionNotFoundException extends RuntimeException {

    public PositionNotFoundException(String position) {
        super("Unknown position " + position);
    }
}
