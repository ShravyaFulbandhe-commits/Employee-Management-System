package com.ems.exception;

/** Thrown when user input fails validation. */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
