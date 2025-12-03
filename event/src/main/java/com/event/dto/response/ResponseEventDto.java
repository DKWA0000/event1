package com.event.dto.response;

import java.time.LocalDate;

public class ResponseEventDto {

    private String name;
    private LocalDate date;
    private int placesLeft;

    public ResponseEventDto(String name, LocalDate date, int placesLeft) {
        this.name = name;
        this.date = date;
        this.placesLeft = placesLeft;
    }

    public ResponseEventDto(){}

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getPlacesLeft() {
        return placesLeft;
    }

    @Override
    public String toString() {
        return "ResponseEventDto{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", placesLeft=" + placesLeft +
                '}';
    }
}
