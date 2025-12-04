package com.event.service.adminservice;

import com.event.dto.request.CreateUserDto;
import com.event.entity.Role;
import com.event.entity.User;
import com.event.exception.UserNameAlreadyInUseException;
import com.event.repository.RoleRepository;
import com.event.repository.UserRepository;
import com.event.service.AdminService;
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
public class CreateUserTest {

    @Mock
    private RoleRepository role_repo;

    @Mock
    private UserRepository user_repo;

    @Spy
    @InjectMocks
    private AdminService admin_service;

    //Test data
    private Role role;
    private User user;
    private CreateUserDto dto;

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

        dto = new CreateUserDto("testUser", null,
                "testUser@somedomain.com", 1);
    }

    @AfterEach
    public void cleanData(){
        role = null;
        user = null;
        dto = null;
    }

    @Test
    @DisplayName("createUser should save new user to database")
    public void createUserOk(){
        //arrange
        when(user_repo.findByName("testUser")).thenReturn(Optional.empty());
        when(role_repo.findById(1)).thenReturn(Optional.of(role));
        when(user_repo.save(user)).thenReturn(user);

        //act
        String response = admin_service.createUser(dto);

        //assert
        assertEquals("User " + dto.getName() + " has been created", response);
    }

    @Test
    @DisplayName("createUser with username already in use should throw UserNameAlreadyInUseException")
    public void createUserDuplicate(){
        //arrange
        when(user_repo.findByName("testUser")).thenReturn(Optional.of(user));
        when(role_repo.findById(1)).thenReturn(Optional.of(role));

        //assert
        UserNameAlreadyInUseException exception = assertThrows(UserNameAlreadyInUseException.class,
                () -> admin_service.createUser(dto));
        assertEquals("Cannot create new user, username is " +
                "already in use", exception.getMessage());
    }
}
