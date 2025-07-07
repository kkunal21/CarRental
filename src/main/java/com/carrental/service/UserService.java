package com.carrental.service;

import com.carrental.dto.LoginRequest;
import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.globalException.EmptyUsernameOrPasswordException;
import com.carrental.globalException.UserAlreadyExistsException;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

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
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        if(username == null || username.isBlank() || password == null  ||password.isBlank())
        {
            throw  new EmptyUsernameOrPasswordException("Username or Password cannot be blank or empty");
        }

        Optional<User> optionalUser = userRepository.findByUserName(loginRequest.getUsername());
        if(optionalUser.isPresent()){
             throw new UserAlreadyExistsException("User Already Exist , Try singing in...");
        }
        String encodedPassword = bcryptPasswordService.encode(loginRequest.getPassword());
       User user = new User(loginRequest.getUsername() , encodedPassword , "USER");
         userRepository.save(user);
    }

    public void createNewAdmin(LoginRequest request){
        String username = request.getUsername();
        String password = request.getPassword();
        if(username == null || username.isBlank() || password == null  ||password.isBlank())
        {
            throw  new EmptyUsernameOrPasswordException("Username or Password cannot be blank or empty");
        }

        Optional<User> optionalUser = userRepository.findByUserName(request.getUsername());
        if(optionalUser.isPresent()){
            throw new UserAlreadyExistsException("Admin Already Exist , Try singing in...");
        }

        String encodedPassword = bcryptPasswordService.encode(request.getPassword());
        User user = new User(request.getUsername() , encodedPassword, "ADMIN");
        userRepository.save(user);
    }

    public List<Car> getAllAvailableCars(){
        return carRepository.findByAvailability(true);
    }
}
