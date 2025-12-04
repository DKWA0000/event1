package com.event.service.userservice;

import com.event.config.JwtUtil;
import com.event.dto.response.ResponseBookingDto;
import com.event.entity.Event;
import com.event.entity.Role;
import com.event.entity.User;
import com.event.exception.EventNotFoundException;
import com.event.exception.UserAlreadyBookedException;
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
public class BookUserTest {

    @Mock
    private BookingRepository booking_repo;

    @Mock
    private EventRepository event_repo;

    @Mock
    private UserRepository user_repo;

    @Mock
    private JwtUtil jwtUtil;

    @Spy
    @InjectMocks
    private UserService user_service;

    //test data
    private Role role;
    private User user;
    private Event event;
    private JwtUtil util1;
    private String token;
    private Claims claims;

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

        util1 = new JwtUtil();

        token = util1.generateToken(user.getName(), role.getName());
        claims = util1.extractClaims(token);
    }

    @AfterEach
    public void clean(){
        role = null;
        user = null;
        event = null;
        util1 = null;
        token = null;
        claims = null;
    }

    @Test
    @DisplayName("bookUser should throw EventNotFoundException when trying to book an event that does not exist")
    public void bookUserEventNotFound(){
        //arrange
        when(user_repo.findByName(user.getName())).thenReturn(Optional.of(user));
        when(event_repo.findByTitle(event.getTitle())).thenReturn(Optional.empty());
        when(jwtUtil.extractClaims(token.substring(7))).thenReturn(claims);

        //assert
        EventNotFoundException exception = assertThrows(EventNotFoundException.class,
                () -> user_service.bookUser(event.getTitle(), token));
        assertEquals("Cannot book, event does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("bookUser should throw UserAlreadyBookedException when user is already booked")
    public void bookUserAlreadyBooked(){
        //arrange
        ResponseBookingDto bookingDto = new ResponseBookingDto(event.getTitle(), null, null);

        when(user_repo.findByName(user.getName())).thenReturn(Optional.of(user));
        when(event_repo.findByTitle(event.getTitle())).thenReturn(Optional.of(event));
        when(booking_repo.getSpecificBooking(event.getTitle(), user.getEmail()))
                .thenReturn(Optional.of(bookingDto));
        when(jwtUtil.extractClaims(token.substring(7))).thenReturn(claims);

        //assert
        UserAlreadyBookedException exception = assertThrows(UserAlreadyBookedException.class,
                () -> user_service.bookUser(event.getTitle(), token));
        assertEquals("Cannot book the same event twice", exception.getMessage());
    }

    @Test
    @DisplayName("bookUser should allow user to be booked")
    public void bookUserBooked(){
        //arrange
        when(user_repo.findByName(user.getName())).thenReturn(Optional.of(user));
        when(event_repo.findByTitle(event.getTitle())).thenReturn(Optional.of(event));
        when(booking_repo.getSpecificBooking(event.getTitle(), user.getEmail()))
                .thenReturn(Optional.empty());
        when(jwtUtil.extractClaims(token.substring(7))).thenReturn(claims);

        //act
        String message = user_service.bookUser(event.getTitle(), token);

        //assert
        assertEquals("user " + user.getName() + " is now booked for event " + event.getTitle(),
                message);
    }
}
