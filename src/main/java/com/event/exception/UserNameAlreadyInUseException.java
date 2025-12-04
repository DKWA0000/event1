package com.event.exception;

public class UserNameAlreadyInUseException extends RuntimeException{

    public UserNameAlreadyInUseException(String message){
        super(message);
    }
}
