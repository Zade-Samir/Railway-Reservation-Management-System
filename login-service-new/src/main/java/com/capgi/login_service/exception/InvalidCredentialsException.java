package com.capgi.login_service.exception;

public class InvalidCredentialsException extends RuntimeException {

    // Constructor with error message
    public InvalidCredentialsException(String message) {
        super(message);
    }

    // Constructor with error message and cause
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
