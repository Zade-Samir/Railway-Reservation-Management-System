package com.capgi.booking_service.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Train {

    @NotBlank(message = "Train number is mandatory")
    private String trainNumber;

    @NotBlank(message = "Train name is mandatory")
    private String trainName;

    @NotBlank(message = "Source is mandatory")
    private String source;

    @NotBlank(message = "Destination is mandatory")
    private String destination;

    @NotBlank(message = "Departure time is mandatory")
    private String departureTime;

    @NotBlank(message = "Arrival time is mandatory")
    private String arrivalTime;

    @Min(value = 0)
    private int generalSeats;

    @Min(value = 0)
    private int sleeperSeats;

    @Min(value = 0)
    private int thirdACSeats;

    @Min(value = 0)
    private int secondACSeats;

    @Min(value = 0)
    private int firstACSeats;

    @Min(value = 0)
    private double generalFare;

    @Min(value = 0)
    private double sleeperFare;

    @Min(value = 0)
    private double thirdACFare;

    @Min(value = 0)
    private double secondACFare;

    @Min(value = 0)
    private double firstACFare;

    @NotNull(message = "Journey date is required")
    private LocalDate journeyDate;
}
