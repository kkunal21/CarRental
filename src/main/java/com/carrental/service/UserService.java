package com.carrental.service;

import com.carrental.dto.LoginRequest;
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

    public void createNewUser(LoginRequest loginRequest){
        Optional<User> optionalUser = userRepository.findByUserName(loginRequest.getUserName());
        if(optionalUser.isPresent()){
             throw new RuntimeException("User Already Exist , Try singing in...");
        }
        String encodedPassword = bcryptPasswordService.encode(loginRequest.getPassword());
       User user = new User(loginRequest.getUserName() , encodedPassword , "USER");
         userRepository.save(user);
    }

    public void createNewAdmin(LoginRequest request){

        Optional<User> optionalUser = userRepository.findByUserName(request.getUserName());
        if(optionalUser.isPresent()){
            throw new RuntimeException("Admin Already Exist , Try singing in...");
        }
        String encodedPassword = bcryptPasswordService.encode(request.getPassword());
        User user = new User(request.getUserName() , encodedPassword, "ADMIN");
        userRepository.save(user);
    }

    public List<Car> getAllAvailableCars(){
        return carRepository.findByAvailability(true);
    }
}
