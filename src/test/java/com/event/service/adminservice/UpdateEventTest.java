package com.event.service.adminservice;

import com.event.dto.request.RequestUpdateEventDto;
import com.event.entity.Event;
import com.event.exception.EventNotFoundException;
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
public class UpdateEventTest {

    @Mock
    private EventRepository event_repo;

    @Spy
    @InjectMocks
    private AdminService admin_service;

    //test data
    private Event event;
    private RequestUpdateEventDto dto;

    @BeforeEach
    public void setUp(){
        event = new Event();
        event.setId(1);
        event.setTitle("event1");

        dto = new RequestUpdateEventDto(event.getTitle(), "event2", null, 25);
    }

    @AfterEach
    public void clean(){
        event = null;
        dto = null;
    }

    @Test
    @DisplayName("updateEvent should save updated event to database")
    public void updateEventOk(){
        //arrange
        when(event_repo.findByTitle(event.getTitle())).thenReturn(Optional.of(event));

        //act
        String message = admin_service.updateEvent(dto);

        //assert
        assertEquals("event " + dto.getCurrentTitle() + " has been updated", message);
    }

    @Test
    @DisplayName("updateEvent that does not exist, should throw EventNotFoundException")
    public void updateEventException(){
        //arrange
        when(event_repo.findByTitle(event.getTitle())).thenReturn(Optional.empty());

        //assert
        EventNotFoundException exception = assertThrows(EventNotFoundException.class,
                () -> admin_service.updateEvent(dto));
        assertEquals("Event not found, cannot update.", exception.getMessage());
    }
}
