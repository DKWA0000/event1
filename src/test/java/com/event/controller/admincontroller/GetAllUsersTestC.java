package com.event.controller.admincontroller;

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

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GetAllUsersTestC {

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
    @DisplayName("getAllUsers with user present, should return status 200 and the user")
    public void getAllUsersPresent() throws Exception {
        mockMvc.perform(get("/admins/getAllUsers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("user"))
                .andExpect(jsonPath("$.[0].email").value("user@somedomain.com"))
                .andExpect(jsonPath("$.[0].role").value("USER"));
    }

    @Test
    @DisplayName("getAllUsers with no user present, should return status 200 and no user")
    public void getAllUsersEmpty() throws Exception {
        user_repo.deleteAll();

        mockMvc.perform(get("/admins/getAllUsers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
