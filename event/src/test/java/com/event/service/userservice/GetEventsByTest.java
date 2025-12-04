package com.event.service.userservice;

import com.event.dto.request.RequestSearchEventDto;
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
public class GetEventsByTest {

    @Mock
    private EventRepository event_repo;

    @Spy
    @InjectMocks
    private UserService user_service;

    //test data
    private Event event;
    private RequestSearchEventDto request_dto;
    private ResponseEventDto response_dto;

    @BeforeEach
    public void setUp(){
        event = new Event();
        event.setId(1);
        event.setTitle("event1");
        event.setMaxParticipants(25);

        request_dto = new RequestSearchEventDto(event.getTitle(), null, null, 25);

        response_dto = new ResponseEventDto(event.getTitle(), null, 25);
    }

    @AfterEach
    public void clean(){
        event = null;
        request_dto = null;
        response_dto = null;
    }

    @Test
    @DisplayName("getEventsBy with event present, should return event")
    public void getEventsByPresent(){
        //arrange
        when(event_repo.findAll()).thenReturn(List.of(event));

        //act
        List<ResponseEventDto> dto_tmp = user_service.getEventsBy(request_dto);

        //assert
        assertEquals(dto_tmp.get(0).getName(), response_dto.getName());
        assertEquals(dto_tmp.get(0).getPlacesLeft(), response_dto.getPlacesLeft());
    }

    @Test
    @DisplayName("getEventsBy with no events present, should return empty")
    public void getEventsByEmpty(){
        //arrange
        when(event_repo.findAll()).thenReturn(List.of());

        //act
        List<ResponseEventDto> dto_tmp = user_service.getEventsBy(request_dto);

        //assert
        assertEquals(0, dto_tmp.size());
    }
}
