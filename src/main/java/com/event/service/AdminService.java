package com.event.service;

import com.event.dto.request.CreateUserDto;
import com.event.dto.request.RequestCreateEventDto;
import com.event.dto.request.RequestDeleteBookingAdmin;
import com.event.dto.request.RequestUpdateEventDto;
import com.event.dto.response.ResponseBookingAdminDto;
import com.event.dto.response.ResponseUserDto;
import com.event.entity.Booking;
import com.event.entity.Event;
import com.event.entity.Role;
import com.event.entity.User;
import com.event.exception.*;
import com.event.repository.BookingRepository;
import com.event.repository.EventRepository;
import com.event.repository.RoleRepository;
import com.event.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {

    private final BookingRepository booking_repo;
    private final EventRepository event_repo;
    private final UserRepository user_repo;
    private final RoleRepository role_repo;

    public AdminService(BookingRepository bookingRepo, EventRepository eventRepo, UserRepository userRepo, RoleRepository roleRepo) {
        booking_repo = bookingRepo;
        event_repo = eventRepo;
        user_repo = userRepo;
        role_repo = roleRepo;
    }

    public List<ResponseBookingAdminDto> getAllBookings(){
        return booking_repo.findAll()
                .stream().map(this::convertToDto).toList();
    }

    private ResponseBookingAdminDto convertToDto(Booking booking){
        return new ResponseBookingAdminDto(
                booking.getUser().getName(),
                booking.getEvent().getTitle(),
                booking.getBookingTime(),
                booking.getEvent().getEventDate()
        );
    }

    public String deleteSpecificBooking(RequestDeleteBookingAdmin dto){
        Optional<Booking> booking_tmp = booking_repo.findAll()
                .stream().filter(b ->
                        (b.getEvent().getTitle().equals(dto.getEventName())) &&
                        (b.getUser().getName().equals(dto.getUserName()))).findFirst();
        if(booking_tmp.isPresent()){
            booking_repo.delete(booking_tmp.get());
            event_repo.save(decreaseParticipants(booking_tmp.get().getEvent().getTitle()));
            return "booking for event " +
                    dto.getEventName() + " for user " +
                    dto.getUserName() + " has been deleted";
        }
        throw new BookingNotFoundException("booking not found, cannot delete");
    }

    private Event decreaseParticipants(String eventName){
        Event event_tmp = event_repo.findByTitle(eventName).get();
        event_tmp.setCurrentParticipants(event_tmp.getCurrentParticipants() - 1);
        return event_tmp;
    }

    public String createEvent(RequestCreateEventDto dto){
        Optional<Event> event_tmp = event_repo.findByTitle(dto.getTitle());
        if(event_tmp.isEmpty()){
            event_repo.save(
                    new Event(dto.getTitle(),
                            dto.getEventDate(),
                            dto.getMaxParticipants()));
            return "event " + dto.getTitle() + " has been created.";
        }
        throw new EventAlreadyPresentException("Event with name "
                + dto.getTitle() + " is already created");
    }

    public String updateEvent(RequestUpdateEventDto dto){
        Optional<Event> event_tmp = event_repo.findByTitle(dto.getCurrentTitle());
        if(event_tmp.isPresent()){
            if((dto.getNewTitle() != null)){
                if(event_repo.findByTitle(dto.getNewTitle()).isPresent()){
                    throw new EventNameAlreadyInUseException("Eventname "
                            + dto.getNewTitle() + " is already in use");
                }
                event_tmp.get().setTitle(dto.getNewTitle());
            }
            if(dto.getEventDate() != null){
                event_tmp.get().setEventDate(dto.getEventDate());
            }
            if(dto.getMaxParticipants() != 0){
                event_tmp.get().setMaxParticipants(dto.getMaxParticipants());
            }
            event_repo.save(event_tmp.get());
            return "event " + dto.getCurrentTitle() + " has been updated";
        }
        throw new EventNotFoundException("Event not found, cannot update.");
    }

    public String deleteEvent(String eventName){
        Optional<Event> event_tmp = event_repo.findByTitle(eventName);
        if(event_tmp.isPresent()){
            event_repo.delete(event_tmp.get());
            return "event " + eventName + " has been deleted";
        }
        throw new EventNotFoundException("Cannot delete, event not found");
    }

    public String createUser(CreateUserDto dto){
        Optional<User> user_tmp = user_repo.findByName(dto.getName());
        Set<Role> roles = new HashSet<>();
        roles.add(role_repo.findById(dto.getRole()).get());
        if(user_tmp.isEmpty()){
            user_repo.save(new User(
                    dto.getName(),
                    new BCryptPasswordEncoder().encode(dto.getPassword()),
                    dto.getEmail(),
                    roles));


            return "User " + dto.getName() + " has been created";
        }
        throw new UserNameAlreadyInUseException("Cannot create new user, username is " +
                "already in use");
    }

    public List<ResponseUserDto> getAllUsers(){
        return user_repo.findAll().stream()
                .map(this::convertFromUser).toList();
    }

    public ResponseUserDto convertFromUser(User user){
        return new ResponseUserDto(user.getName(), user.getEmail(),
                user.getRoles().stream().findFirst().get().getName());
    }
}
