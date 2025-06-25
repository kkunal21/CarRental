package com.carrental.service;

import com.carrental.entity.Car;
import com.carrental.repository.CarRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class AdminServiceTest {

    @Inject
    AdminService adminService;

    @Inject
    CarRepository carRepository;

    @Test
    public void getAllCarsTest(){
        adminService.addCar(new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true));
        adminService.addCar(new Car(null, "R8" , "AUDI" , BigDecimal.valueOf(12000), true));

        List<Car> carList =  adminService.getAllCars();

        assertEquals(2 ,  carList.size());
        assertEquals("BMW" , carList.get(0).getBrand());
        assertEquals("520D" ,carList.get(0).getModel());
        assertEquals(BigDecimal.valueOf(10000) , carList.get(0).getPricePerDay());
        assertEquals("AUDI" , carList.get(1).getBrand());
        assertEquals("R8" ,carList.get(1).getModel());
        assertEquals(BigDecimal.valueOf(12000) , carList.get(1).getPricePerDay());
    }

    @Test
    public void getBookedCarsTest(){

        adminService.addCar(new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true));
        adminService.addCar(new Car(null, "R8" , "AUDI" , BigDecimal.valueOf(12000), false));
        adminService.addCar(new Car(null, "XUV700" , "MAHINDRA" , BigDecimal.valueOf(7000), true));


        List<Car> bookedCars =  adminService.getBookedCars();

        assertEquals(1 , bookedCars.size());
        assertFalse(bookedCars.stream().allMatch(Car::isAvailability));
    }

    @Test
    public void getAvailableCarsTest(){

        adminService.addCar(new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true));
        adminService.addCar(new Car(null, "R8" , "AUDI" , BigDecimal.valueOf(12000), false));
        adminService.addCar(new Car(null, "XUV700" , "MAHINDRA" , BigDecimal.valueOf(7000), true));

        List<Car> availableCars =  adminService.getAvailableCars();

        assertEquals(2 , availableCars.size());
        assertTrue(availableCars.stream().allMatch(Car::isAvailability));
    }

    @Test
    public void addCarTest(){

       Car car1 = new Car(null, "XUV700" , "MAHINDRA" , BigDecimal.valueOf(7000), true);
       Car car2 = new Car(null, "CIVIC" , "HONDA" , BigDecimal.valueOf(7000), true);

        adminService.addCar(car1);
        adminService.addCar(car2);

        assertNotNull(car1.getId() );
        assertTrue(carRepository.existsById(car1.getId()));
        assertNotNull(car2.getId() );
        assertTrue(carRepository.existsById(car2.getId()));
    }

    @Test
    public void findByIdTest(){
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        Car car2 = new Car(null, "R8" , "AUDI" , BigDecimal.valueOf(12000), false);

        adminService.addCar(car1);
        adminService.addCar(car2);

        Long id1 = car1.getId();
        Long id2 = car2.getId();
        Optional<Car> optionalCar = adminService.findById(id1);
        Optional<Car> optionalCar2 = adminService.findById(id2);

        optionalCar.ifPresent(car -> assertEquals(car1, car));
        optionalCar2.ifPresent(car -> assertEquals(car2, car));
    }

    @Test
    public void updateCarDetailsTest(){
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        adminService.addCar(car1);
        Long id = car1.getId();


        Car updatedCar = new Car(null , "R8" , "AUDI" , BigDecimal.valueOf(12000), false);
        adminService.updateCarDetails(id , updatedCar);

        Optional<Car> optionalCar = adminService.findById(id);

        if(optionalCar.isPresent()) {
            Car c1 = optionalCar.get();

            assertEquals( updatedCar.getBrand(),c1.getBrand());
            assertEquals(updatedCar.getModel() , c1.getModel() );
            assertEquals( updatedCar.getPricePerDay() , c1.getPricePerDay());
            assertEquals( updatedCar.isAvailability() , c1.isAvailability());
        }
    }

    @Test
    public void deleteCarTest(){
        Car car1 = new Car( null ,"520D" , "BMW" , BigDecimal.valueOf(10000), true);
        adminService.addCar(car1);
        Long id = car1.getId();

        adminService.deleteCar(id);

        assertFalse(carRepository.existsById(id));
    }

}
