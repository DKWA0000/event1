package com.event.service.adminservice;

import com.event.dto.request.RequestCreateEventDto;
import com.event.entity.Event;
import com.event.exception.EventAlreadyPresentException;
import com.event.repository.EventRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateEventTest{

    @Mock
    private EventRepository event_repo;

    @Spy
    @InjectMocks
    private AdminService admin_service;

    //test data
    private Event event;
    private RequestCreateEventDto dto;

    @BeforeEach
    public void setUp(){
        event = new Event();
        event.setId(1);
        event.setTitle("event1");

        dto = new RequestCreateEventDto(event.getTitle(), null, 25);
    }

    @AfterEach
    public void clean(){
        event = null;
        dto = null;
    }

    @Test
    @DisplayName("createEvent should save event to database")
    public void createEventOk(){
        //arrange
        when(event_repo.findByTitle(event.getTitle())).thenReturn(Optional.empty());

        //act
        String message = admin_service.createEvent(dto);

        //assert
        assertEquals("event " + dto.getTitle() + " has been created.", message);
    }

    @Test
    @DisplayName("create event when event already exist, should throw EventAlreadyPresentException")
    public void createEventException(){
        //arrange
        when(event_repo.findByTitle(event.getTitle())).thenReturn(Optional.of(event));

        //assert
        EventAlreadyPresentException exception = assertThrows(EventAlreadyPresentException.class,
                () -> admin_service.createEvent(dto));
        assertEquals("Event with name "
                + dto.getTitle() + " is already created", exception.getMessage());
    }
}
