package com.capgi.booking_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgi.booking_service.dto.BookingRequest;
import com.capgi.booking_service.model.Booking;
import com.capgi.booking_service.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/booking")
//@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
	
	@Autowired
	private BookingService bookingService;
	
	//create the booking
	@PostMapping("/create") //@Valid is used to trigger automatic validation of the BookingRequest before processing the request
	public ResponseEntity<Booking> createBooking(@RequestBody @Valid BookingRequest bookingRequest) {
		logger.info("Received request to create booking for user: {}", bookingRequest.getUserEmail());
		Booking savedBooking = bookingService.createBooking(bookingRequest);
		logger.info("Booking created successfully with Id : {}", savedBooking);
		return ResponseEntity.ok(savedBooking);
	}
	
	// Get all bookings by user email
	@GetMapping("/user/{email}")
	public ResponseEntity<List<Booking>> getBookingByUser(@PathVariable String email) {
		logger.info("Fetching bookings for user email: {}", email);
		List<Booking> bookings = bookingService.getBookingsByUserEmail(email);
		logger.info("Found {} bookings for user: {}", bookings.size(), email);
		return ResponseEntity.ok(bookings);
	}
	
	// Get all bookings by train number
	@GetMapping("/train/{trainNumber}")
	public ResponseEntity<List<Booking>> getBookingByTrain(@PathVariable String trainNumber) {
		logger.info("Fetching bookings for train number: {}", trainNumber);
		List<Booking> bookings = bookingService.getBookingsByTrainNumber(trainNumber);
		logger.info("Found {} bookings for train number: {}", bookings.size(), trainNumber);
		return ResponseEntity.ok(bookings);
	}
	
	//getting all the bookings list
	@GetMapping("/all")
	public ResponseEntity<List<Booking>> getAllBookings() {
		logger.info("Fetching all bookings");
		List<Booking> bookings = bookingService.getAllBookings();
		logger.info("Total bookings found: {}", bookings.size());
		return ResponseEntity.ok(bookings);
	}

	//cancel the booking
	@DeleteMapping("/cancel/{id}")
	public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
		logger.info("Received request to cancel booking with ID: {}", id);
		bookingService.cancelBooking(id);
		logger.info("Booking cancelled successfully for ID: {}", id);
		return ResponseEntity.ok("Booking Cancelled Successfully!");
	}
}














