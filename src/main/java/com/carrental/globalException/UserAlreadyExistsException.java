package com.carrental.globalException;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message ){
        super(message);
    }
}
