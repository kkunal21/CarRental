package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.repository.CarRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public List<Car> getAvailableCars(){
        return carRepository.findByAvailability(true);
    }


    public void addCar(Car car){

          carRepository.save(car);
    }

    public Optional<Car> findById(Long id){
        return carRepository.findById(id);
    }

    public Car updateCarDetails(Long id , Car updatedCar){

        Car oldCar = carRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Car not found"));

            oldCar.setBrand(updatedCar.getBrand());
            oldCar.setModel(updatedCar.getModel());
            oldCar.setPricePerDay(updatedCar.getPricePerDay());
            oldCar.setAvailability(updatedCar.isAvailability());

            carRepository.update(oldCar);
            return oldCar;
    }

    public void deleteCar(Long id){
//        Car car = carRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No Car found"));
        carRepository.deleteById(id);
    }


}
