package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserService {

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    private final BcryptPasswordService bcryptPasswordService;

    @Inject
    public UserService(CarRepository carRepository, UserRepository userRepository,BcryptPasswordService bcryptPasswordService){
        this.carRepository= carRepository;
        this.userRepository = userRepository;
        this.bcryptPasswordService = bcryptPasswordService;
    }

    public void createNewUser(User user){
        Optional<User> optionalUser = userRepository.findByUserName(user.getUserName());
        if(optionalUser.isPresent()){
             throw new RuntimeException("User Already Exist , Try singing in...");
        }
        user.setPassword(bcryptPasswordService.encode(user.getPassword()));
        user.setRole("USER");
         userRepository.save(user);
    }

    public void createNewAdmin(User user){
        Optional<User> optionalUser = userRepository.findByUserName(user.getUserName());
        if(optionalUser.isPresent()){
            throw new RuntimeException("Admin Already Exist , Try singing in...");
        }
        user.setPassword(bcryptPasswordService.encode(user.getPassword()));
        user.setRole("ADMIN");
        userRepository.save(user);
    }

    public List<Car> getAllAvailableCars(){
        return carRepository.findByAvailability(true);
    }
}
