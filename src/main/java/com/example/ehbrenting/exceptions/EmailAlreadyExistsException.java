package com.example.ehbrenting.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("Email already in use");
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
