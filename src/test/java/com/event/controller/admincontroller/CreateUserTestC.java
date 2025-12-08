package com.event.controller.admincontroller;

import com.event.dto.request.CreateUserDto;
import com.event.entity.Role;
import com.event.entity.User;
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

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CreateUserTestC {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository role_repo;

    @Autowired
    private UserRepository user_repo;

    //test data
    private Role role;
    private User user;


    @BeforeEach
    public void setUp() {
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
    }

    @Test
    @DisplayName("createUser with unused user name, should return status 200 and message")
    public void createUserOk() throws Exception {
        CreateUserDto dto = new CreateUserDto("user1", "user123",
                "user1@somedomain.com", role_repo.findAll().get(0).getId());

        mockMvc.perform(post("/admins/createUser").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User " + dto.getName() + " has been created"));
    }

    @Test
    @DisplayName("createUser with user name already in use, should return status 409 and message")
    public void createUserOkException409() throws Exception {
        CreateUserDto dto = new CreateUserDto("user", "user123",
                "user@somedomain.com", role_repo.findAll().get(0).getId());

        mockMvc.perform(post("/admins/createUser").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Cannot create new user, username is " +
                                "already in use"));
    }
}
