package com.event.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class RequestUpdateEventDto {

    @NotEmpty
    private String currentTitle;

    private String newTitle;

    @Future
    private LocalDate eventDate;

    @Positive
    private int maxParticipants;

    public RequestUpdateEventDto(String currentTitle, String newTitle, LocalDate eventDate, int maxParticipants) {
        this.currentTitle = currentTitle;
        this.newTitle = newTitle;
        this.eventDate = eventDate;
        this.maxParticipants = maxParticipants;
    }

    public String getCurrentTitle() {
        return currentTitle;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    @Override
    public String toString() {
        return "RequestUpdateEventDto{" +
                "currentTitle='" + currentTitle + '\'' +
                ", newTitle='" + newTitle + '\'' +
                ", eventDate=" + eventDate +
                ", maxParticipants=" + maxParticipants +
                '}';
    }
}
