package com.capgi.booking_service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.capgi.booking_service.dto.Train;

@FeignClient(name = "TRAIN-SERVICE")
public interface TrainClient {

    @GetMapping("/api/train")
    List<Train> getAllTrains();

    @GetMapping("/api/train/{trainNumber}")
    Train getTrainByNumber(@PathVariable String trainNumber);

    @PostMapping("/api/train/add")
    Train addTrain(@RequestBody Train train);

    @DeleteMapping("/api/train/{trainNumber}")
    void deleteTrain(@PathVariable String trainNumber);
}


