package com.event.dto.response;

import java.time.LocalDate;

public class ResponseBookingAdminDto {

    private String userName;
    private String eventName;
    private LocalDate bookingDate;
    private LocalDate eventDate;

    public ResponseBookingAdminDto(String userName, String eventName, LocalDate bookingDate, LocalDate eventDate) {
        this.userName = userName;
        this.eventName = eventName;
        this.bookingDate = bookingDate;
        this.eventDate = eventDate;
    }

    public String getUserName() {
        return userName;
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
        return "ResponseBookingAdminDto{" +
                "userName='" + userName + '\'' +
                ", eventName='" + eventName + '\'' +
                ", bookingDate=" + bookingDate +
                ", eventDate=" + eventDate +
                '}';
    }
}
