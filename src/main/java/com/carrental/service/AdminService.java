package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.repository.CarRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class AdminService {

    private final CarRepository carRepository;


    @Inject
    public AdminService(CarRepository carRepository){
        this.carRepository=carRepository;
    }

    public List<Car> getAllCars(){
        return carRepository.findAll();
    }

    public List<Car> getBookedCars(){
        return carRepository.findByAvailability(false);
    }


    public Car addCar(Car car){
         return carRepository.save(car);
    }

}
