package com.event.exception;

public class EventAlreadyPresentException extends RuntimeException{

    public EventAlreadyPresentException(String message){
        super(message);
    }
}
