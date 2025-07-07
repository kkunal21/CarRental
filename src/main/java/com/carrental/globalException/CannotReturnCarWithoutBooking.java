package com.carrental.globalException;

public class CannotReturnCarWithoutBooking extends RuntimeException{
    public CannotReturnCarWithoutBooking(String message){
        super(message);
    }
}
