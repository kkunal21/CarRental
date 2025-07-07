package com.carrental.service;

import com.carrental.dto.BookCarRequest;
import com.carrental.dto.BookCarResponse;
import com.carrental.entity.Booking;
import com.carrental.entity.Car;
import com.carrental.globalException.CannotReturnCarWithoutBooking;
import com.carrental.globalException.CarAlreadyBookedByAnotherUser;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.CarRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

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
        Car car = getAvailableCar(request.getCarId());

        car.setAvailability(false);
        carRepository.update(car);

        BigDecimal billAmount = calculateBill(car, request.getNoOfDays());

        Booking booking = new Booking();
        booking.setCarId(request.getCarId());
        booking.setUserId(request.getUserId());
        booking.setNo_of_days(request.getNoOfDays());
        booking.setBillAmount(billAmount);

        bookingRepository.save(booking);

        return new BookCarResponse(
                booking.getCarId(),
                booking.getBillAmount(),
                "Car Booked Successfully"
        );
    }

    private Car getAvailableCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new NoSuchElementException("No Car Found"));

        if (!car.isAvailability()) {
            throw new CarAlreadyBookedByAnotherUser("Car is booked by some other user, please try booking another car");
        }
        return car;
    }

    private BigDecimal calculateBill(Car car, Long  noOfDays) {
        return car.getPricePerDay().multiply(BigDecimal.valueOf(noOfDays));
    }

    public void returnCar(Long id) {
        Car car = getBookedCarById(id);
        car.setAvailability(true);
        carRepository.update(car);
    }


    private Car getBookedCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Car not found"));

        if (car.isAvailability()) {
            throw new CannotReturnCarWithoutBooking("This car was not booked, so it cannot be returned.");
        }

        return car;
    }

}
