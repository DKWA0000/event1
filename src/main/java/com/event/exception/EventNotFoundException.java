package com.event.exception;

public class EventNotFoundException extends RuntimeException{

    public EventNotFoundException(String message){
        super(message);
    }
}
