package com.event.service.adminservice;

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
public class DeleteEventTest {

    @Mock
    private EventRepository event_repo;

    @Spy
    @InjectMocks
    private AdminService admin_service;

    //test data
    private Event event;

    @BeforeEach
    public void setUp(){
        event = new Event();
        event.setId(1);
        event.setTitle("event1");
    }

    @AfterEach
    public void clean(){
        event = null;
    }

    @Test
    @DisplayName("deleteEvent should delete event from database")
    public void deleteEventOk(){
        //arrange
        when(event_repo.findByTitle(event.getTitle())).thenReturn(Optional.of(event));

        //act
        String message = admin_service.deleteEvent(event.getTitle());

        //assert
        assertEquals("event " + event.getTitle() + " has been deleted", message);
    }

    @Test
    @DisplayName("deleteEvent when event does not exist, should return EventNotFoundException")
    public void deleteEventException(){
        //arrange
        when(event_repo.findByTitle(event.getTitle())).thenReturn(Optional.empty());

        //assert
        EventNotFoundException exception = assertThrows(EventNotFoundException.class,
                () -> admin_service.deleteEvent(event.getTitle()));
        assertEquals("Cannot delete, event not found", exception.getMessage());
    }
}
