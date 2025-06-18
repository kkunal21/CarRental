package com.carrental.controller;

import com.carrental.dto.BookCarRequest;
import com.carrental.dto.BookCarResponse;
import com.carrental.entity.Car;
import com.carrental.service.BookingService;
import com.carrental.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

import java.util.List;

@Controller("/user")
public class UserController {


    private final UserService userService;


    private final BookingService bookingService;


    @Inject
    public UserController(UserService userService,BookingService bookingService){
        this.userService = userService;
        this.bookingService = bookingService;
    }


    @Get("/availabeCars")
    public List<Car> allAvailableCars(){
        return userService.getAllAvailableCars();
    }

    @Post("/bookCar")
    public HttpResponse<BookCarResponse> bookCar(@Body @Valid BookCarRequest request) {

       try{
           BookCarResponse response =  bookingService.bookMyCar(request);
           return HttpResponse.created(response);
       }catch(RuntimeException e){
           return HttpResponse.badRequest();
        }
    }

}
