package com.capgi.booking_service.exception;

public class BookingNotFoundException extends RuntimeException {
	
	public BookingNotFoundException(String message) {
        super(message);
    }

}
