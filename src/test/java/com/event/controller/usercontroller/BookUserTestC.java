package com.event.controller.usercontroller;

import com.event.config.JwtUtil;
import com.event.entity.Booking;
import com.event.entity.Event;
import com.event.entity.Role;
import com.event.entity.User;
import com.event.repository.BookingRepository;
import com.event.repository.EventRepository;
import com.event.repository.RoleRepository;
import com.event.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookUserTestC {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository role_repo;

    @Autowired
    private UserRepository user_repo;

    @Autowired
    private EventRepository event_repo;

    @Autowired
    private BookingRepository booking_repo;

    @Autowired
    private JwtUtil util;

    //test data
    private Role role;
    private User user;
    private Event event;
    private Booking booking;
    private String token;

    @BeforeEach
    public void setUp(){
        booking_repo.deleteAll();
        event_repo.deleteAll();
        user_repo.deleteAll();
        role_repo.deleteAll();

        role = new Role();
        role.setName("USER");
        role_repo.save(role);

        user = new User();
        user.setName("user");
        user.setEmail("user@somedomain.com");
        user.setRoles(Set.of(role));
        user_repo.save(user);

        event = new Event();
        event.setTitle("event1");
        event.setMaxParticipants(25);
        event.setCurrentParticipants(1);
        event_repo.save(event);

        booking = new Booking();
        booking.setEvent(event);
        booking.setUser(user);
        booking.setBookingTime(LocalDate.now());
        booking_repo.save(booking);

        token = util.generateToken(user.getName(), role.getName());
    }

    @Test
    @DisplayName("bookUser with no booking present for current user, should return status 200 and message")
    public void bookUserOk() throws Exception {
        booking_repo.deleteAll();

        mockMvc.perform(post("/users/bookUser/" + event.getTitle())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("user " + user.getName() +
                                " is now booked for event " + event.getTitle()));
    }

    @Test
    @DisplayName("bookUser with no event present, should return status 404 and message")
    public void bookUserException404() throws Exception {
        booking_repo.deleteAll();
        event_repo.deleteAll();

        mockMvc.perform(post("/users/bookUser/" + event.getTitle())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Cannot book, event does not exist"));
    }

    @Test
    @DisplayName("bookUser with booking already present, should return status 409 and message")
    public void bookUserException409() throws Exception {
        mockMvc.perform(post("/users/bookUser/" + event.getTitle())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "bearer " + token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Cannot book the same event twice"));
    }
}
