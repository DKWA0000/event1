package com.event.service.adminservice;

import com.event.dto.response.ResponseUserDto;
import com.event.entity.User;
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

import java.util.List;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllUsersTest {

    @Mock
    private UserRepository user_repo;

    @Spy
    @InjectMocks
    private AdminService admin_service;

    //test data
    private User user;
    private ResponseUserDto dto;

    @BeforeEach
    public void setUp(){
        user = new User();
        user.setId(1);
        user.setName("user");

        dto = new ResponseUserDto("user", "user@somedomain.com", "USER");
    }

    @AfterEach
    public void clean(){
        dto = null;
    }

    @Test
    @DisplayName("getAllUsers with existing user, should return list with user")
    public void getAllUsersOk(){
        //arrange
        lenient().when(user_repo.findAll()).thenReturn(List.of(user));
    }

    @Test
    @DisplayName("getAllUsers with no users, should return empty list")
    public void getAllUsersEmpty(){
        //arrange
        lenient().when(user_repo.findAll()).thenReturn(List.of());
    }
}
