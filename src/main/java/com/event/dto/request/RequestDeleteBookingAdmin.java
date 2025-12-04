package com.event.dto.request;

public class RequestDeleteBookingAdmin {

    private String userName;
    private String eventName;

    public RequestDeleteBookingAdmin(String userName, String eventName) {
        this.userName = userName;
        this.eventName = eventName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEventName() {
        return eventName;
    }

    @Override
    public String toString() {
        return "RequestDeleteBookingAdmin{" +
                "userName='" + userName + '\'' +
                ", eventName='" + eventName + '\'' +
                '}';
    }
}
