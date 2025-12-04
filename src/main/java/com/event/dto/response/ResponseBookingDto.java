package com.event.dto.response;

import java.time.LocalDate;

public class ResponseBookingDto {

    private String eventName;
    private LocalDate bookingDate;
    private LocalDate eventDate;

    public ResponseBookingDto(String eventName, LocalDate bookingDate, LocalDate eventDate) {
        this.eventName = eventName;
        this.bookingDate = bookingDate;
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    @Override
    public String toString() {
        return "ResponseBookingDto{" +
                "eventName='" + eventName + '\'' +
                ", bookingDate=" + bookingDate +
                ", eventDate=" + eventDate +
                '}';
    }
}
