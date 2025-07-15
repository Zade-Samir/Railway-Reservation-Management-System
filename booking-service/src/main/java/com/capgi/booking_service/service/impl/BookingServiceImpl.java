package com.capgi.booking_service.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgi.booking_service.TrainClient;
import com.capgi.booking_service.dto.BookingRequest;
import com.capgi.booking_service.dto.Train;
import com.capgi.booking_service.exception.BookingNotFoundException;
import com.capgi.booking_service.exception.NoAvailableSeatsException;
import com.capgi.booking_service.model.Booking;
import com.capgi.booking_service.repository.BookingRepository;
import com.capgi.booking_service.service.BookingService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
	
	private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
	
	//if @autowired used, it can be modified after object creation. 
	//This may introduce risks of unintentional changes.
	private final BookingRepository bookingRepository;
	
	private final TrainClient trainClient;
	
	@Override
	public Booking createBooking(BookingRequest bookingRequest) {
		
		logger.info("Creating booking for user: {} on train: {}",
                bookingRequest.getUserEmail(), bookingRequest.getTrainNumber());
		
		Train train = trainClient.getTrainByNumber(bookingRequest.getTrainNumber());	
		
		if (train == null) {
            logger.error("Train not found for number: {}", bookingRequest.getTrainNumber());
            throw new RuntimeException("Train not found.");
        }
		
		int requestedSeats = bookingRequest.getSeatsBooked();
		String seatClass = bookingRequest.getSeatClass();
		double farePerSeat;
		int availableSeats;

		switch (seatClass.toUpperCase()) {
			case "GENERAL" -> {
				availableSeats = train.getGeneralSeats();
				farePerSeat = train.getGeneralFare();
			}
			case "SLEEPER" -> {
				availableSeats = train.getSleeperSeats();
				farePerSeat = train.getSleeperFare();
			}
			case "3AC" -> {
				availableSeats = train.getThirdACSeats();
				farePerSeat = train.getThirdACFare();
			}
			case "2AC" -> {
				availableSeats = train.getSecondACSeats();
				farePerSeat = train.getSecondACFare();
			}
			case "1AC" -> {
				availableSeats = train.getFirstACSeats();
				farePerSeat = train.getFirstACFare();
			}
			default -> throw new RuntimeException("Invalid seat class provided.");
		}

		if (availableSeats < requestedSeats) {
			logger.warn("Not enough seats in {}: requested {}, available {}", seatClass, requestedSeats, availableSeats);
			throw new NoAvailableSeatsException("Not enough seats available in selected class: " + seatClass);
		}

		
		// Fill booking details from train response
		Booking booking = new Booking();
		
	    booking.setUserEmail(bookingRequest.getUserEmail());
	    booking.setTrainNumber(train.getTrainNumber());
	    booking.setTrainName(train.getTrainName());
	    booking.setSource(train.getSource());
	    booking.setDestination(train.getDestination());
	    booking.setJourneyDate(bookingRequest.getJourneyDate());
	    booking.setSeatClass(seatClass);
	    booking.setSeatsBooked(requestedSeats);
	    booking.setFarePaid(farePerSeat * requestedSeats);
	    booking.setBookingDate(LocalDate.now());
	    booking.setStatus("CONFIRMED");
		
	    Booking saved = bookingRepository.save(booking);
	    logger.info("Booking successfully created with ID: {}", saved.getId());
		return saved;
	}
	
	@Override
	public List<Booking> getBookingsByUserEmail(String email) {
		logger.info("Fetching bookings for user email: {}", email);
		List<Booking> bookings = bookingRepository.findByUserEmail(email);
		logger.info("Found {} bookings for user email: {}", bookings.size(), email);
		return bookings;
	}
	
	@Override
	public List<Booking> getBookingsByTrainNumber(String trainNumber) {
		logger.info("Fetching bookings for train number: {}", trainNumber);
		List<Booking> bookings = bookingRepository.findByTrainNumber(trainNumber);
	    logger.info("Found {} bookings for train number: {}", bookings.size(), trainNumber);
	    return bookings;
	}
	
	@Override
	public List<Booking> getAllBookings() {
		logger.info("Fetching all bookings from repository");
		List<Booking> bookings = bookingRepository.findAll();
	    logger.info("Found total {} bookings in repository", bookings.size());
	    return bookings;
	}

	@Override
	public void cancelBooking(Long id) {
		logger.info("Request to cancel booking with ID: {}", id);
		Booking booking = bookingRepository.findById(id)
				.orElseThrow(() -> {
					logger.error("Booking not found for ID: {}", id);
					return new BookingNotFoundException("Booking not found with Id: " + id);
				});
		
		bookingRepository.delete(booking);
		logger.info("Booking with ID: {} successfully cancelled", id);
	}

}
