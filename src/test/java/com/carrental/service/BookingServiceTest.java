package com.carrental.service;

import com.carrental.dto.BookCarRequest;
import com.carrental.dto.BookCarResponse;
import com.carrental.entity.Car;
import com.carrental.entity.User;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.CarRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    CarRepository carRepository;

    @InjectMocks
    BookingService bookingService;



    @Test
    public void shouldBookCarSuccessfully_WhenCarIsAvailable(){

//      create mock data
        Car car1 = new Car(null , "R8" , "AUDI" , BigDecimal.valueOf(12000), true);
        Long carId = 1L;
        BookCarRequest request = new BookCarRequest(1L ,1L , 9L);
        BookCarResponse expectedResponse = new BookCarResponse(1L , car1.getPricePerDay().multiply(BigDecimal.valueOf(request.getNoOfDays())) , "Car Booked Successfully");

//      when
        when(carRepository.findById(carId)).thenReturn(Optional.of(car1));

        BookCarResponse response = bookingService.bookMyCar(request);

//      assertions
        assertEquals(expectedResponse.getBookingId() , response.getBookingId());
        assertEquals(expectedResponse.getBillAmount() , response.getBillAmount());
        assertEquals(expectedResponse.getMessage() , response.getMessage());
        verify(carRepository, times(1)).findById(carId);

    }

    @Test
    public void shouldFailToBookCar_WhenCarIdDoesNotExist(){

//      create mock data
        Car car1 = new Car(null , "R8" , "AUDI" , BigDecimal.valueOf(12000), true);
        Long carId = 1L;
        BookCarRequest request = new BookCarRequest(1L ,1L , 9L);

        Exception exception = assertThrows(Exception.class,()-> bookingService.bookMyCar(request));

//      assertions
        verify(carRepository, times(1)).findById(carId);
        assertEquals("No Car Found" , exception.getMessage());

    }

    @Test
    public void shouldFailToBookCar_WhenCarIsNotAvailable(){
//      create mock data
        Car car1 = new Car(1L , "R8" , "AUDI" , BigDecimal.valueOf(12000), false);
        Long carId = 1L;
        BookCarRequest request = new BookCarRequest(1L ,1L , 9L);

//      when
        when(carRepository.findById(carId)).thenReturn(Optional.of(car1));

//      assertions
        RuntimeException exception = assertThrows(RuntimeException.class,()-> bookingService.bookMyCar(request));
        assertFalse(car1.isAvailability());
        assertEquals("Car not Available" , exception.getMessage());
        verify(carRepository, times(1)).findById(carId);

    }

    @Test
    public void shouldReturnCarSuccessfully_WhenBookingIsValid(){
//      create mock data
        Car car1 = new Car(1L , "R8" , "AUDI" , BigDecimal.valueOf(12000), false);
        Long carId = 1L;

//      when
        when(carRepository.findById(carId)).thenReturn(Optional.of(car1));
        bookingService.returnCar(carId);

//      assertions
        assertTrue(car1.isAvailability());
        verify(carRepository , times(1)).findById(carId);
        verify(carRepository, times(1)).update(car1);

    }

    @Test
    public void shouldFailToReturnCar_WhenCarIdDoesNotExist(){

//      create mock data
        Car car1 = new Car(1L , "R8" , "AUDI" , BigDecimal.valueOf(12000), false);
        Long carId = 2L;
//      when
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

//      assertions
        assertThrows(NoSuchElementException.class , ()-> bookingService.returnCar(carId) , "Car not found");
        verify(carRepository , times(1)).findById(carId);

    }

    @Test
    public void  shouldFailToReturnCar_WhenCarWasNeverBooked(){

//      create mock data
        Car car1 = new Car(1L , "R8" , "AUDI" , BigDecimal.valueOf(12000), true);
        Long carId = 1L;

//      when
        when(carRepository.findById(carId)).thenReturn(Optional.of(car1));

//      assertions
        assertThrows(RuntimeException.class , ()-> bookingService.returnCar(carId) , "This Car was not booked");
        assertTrue(car1.isAvailability());
        verify(carRepository , times(1)).findById(carId);

    }
}
