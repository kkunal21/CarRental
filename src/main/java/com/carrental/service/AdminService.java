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

//  return list of all cars with their status as booked or Available
    public List<Car> getAllCars(){
        return carRepository.findAll();
    }

//    return list of only Booked cars
    public List<Car> getBookedCars(){
        return carRepository.findByAvailability(false);
    }

//    return list of only available cars
    public List<Car> getAvailableCars(){
        return carRepository.findByAvailability(true);
    }

//  addCar allows the admin to add a new car
    public void addCar(Car car){
          carRepository.save(car);
    }

//   searches for the car by id if it Exists
    public Optional<Car> findById(Long id){
        return carRepository.findById(id);
    }

//    updateCar allows Admin to update the details of the existing car
    public Car updateCarDetails(Long id , Car updatedCar){

        Car oldCar = getCarOrThrow(id);

            oldCar.setBrand(updatedCar.getBrand());
            oldCar.setModel(updatedCar.getModel());
            oldCar.setPricePerDay(updatedCar.getPricePerDay());
            oldCar.setAvailability(updatedCar.isAvailability());

            carRepository.update(oldCar);
            return oldCar;
    }
    private Car getCarOrThrow(Long id){
        return carRepository.findById(id).orElseThrow(()->new NoSuchElementException("Car Not Found"));
    }


//  allows the admin to delete the car by id if it Exists in the DB
    public void deleteCar(Long id){
        assertCarExists(id);
        carRepository.deleteById(id);
    }

//    just check if car exist before deleting
    private void assertCarExists(Long id) {
        if (!carRepository.existsById(id)) {
            throw new NoSuchElementException("Car Not Found with id: " + id);
        }
    }
}