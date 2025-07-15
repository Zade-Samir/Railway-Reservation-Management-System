package com.capgi.booking_service.exception;

public class NoAvailableSeatsException extends RuntimeException {
	
    public NoAvailableSeatsException(String message) {
        super(message);
    }
}
