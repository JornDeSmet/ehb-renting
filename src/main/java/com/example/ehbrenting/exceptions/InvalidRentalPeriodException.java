package com.example.ehbrenting.exceptions;

public class InvalidRentalPeriodException extends RuntimeException {
    public InvalidRentalPeriodException(String message) {
        super(message);
    }
}
