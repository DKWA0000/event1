package com.event.service.userservice;

import com.event.dto.response.ResponseEventDto;
import com.event.entity.Event;
import com.event.repository.EventRepository;
import com.event.service.UserService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllEventsTest {

    @Mock
    private EventRepository event_repo;

    @Spy
    @InjectMocks
    private UserService user_service;

    //test data
    private Event event;
    private ResponseEventDto dto;

    @BeforeEach
    public void setUp(){
        event = new Event();
        event.setId(1);
        event.setTitle("event1");
        event.setMaxParticipants(25);

        dto = new ResponseEventDto(event.getTitle(), null, 25);
    }

    @AfterEach
    public void clean(){
        event = null;
        dto = null;
    }

    @Test
    @DisplayName("getAllEvents with event present, should return event")
    public void getAllEventsPresent(){
        //arrange
        when(event_repo.findAll()).thenReturn(List.of(event));

        //act
        List<ResponseEventDto> dto_tmp = user_service.getAllEvents();

        //assert
        assertEquals(dto.getName(), dto_tmp.get(0).getName());
        assertEquals(dto.getPlacesLeft(), dto_tmp.get(0).getPlacesLeft());
    }

    @Test
    @DisplayName("getAllEvents with no event present should return empty list")
    public void getAllEventsEmpty(){
        //arrange
        when(event_repo.findAll()).thenReturn(List.of());

        //act
        List<ResponseEventDto> dto_tmp = user_service.getAllEvents();

        //assert
        assertEquals(0, dto_tmp.size());
    }
}
