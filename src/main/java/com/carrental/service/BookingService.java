package com.carrental.service;

import com.carrental.dto.BookCarRequest;
import com.carrental.dto.BookCarResponse;
import com.carrental.entity.Booking;
import com.carrental.entity.Car;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.CarRepository;
import io.micronaut.http.server.exceptions.NotFoundException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@Singleton
public class BookingService {

    private final CarRepository carRepository;

    private final BookingRepository bookingRepository;


    @Inject
    public BookingService(CarRepository carRepository,BookingRepository bookingRepository){
        this.carRepository = carRepository;
        this.bookingRepository = bookingRepository;

    }


    @Transactional
    public BookCarResponse bookMyCar(BookCarRequest request) {
        Car car  = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new NotFoundException());



            if(car.isAvailability()) {
                car.setAvailability(false);
                carRepository.update(car);
                BigDecimal billAmount =  car.getPricePerDay().multiply(BigDecimal.valueOf(request.getNoOfDays()));

                Booking booking = new Booking();
                booking.setCarId(request.getCarId());
                booking.setUserId(request.getUserId());
                booking.setBillAmount(billAmount);
                booking.setNo_of_days(request.getNoOfDays());

                bookingRepository.save(booking);

                BookCarResponse response = new BookCarResponse();
                response.setBookingId(booking.getId());
                response.setBillAmount(booking.getBillAmount());
                response.setMessage("Car Booked SuccessFully");

                return response;
            }
            else{
                throw new RuntimeException("Car not Available");
            }
    }
}
