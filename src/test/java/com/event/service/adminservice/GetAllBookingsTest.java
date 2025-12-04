package com.event.service.adminservice;

import com.event.entity.Booking;
import com.event.entity.Event;
import com.event.entity.User;
import com.event.repository.BookingRepository;
import com.event.service.AdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllBookingsTest {

    @Mock
    private BookingRepository booking_repo;

    @Spy
    @InjectMocks
    private AdminService admin_service;

    //test data
    private User user;
    private Event event;
    private Booking booking;

    @BeforeEach
    public void setUp(){
        user = new User();
        user.setId(1);
        user.setName("user");

        event = new Event();
        event.setId(1);
        event.setTitle("event1");

        booking = new Booking();
        booking.setId(1);
        booking.setUser(user);
        booking.setEvent(event);
    }

    @AfterEach
    public void clean(){
        user = null;
        event = null;
        booking = null;
    }

    @Test
    @DisplayName("getAllBookings with booking should return list with booking")
    public void getAllBookingsOk(){
        //arrange
        lenient().when(booking_repo.findAll()).thenReturn(List.of(booking));
    }

    @Test
    @DisplayName("getAllBookings with no bookings should return empty list")
    public void getAllBookingsEmpty(){
        //arrange
        lenient().when(booking_repo.findAll()).thenReturn(List.of());
    }
}
