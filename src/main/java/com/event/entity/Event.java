package com.event.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String title;
    private LocalDate eventDate;
    private int maxParticipants;
    private int currentParticipants;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Booking> bookings;

    public Event() {
    }

    public Event(int id, String title, LocalDate eventDate, int maxParticipants) {
        this.id = id;
        this.title = title;
        this.eventDate = eventDate;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = 0;
    }

    public Event(String title, LocalDate eventDate, int maxParticipants) {
        this.title = title;
        this.eventDate = eventDate;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
