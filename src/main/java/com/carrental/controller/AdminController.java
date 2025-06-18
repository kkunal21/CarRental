package com.carrental.controller;

import com.carrental.entity.Car;
import com.carrental.service.AdminService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.List;

@Controller("/admin")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @Get("/allCars")
    public List<Car> findAllCars(){
        return adminService.getAllCars();
    }

    @Get("/bookedCars")
    public List<Car> viewBookedCars(){
        return adminService.getBookedCars();
    }

    @Post("/AddCar")
    public HttpResponse<Car> addCar(@Body Car car){
          adminService.addCar(car);
          return HttpResponse.created(car );
    }

//    @Put("/updateCar")
//    public HttpResponse<?> updateCarDetails(){
//
//    }


}
