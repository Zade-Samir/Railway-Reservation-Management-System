package com.capgi.train_service.service;

import java.time.LocalDate;
import java.util.List;

import com.capgi.train_service.model.Train;

public interface TrainService {
	
//	Train getTrainByTrainNumber(String trainNumber);
	
	Train addTrain(Train train);
	
	List<Train> getAllTrains();
	
	Train getTrainByTrainNumber(String trainNumber);
	
    void deleteTrain(String trainNumber);

//    String fetchTrainsFromGovAPI(String source, String destination, int hours);

    List<Train> searchTrains(String source, String destination, LocalDate journeyDate);

}
