package com.event.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class RequestSearchEventDto {

    private String title;

    @Future
    private LocalDate startDate;

    @Future
    private LocalDate endDate;

    @Positive
    private int placesLeft;

    public RequestSearchEventDto(String title, LocalDate startDate, LocalDate endDate, int placesLeft) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.placesLeft = placesLeft;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getPlacesLeft() {
        return placesLeft;
    }

    @Override
    public String toString() {
        return "RequestSearchEventDto{" +
                "title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", placesLeft=" + placesLeft +
                '}';
    }
}
