package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class UserService {

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    @Inject
    public UserService(CarRepository carRepository, UserRepository userRepository){
        this.carRepository= carRepository;
        this.userRepository = userRepository;
    }

    public void createUser(User user){
         userRepository.save(user);
    }

    public List<Car> getAllAvailableCars(){
        return carRepository.findByAvailability(true);
    }
}
