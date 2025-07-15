package com.capgi.login_service.exception;

public class InvalidJwtException extends RuntimeException {

    // Constructor that takes a message as a parameter
    public InvalidJwtException(String message) {
        super(message);
    }

    // Constructor that takes a message and a cause (another throwable) as parameters
    public InvalidJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
