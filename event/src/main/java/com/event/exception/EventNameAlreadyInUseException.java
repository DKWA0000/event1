package com.event.exception;

public class EventNameAlreadyInUseException extends RuntimeException{

    public EventNameAlreadyInUseException(String message){
        super(message);
    }
}
