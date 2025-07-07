package com.carrental.globalException;

public class EmptyUsernameOrPasswordException extends RuntimeException{
    public EmptyUsernameOrPasswordException(String message){
        super(message);
    }
}
