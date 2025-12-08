package com.event.repository.eventrepository;

import com.event.entity.Event;
import com.event.repository.EventRepository;
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
public class FindByTitleTestR {

    @Autowired
    private EventRepository event_repo;

    //test data
    private Event event;

    @BeforeEach
    public void setUp(){
        event = new Event();
        event.setTitle("event1");
        event_repo.save(event);
    }

    @AfterEach
    public void clean(){
        event_repo.deleteAll();
        event = null;
    }

    @Test
    @DisplayName("findByTitle with matching event present, should return event")
    public void findByTitlePresent(){
        Optional<Event> event_tmp = event_repo.findByTitle(event.getTitle());

        assertTrue(event_tmp.isPresent());
        assertEquals(event.getTitle(), event_tmp.get().getTitle());
    }

    @Test
    @DisplayName("findByTitle with no matching event present, should return nothing")
    public void findByTitleEmpty(){
        Optional<Event> event_tmp = event_repo.findByTitle("event2");

        assertTrue(event_tmp.isEmpty());
    }
}
