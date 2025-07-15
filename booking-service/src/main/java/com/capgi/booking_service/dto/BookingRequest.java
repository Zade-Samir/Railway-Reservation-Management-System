package com.capgi.booking_service.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
	
	//using 'validation' for validate the things
	@NotBlank(message = "User email is mandatory")
    @Email(message = "Invalid email format")
    private String userEmail;

    @NotBlank(message = "Train number is mandatory")
    private String trainNumber;

    @NotBlank(message = "Train name is mandatory")
    private String trainName;

    @NotBlank(message = "Source is mandatory")
    private String source;

    @NotBlank(message = "Destination is mandatory")
    private String destination;

    @NotNull(message = "Journey date is mandatory")
    @FutureOrPresent(message = "Journey date must be today or a future date")
    private LocalDate journeyDate;

    @Min(value = 1, message = "At least 1 ticket must be booked")
    private int seatsBooked;

    @NotBlank(message = "Seat class is required")
    private String seatClass; // e.g. GENERAL, SLEEPER, 3AC, 2AC, 1AC

}
