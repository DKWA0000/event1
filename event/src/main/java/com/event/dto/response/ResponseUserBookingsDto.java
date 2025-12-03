package com.event.dto.response;

import java.util.List;

public class ResponseUserBookingsDto {

    private String userName;
    private List<ResponseBookingDto> bookings;

    public ResponseUserBookingsDto(String userName, List<ResponseBookingDto> bookings) {
        this.userName = userName;
        this.bookings = bookings;
    }

    public String getUserName() {
        return userName;
    }

    public List<ResponseBookingDto> getBookings() {
        return bookings;
    }

    @Override
    public String toString() {
        return "ResponseUserBookingsDto{" +
                "userName='" + userName + '\'' +
                ", bookings=" + bookings +
                '}';
    }
}
