package com.carrental.globalException;

public class CarAlreadyBookedByAnotherUser extends RuntimeException{
    public CarAlreadyBookedByAnotherUser(String message){
        super(message);
    }
}
