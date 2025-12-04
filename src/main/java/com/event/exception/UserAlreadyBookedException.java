package com.event.exception;

public class UserAlreadyBookedException extends RuntimeException{

    public UserAlreadyBookedException(String message){
        super(message);
    }
}
