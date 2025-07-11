package com.carrental.controller;

import com.carrental.dto.BookCarRequest;
import com.carrental.dto.BookCarResponse;
import com.carrental.entity.Car;
import com.carrental.service.BookingService;
import com.carrental.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

import java.util.List;
import java.util.NoSuchElementException;

@Controller("/user")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;

    @Inject
    public UserController(UserService userService,BookingService bookingService){
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @Get("/available-cars")
    public List<Car> allAvailableCars(){
        return userService.getAllAvailableCars();
    }

    @Post("/book-car")
    public HttpResponse<BookCarResponse> bookCar(@Body @Valid BookCarRequest request) {
       try{
           BookCarResponse response =  bookingService.bookMyCar(request);
           return HttpResponse.created(response);
       }catch(RuntimeException e){
           return HttpResponse.badRequest();
        }
    }

    @Post("/return-car/{id}")
    public HttpResponse<String> returnCar(@PathVariable Long id){
        try{
           bookingService.returnCar(id);
           return HttpResponse.ok("Car Returned Successfully");
        }catch(NoSuchElementException e){
            return HttpResponse.notFound("Car Not found");
        }
    }
}
