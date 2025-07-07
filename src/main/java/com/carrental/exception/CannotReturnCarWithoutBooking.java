package com.carrental.exception;

public class CannotReturnCarWithoutBooking extends RuntimeException{
    public CannotReturnCarWithoutBooking(String message){
        super(message);
    }
}
