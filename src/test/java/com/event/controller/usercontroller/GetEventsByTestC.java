package com.event.controller.usercontroller;

import com.event.dto.request.RequestSearchEventDto;
import com.event.entity.Event;
import com.event.entity.Role;
import com.event.entity.User;
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

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GetEventsByTestC {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository role_repo;

    @Autowired
    private UserRepository user_repo;

    @Autowired
    private EventRepository event_repo;

    //test data
    private Role role;
    private User user;
    private Event event;

    @BeforeEach
    public void setUp() {
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
        event.setEventDate(LocalDate.parse("2025-11-11"));
        event.setMaxParticipants(25);
        event.setCurrentParticipants(1);
        event_repo.save(event);
    }

    @Test
    @DisplayName("getEventsBy with matching event present, should return status 200 and event")
    public void getEventsByPresent() throws Exception {
        RequestSearchEventDto dto = new RequestSearchEventDto(event.getTitle(),
                null, null,
                event.getMaxParticipants());

        mockMvc.perform(get("/users/getEventsBy").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value(event.getTitle()))
                .andExpect(jsonPath("$.[0].placesLeft")
                        .value(event.getMaxParticipants()));
    }

    @Test
    @DisplayName("getEventsBy with no matching event present, should return status 200 and no events")
    public void getEventsByEmpty() throws Exception {
        RequestSearchEventDto dto = new RequestSearchEventDto("event2",
                LocalDate.now(), LocalDate.now(),
                30);

        mockMvc.perform(get("/users/getEventsBy").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Cannot find matching event"));
    }
}
