package com.example.ehbrenting.exceptions;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("Passwords do not match");
    }

    public PasswordMismatchException(String message) {
        super(message);
    }
}
