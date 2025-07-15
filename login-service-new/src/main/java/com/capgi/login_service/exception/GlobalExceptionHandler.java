package com.capgi.login_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle InvalidJwtException
    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<String> handleInvalidJwtException(InvalidJwtException e) {

        return new ResponseEntity<>("Invalid JWT token: " + e.getMessage(), HttpStatus.FORBIDDEN);
    }

    // Handle InvalidCredentialsException
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return new ResponseEntity<>("Invalid credentials: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
