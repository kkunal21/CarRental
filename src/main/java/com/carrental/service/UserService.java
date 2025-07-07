package com.carrental.service;

import com.carrental.dto.LoginRequest;
import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.exception.EmptyUsernameOrPasswordException;
import com.carrental.exception.UserAlreadyExistsException;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
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

//    create user will create new user if request is valid
    public void createNewUser(LoginRequest request){
        validateRequest(request);
        String encodedPassword = bcryptPasswordService.encode(request.getPassword());
        User user = new User(request.getUsername() , encodedPassword , "USER");
        userRepository.save(user);


    }

    //    createNewAdmin will create new Admin if request is valid
    public void createNewAdmin(LoginRequest request){
        validateRequest(request);
        String encodedPassword = bcryptPasswordService.encode(request.getPassword());
        User admin = new User(request.getUsername() , encodedPassword, "ADMIN");
        userRepository.save(admin);

    }

//    check if the request coming from user is valid or not
    public void validateRequest(LoginRequest loginRequest){
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
    }

//  returns a list of car that user can choose from to book a car
    public List<Car> getAllAvailableCars(){
        return carRepository.findByAvailability(true);
    }
}
