package com.carrental.controller;

import com.carrental.entity.Car;

import com.carrental.entity.User;
import com.carrental.service.AdminService;
import com.carrental.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller("/admin")
@Secured("ADMIN")
public class AdminController {

    private final AdminService adminService;

    private final UserService userService;;


    @Inject
    public AdminController(AdminService adminService ,UserService userService){
        this.adminService = adminService;
        this.userService = userService;


    }
    @Post("/AdminSignUp")
    public HttpResponse<?> createNewAdmin(@Body User user) {
        userService.createNewAdmin(user);
        return HttpResponse.created("Admin Created");

    }

    @Get("/allCars")
    public List<Car> findAllCars(){
        return adminService.getAllCars();
    }

    @Get("/bookedCars")
    public List<Car> viewBookedCars(){
        return adminService.getBookedCars();
    }

    @Get("/availableCars")
    public List<Car> viewAvailableCars(){
        return adminService.getAvailableCars();
    }

    @Get("/findCar/{id}")
    public HttpResponse<Car> findById(@PathVariable Long id){
        Optional<Car> optionalCar = adminService.findById(id);
        if(optionalCar.isPresent()){
            return HttpResponse.ok(optionalCar.get());
        }else{
            throw new RuntimeException("Car not Found");
        }
    }

    @Post("/AddCar")
    public HttpResponse<?> addCar(@Body Car car){
          adminService.addCar(car);
          return HttpResponse.created(car );
    }



    @Put("/updateCar/{id}")
    public HttpResponse<?> updateCarDetails(@PathVariable Long id , @Body Car car ){
          try{
              Car newCar = adminService.updateCarDetails(id , car);
              return HttpResponse.ok(newCar) ;
          } catch (NoSuchElementException e) {
              return HttpResponse.notFound();
          }
    }

    @Delete("/deleteCar/{id}")
    public HttpResponse<?> deleteCar(@PathVariable Long id){
        try{
            adminService.deleteCar(id);
            return HttpResponse.ok("car deleted");
        }catch(NoSuchElementException e){
            return HttpResponse.notFound();
        }
    }




}
