package com.event.repository.bookingrepository;

import com.event.dto.response.ResponseBookingDto;
import com.event.entity.Booking;
import com.event.entity.Event;
import com.event.entity.Role;
import com.event.entity.User;
import com.event.repository.BookingRepository;
import com.event.repository.EventRepository;
import com.event.repository.RoleRepository;
import com.event.repository.UserRepository;
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
public class GetSpecificBookingTestR {

    @Autowired
    private RoleRepository role_repo;

    @Autowired
    private UserRepository user_repo;

    @Autowired
    private EventRepository event_repo;

    @Autowired
    private BookingRepository booking_repo;

    //test data
    private Role role;
    private User user;
    private Event event;
    private Booking booking;

    @BeforeEach
    public void setUp(){
        role = new Role();
        role.setName("USER");
        role_repo.save(role);

        user = new User();
        user.setName("user");
        user.setEmail("user@somedomain.com");
        user_repo.save(user);

        event = new Event();
        event.setTitle("event1");
        event_repo.save(event);

        booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking_repo.save(booking);
    }

    @AfterEach
    public void clean(){
        booking = null;
        booking_repo.deleteAll();

        event = null;
        event_repo.deleteAll();

        user = null;
        user_repo.deleteAll();

        role = null;
        role_repo.deleteAll();
    }

    @Test
    @DisplayName("getSpecificBooking with matching booking present, should return booking")
    public void getSpecificBookingPresent(){
        Optional<ResponseBookingDto> booking_tmp = booking_repo
                .getSpecificBooking(event.getTitle(), user.getEmail());

        assertTrue(booking_tmp.isPresent());
        assertEquals(event.getTitle(), booking_tmp.get().getEventName());
    }

    @Test
    @DisplayName("getSpecificBooking with no matching booking present, should return nothing")
    public void getSpecificBookingEmpty(){
        Optional<ResponseBookingDto> booking_tmp = booking_repo
                .getSpecificBooking("notEvent1", "notUser@somedomain.com");
        assertTrue(booking_tmp.isEmpty());
    }
}
