package com.event.repository.userrepository;

import com.event.entity.Role;
import com.event.entity.User;
import com.event.repository.RoleRepository;
import com.event.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
public class FindByNameTestR {

    @Autowired
    private RoleRepository role_repo;

    @Autowired
    private UserRepository user_repo;

    //test data
    private Role role;
    private User user;

    @BeforeEach
    public void setUp(){
        role = new Role();
        role.setName("USER");
        role_repo.save(role);

        user = new User();
        user.setName("user");
        user_repo.save(user);
    }

    @AfterEach
    public void clean(){
        role = null;
        user = null;
        user_repo.deleteAll();
        role_repo.deleteAll();
    }

    @Test
    @DisplayName("findByName with matching user present, should return user")
    public void findByNamePresent(){
        Optional<User> user_tmp = user_repo.findByName(user.getName());

        assertTrue(user_tmp.isPresent());
        assertEquals(user.getName(), user_tmp.get().getName());
    }

    @Test
    @DisplayName("findByName with no matching user present, should return nothing")
    public void findByNameEmpty(){
        Optional<User> user_tmp = user_repo.findByName("notUser");

        assertTrue(user_tmp.isEmpty());
    }

}
