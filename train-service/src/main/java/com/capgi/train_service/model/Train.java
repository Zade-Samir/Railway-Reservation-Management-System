package com.capgi.train_service.model;


import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trains")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Train {
    
	@Id
    @NotBlank(message = "Train number must not be blank")
    private String trainNumber;

    @NotBlank(message = "Train name must not be blank")
    private String trainName;

    @NotBlank(message = "Source must not be blank")
    private String source;

    @NotBlank(message = "Destination must not be blank")
    private String destination;

    @NotBlank(message = "Departure time must not be blank")
    private String departureTime;

    @NotBlank(message = "Arrival time must not be blank")
    private String arrivalTime;

    @Min(value = 0, message = "General class seats must be 0 or more")
    private int generalSeats;

    @Min(value = 0, message = "Sleeper class seats must be 0 or more")
    private int sleeperSeats;

    @Min(value = 0, message = "3AC class seats must be 0 or more")
    private int thirdACSeats;

    @Min(value = 0, message = "2AC class seats must be 0 or more")
    private int secondACSeats;

    @Min(value = 0, message = "1AC class seats must be 0 or more")
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
    
    @FutureOrPresent(message = "Journey date must be today or in the future")
    private LocalDate journeyDate;

}
