package com.event.controller.admincontroller;

import com.event.dto.request.RequestDeleteBookingAdmin;
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
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Set;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class DeleteBookingTestC {

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

    //test data
    private Role role;
    private User user;
    private Event event;
    private Booking booking;

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
    }

    @Test
    @DisplayName("deleteBooking with one booking present, should return status 200 and message")
    public void deleteBookingOk() throws Exception {
        RequestDeleteBookingAdmin dto = new RequestDeleteBookingAdmin("user", "event1");

        mockMvc.perform(delete("/admins/deleteBooking").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("booking for event " +
                        dto.getEventName() + " for user " +
                        dto.getUserName() + " has been deleted"));
    }

    @Test
    @DisplayName("deleteBooking with no booking present, should return status 404 and message")
    public void deleteBookingException() throws Exception {
        RequestDeleteBookingAdmin dto = new RequestDeleteBookingAdmin("user1", "event2");

        mockMvc.perform(delete("/admins/deleteBooking").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("booking not found, cannot delete"));
    }
}
