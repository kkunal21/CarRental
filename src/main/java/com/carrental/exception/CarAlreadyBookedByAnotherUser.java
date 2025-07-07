package com.carrental.exception;

public class CarAlreadyBookedByAnotherUser extends RuntimeException{
    public CarAlreadyBookedByAnotherUser(String message){
        super(message);
    }
}
