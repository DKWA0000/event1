package com.event.service.userservice;

import com.event.config.JwtUtil;
import com.event.dto.response.ResponseBookingDto;
import com.event.entity.Event;
import com.event.entity.Role;
import com.event.entity.User;
import com.event.exception.BookingNotFoundException;
import com.event.repository.BookingRepository;
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
public class GetBookingByTitleTest {

    @Mock
    private UserRepository user_repo;

    @Mock
    private BookingRepository booking_repo;

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
        util1 = null;
        token = null;
        claims = null;
        dto = null;
    }

    @Test
    @DisplayName("getBookingByTitle should return event1")
    public void getBookingByTitleOk(){
        //arrange
        when(booking_repo.getSpecificBooking(event.getTitle(), user.getEmail())).thenReturn(Optional.of(dto));
        when(jwtUtil.extractClaims(token.substring(7))).thenReturn(claims);
        when(user_repo.findByName(user.getName())).thenReturn(Optional.of(user));

        //act
        ResponseBookingDto dto_tmp = user_service.getBookingByTitle(event.getTitle(), token);

        //assert
        assertEquals(dto, dto_tmp);
    }

    @Test
    @DisplayName("getBookingByTitle when no booking is present, should throw BookingNotFoundException")
    public void getBookingByTitleException(){
        //arrange
        when(booking_repo.getSpecificBooking(event.getTitle(), user.getEmail())).thenReturn(Optional.empty());
        when(jwtUtil.extractClaims(token.substring(7))).thenReturn(claims);
        when(user_repo.findByName(user.getName())).thenReturn(Optional.of(user));

        //assert
        BookingNotFoundException exception = assertThrows(BookingNotFoundException.class,
                () -> user_service.getBookingByTitle(event.getTitle(), token));
        assertEquals("Cannot find booking", exception.getMessage());
    }

}
