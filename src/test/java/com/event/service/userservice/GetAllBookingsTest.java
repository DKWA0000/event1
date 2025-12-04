package com.event.service.userservice;

import com.event.config.JwtUtil;
import com.event.dto.response.ResponseBookingDto;
import com.event.dto.response.ResponseUserBookingsDto;
import com.event.entity.Booking;
import com.event.entity.Event;
import com.event.entity.Role;
import com.event.entity.User;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllBookingsTest {

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
    private Booking booking;
    private ResponseUserBookingsDto dto;
    private JwtUtil util;
    private String token;
    private Claims claims;

    @BeforeEach
    public void setUp() {
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

        dto = new ResponseUserBookingsDto(user.getName(),
                List.of(new ResponseBookingDto(event.getTitle(), null, null)));

        util = new JwtUtil();

        token = util.generateToken(user.getName(), role.getName());
        claims = util.extractClaims(token);
    }

    @AfterEach
    public void clean(){
        role = null;
        user = null;
        event = null;
        booking = null;
        dto = null;
        util = null;
        token = null;
        claims = null;
    }

    @Test
    @DisplayName("getAllBookings with booking present, should return booking")
    public void getAllBookingsOk(){
        //arrange
        when(jwtUtil.extractClaims(token.substring(7))).thenReturn(claims);
        when(user_repo.findByName(user.getName())).thenReturn(Optional.of(user));
        when(booking_repo.getUserBookings(user.getEmail()))
                .thenReturn(dto.getBookings());

        //List<ResponseBookingDto>

        //act
        ResponseUserBookingsDto dto_tmp = user_service.getAllBookings(token);

        //assert
        assertEquals(dto.getUserName(), dto_tmp.getUserName());
        assertEquals(1, dto_tmp.getBookings().size());
    }

    @Test
    @DisplayName("getAllBookings with no booking present, should return empty")
    public void getAllBookingsEmpty(){
        when(jwtUtil.extractClaims(token.substring(7))).thenReturn(claims);
        when(user_repo.findByName(user.getName())).thenReturn(Optional.of(user));
        when(booking_repo.getUserBookings(user.getEmail()))
                .thenReturn(List.of());

        //act
        ResponseUserBookingsDto dto_tmp = user_service.getAllBookings(token);

        //assert
        assertEquals(dto.getUserName(), dto_tmp.getUserName());
        assertEquals(0, dto_tmp.getBookings().size());
    }
}
