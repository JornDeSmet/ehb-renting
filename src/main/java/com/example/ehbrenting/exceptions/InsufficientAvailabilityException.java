package com.example.ehbrenting.exceptions;

public class InsufficientAvailabilityException extends RuntimeException {
    public InsufficientAvailabilityException(String message) {
        super(message);
    }
}
