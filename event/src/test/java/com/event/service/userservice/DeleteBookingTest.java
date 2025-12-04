package com.event.service.userservice;

import com.event.config.JwtUtil;
import com.event.dto.response.ResponseBookingDto;
import com.event.entity.Booking;
import com.event.entity.Event;
import com.event.entity.Role;
import com.event.entity.User;
import com.event.exception.BookingNotFoundException;
import com.event.repository.BookingRepository;
import com.event.repository.EventRepository;
import com.event.repository.UserRepository;
import com.event.service.UserService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteBookingTest {

    @Mock
    private UserRepository user_repo;

    @Mock
    private BookingRepository booking_repo;

    @Mock
    private EventRepository event_repo;

    @Mock
    private JwtUtil jwtUtil;

    @Spy
    @InjectMocks
    private UserService user_service;

    //test data
    private Role role;
    private User user;
    private Event event;
    private Booking booking;
    private JwtUtil util1;
    private String token;
    private Claims claims;
    private ResponseBookingDto dto;

    @BeforeEach
    public void setUp(){
        role = new Role();
        role.setId(1);
        role.setName("USER");

        user = new User();
        user.setName("testUser");
        user.setEmail("testUser@somedomain.com");
        user.setRoles(Set.of(role));
        user.setBookings(null);

        event = new Event();
        event.setTitle("event1");

        booking = new Booking();
        booking.setId(1);
        booking.setUser(user);
        booking.setEvent(event);

        dto = new ResponseBookingDto(event.getTitle(), null, null);

        util1 = new JwtUtil();

        token = util1.generateToken(user.getName(), role.getName());
        claims = util1.extractClaims(token);
    }

    @AfterEach
    public void clean(){
        role = null;
        user = null;
        event = null;
        booking = null;
        util1 = null;
        token = null;
        claims = null;
        dto = null;
    }

    @Test
    @DisplayName("deleteBooking when booking is present, should delete booking from database")
    public void deleteBookingOk(){
        //arrange
        when(booking_repo.getBookingBy(user.getEmail(), event.getTitle())).thenReturn(Optional.of(booking));
        when(jwtUtil.extractClaims(token.substring(7))).thenReturn(claims);
        when(user_repo.findByName(user.getName())).thenReturn(Optional.of(user));
        when(event_repo.findByTitle(event.getTitle())).thenReturn(Optional.of(event));

        //act
        String message = user_service.deleteBooking(event.getTitle(), token);

        //assert
        assertEquals("Booking has been removed", message);
    }

    @Test
    @DisplayName("deleteBooking when booking is not present, should throw BookingNotFoundException")
    public void deleteBookingException(){
        //arrange
        when(booking_repo.getBookingBy(user.getEmail(), event.getTitle())).thenReturn(Optional.empty());
        when(jwtUtil.extractClaims(token.substring(7))).thenReturn(claims);
        when(user_repo.findByName(user.getName())).thenReturn(Optional.of(user));

        //assert
        BookingNotFoundException exception = assertThrows(BookingNotFoundException.class,
                () -> user_service.deleteBooking(event.getTitle(), token));
        assertEquals("Cannot find the booking to be deleted", exception.getMessage());
    }
}
