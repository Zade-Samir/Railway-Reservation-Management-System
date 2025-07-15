package com.capgi.train_service.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgi.train_service.model.Train;
import com.capgi.train_service.service.TrainService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/train")
public class TrainController {
	
	private static final Logger logger = LoggerFactory.getLogger(TrainController.class);
	
	private final TrainService trainService;
	
	//add new train
	@PostMapping("/add")
	public ResponseEntity<Train> addTrain(@RequestBody Train train) {
		logger.info("Request to add train: {}", train.getTrainNumber());
		Train savedTrain = trainService.addTrain(train);
		
		logger.info("Train saved successfully: {}", savedTrain.getTrainNumber());
		return ResponseEntity.status(201).body(savedTrain);
	}
	
	//get all the trains
	@GetMapping
	public ResponseEntity<List<Train>> getAllTrains() {
		logger.info("Fetching all trains...");
		List<Train> trains = trainService.getAllTrains();
		
		logger.info("Total trains found: {}", trains.size());
		return ResponseEntity.ok(trains);
	}
	
	//get trains by trainNumber
	@GetMapping("/{trainNumber}")
	public ResponseEntity<Train> getTrainByNumber(@PathVariable String trainNumber) {
		
		logger.info("Fetching train with number: {}", trainNumber);
		Train train = trainService.getTrainByTrainNumber(trainNumber);
		
		if (train == null) {
			logger.info("Train not found (null): {}", train);
			return ResponseEntity.notFound().build();
		}
		logger.info("Train found: {}", train);
		return ResponseEntity.ok(train);
	}
	
	//delete train using trainNumber
	@DeleteMapping("/{trainNumber}")
	public ResponseEntity<Void> deleteTrain(@PathVariable String trainNumber) {
		logger.warn("Request to delete train: {}", trainNumber);
		trainService.deleteTrain(trainNumber);
		
		logger.info("Train deleted successfully: {}", trainNumber);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Train>> searchTrains(
	        @RequestParam String source,
	        @RequestParam String destination,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate journeyDate) {

	    logger.info("Search request: {} to {} on {}", source, destination, journeyDate);
	    List<Train> trains = trainService.searchTrains(source, destination, journeyDate);

	    return trains.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(trains);
	}

	
}




















