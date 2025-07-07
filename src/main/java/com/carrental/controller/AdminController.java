package com.carrental.controller;

import com.carrental.dto.LoginRequest;
import com.carrental.entity.Car;

import com.carrental.entity.User;
import com.carrental.service.AdminService;
import com.carrental.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller("/admin")
@Secured("ADMIN")
public class AdminController {

    private final AdminService adminService;

    private final UserService userService;


    @Inject
    public AdminController(AdminService adminService ,UserService userService){
        this.adminService = adminService;
        this.userService = userService;


    }
    @Post("/admin-sign-up")
    public HttpResponse<String> createNewAdmin(@Body @Valid LoginRequest request) {
        userService.createNewAdmin(request);
        return HttpResponse.created("Admin Created Successfully");

    }

    @Get("/all-cars")
    public List<Car> findAllCars(){
        return adminService.getAllCars();
    }

    @Get("/booked-cars")
    public List<Car> viewBookedCars(){
        return adminService.getBookedCars();
    }

    @Get("/available-cars")
    public List<Car> viewAvailableCars(){
        return adminService.getAvailableCars();
    }

    @Get("/find-car/{id}")
    public HttpResponse<Car> findById(@PathVariable Long id){
        Optional<Car> optionalCar = adminService.findById(id);
        if(optionalCar.isPresent()){
            return HttpResponse.ok(optionalCar.get());
        }else{
            return HttpResponse.badRequest();
        }
    }

    @Post("/add-car")
    public HttpResponse<?> addCar(@Body @Valid Car car){
          adminService.addCar(car);
          return HttpResponse.created(car );
    }



    @Put("/update-car/{id}")
    public HttpResponse<?> updateCarDetails(@PathVariable Long id , @Body @Valid Car car ){
          try{
              Car newCar = adminService.updateCarDetails(id , car);
              return HttpResponse.ok(newCar) ;
          } catch (NoSuchElementException e) {
              return HttpResponse.notFound();
          }
    }

    @Delete("/delete-car/{id}")
    public HttpResponse<?> deleteCar(@PathVariable Long id){
        try{
            adminService.deleteCar(id);
            return HttpResponse.ok("Car Deleted");
        }catch(NoSuchElementException e){
            return HttpResponse.notFound();
        }
    }




}
