package com.event.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class RequestCreateEventDto {

    @NotEmpty
    private String title;

    @Future
    private LocalDate eventDate;

    @Positive
    private int maxParticipants;

    public RequestCreateEventDto(String title, LocalDate eventDate, int maxParticipants) {
        this.title = title;
        this.eventDate = eventDate;
        this.maxParticipants = maxParticipants;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    @Override
    public String toString() {
        return "RequestCreateEventDto{" +
                "title='" + title + '\'' +
                ", eventDate=" + eventDate +
                ", maxParticipants=" + maxParticipants +
                '}';
    }
}
