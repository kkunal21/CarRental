package com.carrental.service;

import com.carrental.dto.BookCarRequest;
import com.carrental.dto.BookCarResponse;
import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.repository.BookingRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class BookingServiceTest {

    @Inject
    BookingService bookingService;

    @Inject
    BookingRepository bookingRepository;

    @Inject
    AdminService adminService;

    @Inject
    UserService userService;

    @Test
    public void bookMyCar(){
        Car car1 = new Car(null , "R8" , "AUDI" , BigDecimal.valueOf(12000), true);
        adminService.addCar(car1);
        User user1 = new User(null , "kunal" , "kunal@123","USER");
        userService.createNewUser(user1);
        BookCarRequest request = new BookCarRequest(car1.getId(), user1.getId() , 7L);

        BookCarResponse response = bookingService.bookMyCar(request);

        assertTrue(bookingRepository.existsById(response.getBookingId()));
        assertEquals(car1.getPricePerDay().multiply(BigDecimal.valueOf(request.getNoOfDays())) , response.getBillAmount());
        assertEquals("Car Booked SuccessFully" , response.getMessage());

    }

    @Test
    public void returnCarTest(){
        Car car1 = new Car(null , "R8" , "AUDI" , BigDecimal.valueOf(12000), false);
        adminService.addCar(car1);
        bookingService.returnCar(car1.getId());

        assertTrue(car1.isAvailability());

    }
}
