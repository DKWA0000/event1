package com.event.service;

import com.event.dto.request.CreateUserDto;
import com.event.entity.Role;
import com.event.entity.User;
import com.event.repository.BookingRepository;
import com.event.repository.EventRepository;
import com.event.repository.RoleRepository;
import com.event.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password4j.BcryptPassword4jPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private BookingRepository booking_repo;

    @Mock
    private EventRepository event_repo;

    @Mock
    private RoleRepository role_repo;

    @Mock
    private UserRepository user_repo;

    @Spy
    @InjectMocks
    private AdminService admin_service;

    @Test
    @DisplayName("createUser should save new user to database")
    public void createUser(){
        //arrange
        Role role = new Role();
        role.setId(1);
        role.setName("USER");

        User user = new User();
        user.setName("testUser");
        user.setEmail("testUser@somedomain.com");
        user.setRoles(Set.of(role));
        user.setBookings(null);

        CreateUserDto dto = new CreateUserDto("testUser", null,
                "testUser@somedomain.com", 1);

        when(user_repo.findByName(user.getName())).thenReturn(Optional.empty());
        when(role_repo.findById(1)).thenReturn(Optional.of(role));
        when(user_repo.save(user)).thenReturn(user);

        //act
        String response = admin_service.createUser(dto);

        //assert
        verify(user_repo, times(0)).save(user);
    }
}
