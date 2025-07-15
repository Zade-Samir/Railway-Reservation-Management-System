package com.capgi.train_service.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.capgi.train_service.exception.TrainNotFoundException;
import com.capgi.train_service.model.Train;
import com.capgi.train_service.repository.TrainRepository;
import com.capgi.train_service.service.TrainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {
	
	private static final Logger logger = LoggerFactory.getLogger(TrainServiceImpl.class);

    private final TrainRepository trainRepository;
    
    private final WebClient webClient;
    
    @Value("${rapidapi.key}")
    private String apiKey;

    @Value("${rapidapi.host}")
    private String apiHost;
    
    
    public String fetchTrainsFromGovAPI(String fromStation, String toStation, int hours) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(apiHost)
                        .path("/api/v3/getLiveStation")
                        .queryParam("fromStationCode", fromStation)
                        .queryParam("toStationCode", toStation)
                        .queryParam("hours", hours)
                        .build())
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", apiHost)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
  

    
	
	@Override
	public Train addTrain(Train train) {
		
		logger.info("Adding new train: {}", train.getTrainNumber());
		return trainRepository.save(train);
	}

	@Override
	public List<Train> getAllTrains() {
		
		logger.info("Fetching all trains");	
		return trainRepository.findAll();
	}

	@Override
	public Train getTrainByTrainNumber(String trainNumber) {
		
		logger.info("Fetching train with number: {}", trainNumber);
        Train train = trainRepository.findByTrainNumber(trainNumber);
        
        if (train == null) {
            logger.warn("Train with number {} not found", trainNumber);
            throw new TrainNotFoundException("Train not found with number: " + trainNumber);
        }
        return train;
	}

	@Override
	public void deleteTrain(String trainNumber) {
		
		if (!trainRepository.existsById(trainNumber)) {
            logger.warn("Cannot delete. Train with number {} not found", trainNumber);
            throw new TrainNotFoundException("Train not found with number: " + trainNumber);
        }
        trainRepository.deleteById(trainNumber);
        logger.info("Train with number {} deleted successfully", trainNumber);
	}

	@Override
	public List<Train> searchTrains(String source, String destination, LocalDate journeyDate) {
	    logger.info("Searching for trains from {} to {} on {}", source, destination, journeyDate);
	    return trainRepository.findBySourceAndDestinationAndJourneyDate(source, destination, journeyDate);
	}

}













