package com.event.service.adminservice;

import com.event.dto.request.RequestDeleteBookingAdmin;
import com.event.entity.Booking;
import com.event.entity.Event;
import com.event.entity.User;
import com.event.exception.BookingNotFoundException;
import com.event.repository.BookingRepository;
import com.event.repository.EventRepository;
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
import org.mockito.stubbing.OngoingStubbing;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteSpecificBookingTest {

    @Mock
    private BookingRepository booking_repo;

    @Mock
    private EventRepository event_repo;

    @Spy
    @InjectMocks
    private AdminService admin_service;

    //Test data
    private User user;
    private Event event;
    private Booking booking;
    private RequestDeleteBookingAdmin dto;

    @BeforeEach
    public void setUp(){
        user = new User();
        user.setName("user");

        event = new Event();
        event.setTitle("event1");

        booking = new Booking();
        booking.setId(1);
        booking.setEvent(event);
        booking.setUser(user);
        booking.setBookingTime(null);

        dto = new RequestDeleteBookingAdmin(user.getName(), event.getTitle());
    }

    @AfterEach
    public void clean(){
        user = null;
        event = null;
        booking = null;
        dto = null;
    }

    @Test
    @DisplayName("deleteSpecificBooking should delete specific booking from database")
    public void deleteSpecificBookingOk(){
        //arrange
        when(booking_repo.findAll()).thenReturn(List.of(booking));
        when(event_repo.save(event)).thenReturn(event);
        when(event_repo.findByTitle(event.getTitle())).thenReturn(Optional.ofNullable(event));

        //act
        String message = admin_service.deleteSpecificBooking(dto);

        //assert
        assertEquals("booking for event " +
                dto.getEventName() + " for user " +
                dto.getUserName() + " has been deleted", message);
    }

    @Test
    @DisplayName("deleteSpecificBooking when no booking, should return BookingNotFoundException")
    public void deleteSpecificBookingException(){
        //arrange
        when(booking_repo.findAll()).thenReturn(List.of());

        //assert
        BookingNotFoundException exception = assertThrows(BookingNotFoundException.class,
                () -> admin_service.deleteSpecificBooking(dto));
        assertEquals("booking not found, cannot delete", exception.getMessage());
    }
}
