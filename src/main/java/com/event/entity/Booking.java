package com.event.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate bookingTime;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "eventId")
    @JsonManagedReference
    private Event event;

    public Booking() {
    }

    public Booking(int id, LocalDate bookingTime, User user, Event event) {
        this.id = id;
        this.bookingTime = bookingTime;
        this.user = user;
        this.event = event;
    }

    public Booking(User user, Event event){
        this.bookingTime = LocalDate.now();
        this.user = user;
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDate bookingTime) {
        this.bookingTime = bookingTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingTime=" + bookingTime +
                ", user=" + user +
                ", event=" + event +
                '}';
    }
}
