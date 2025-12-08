package com.event.repository.rolerepository;

import com.event.entity.Role;
import com.event.repository.RoleRepository;
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
public class GetByNameTestR {

    @Autowired
    private RoleRepository role_repo;

    //test data
    private Role role;

    @BeforeEach
    public void setUp(){
        role = new Role();
        role.setName("USER");
        role_repo.save(role);
    }

    @AfterEach
    public void clean(){
        role = null;
        role_repo.deleteAll();
    }

    @Test
    @DisplayName("getByName with matching role present, should return role")
    public void getByNamePresent(){
        Optional<Role> role_tmp = role_repo.getByName(role.getName());

        assertTrue(role_tmp.isPresent());
        assertEquals(role.getName(), role_tmp.get().getName());
    }

    @Test
    @DisplayName("getByName with no matching role present, should return nothing")
    public void getByNameEmpty(){
        Optional<Role> role_tmp = role_repo.getByName("NOTUSER");
        assertTrue(role_tmp.isEmpty());
    }
}
