package com.carrental.exception;

public class EmptyUsernameOrPasswordException extends RuntimeException{
    public EmptyUsernameOrPasswordException(String message){
        super(message);
    }
}
