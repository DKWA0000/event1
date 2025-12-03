package com.event.service;

import com.event.config.JwtUtil;
import com.event.dto.request.RequestSearchEventDto;
import com.event.dto.response.ResponseBookingDto;
import com.event.dto.response.ResponseEventDto;
import com.event.dto.response.ResponseUserBookingsDto;
import com.event.entity.Booking;
import com.event.entity.Event;
import com.event.entity.User;
import com.event.exception.BookingNotFoundException;
import com.event.exception.EventNotFoundException;
import com.event.exception.UserAlreadyBookedException;
import com.event.repository.BookingRepository;
import com.event.repository.EventRepository;
import com.event.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository user_repo;
    private final BookingRepository booking_repo;
    private final EventRepository event_repo;

    public UserService(JwtUtil jwtUtil, UserRepository userRepo, BookingRepository bookingRepo, EventRepository eventRepo) {
        this.jwtUtil = jwtUtil;
        user_repo = userRepo;
        booking_repo = bookingRepo;
        event_repo = eventRepo;
    }

    public ResponseUserBookingsDto getAllBookings(String token){
        Claims claims = jwtUtil.extractClaims(token.substring(7));
        User user = user_repo.findByName(claims.getSubject()).get();
        return new ResponseUserBookingsDto(user.getName(),
                booking_repo.getUserBookings(user.getEmail()));
    }

    public ResponseBookingDto getBookingByTitle(String eventName, String token){
        Claims claims = jwtUtil.extractClaims(token.substring(7));
        User user = user_repo.findByName(claims.getSubject()).get();
        Optional<ResponseBookingDto> booking_tmp = booking_repo
                .getSpecificBooking(eventName, user.getEmail());
        if(booking_tmp.isPresent()){
            return booking_tmp.get();
        }
        throw new BookingNotFoundException("Cannot find booking");
    }

    public String bookUser(String event_name, String token){
        Claims claims = jwtUtil.extractClaims(token.substring(7));
        User user = user_repo.findByName(claims.getSubject()).get();
        Optional<Event> event_tmp = event_repo.findByTitle(event_name);
        if(event_tmp.isEmpty()){
            throw new EventNotFoundException("Cannot book, event does not exist");
        }
        else if(booking_repo.getSpecificBooking(event_tmp.get().getTitle(), user.getEmail()).isPresent()){
            throw new UserAlreadyBookedException("Cannot book the same event twice");
        }
        else{
            booking_repo.save(new Booking(user, event_tmp.get()));
            event_repo.save(increaseCurrentParticipant(event_name));
            return "user " + user.getName() + " is now booked for event " + event_name;
        }
    }

    private Event increaseCurrentParticipant(String event_name){
        Event event_tmp = event_repo.findByTitle(event_name).get();
        event_tmp.setCurrentParticipants(event_tmp.getCurrentParticipants() + 1);
        return event_tmp;
    }

    public String deleteBooking(String event_name, String token){
        Claims claims = jwtUtil.extractClaims(token.substring(7));
        User user = user_repo.findByName(claims.getSubject()).get();
        Optional<Booking> booking_tmp = booking_repo.getBookingBy(user.getEmail(), event_name);
        if(booking_tmp.isPresent()){
            booking_repo.delete(booking_tmp.get());
            event_repo.save(decreaseCurrentParticipants(event_name));
            return "Booking has been removed";
        }
        throw new BookingNotFoundException("Cannot find the booking to be deleted");
    }

    private Event decreaseCurrentParticipants(String event_name){
        Event event_tmp = event_repo.findByTitle(event_name).get();
        event_tmp.setCurrentParticipants(event_tmp.getCurrentParticipants() - 1);
        return event_tmp;
    }

    public List<ResponseEventDto> getAllEvents(){
        return event_repo.findAll()
                .stream().map(this::convertToEvent).toList();
    }

    private ResponseEventDto convertToEvent(Event event){
        return new ResponseEventDto(event.getTitle(), event.getEventDate(), event.getMaxParticipants());
    }

    private ResponseEventDto toResponseEventDto(Booking booking){
        return new ResponseEventDto(
                booking.getEvent().getTitle(),
                booking.getBookingTime(),
                booking.getEvent().getMaxParticipants() -
                        booking.getEvent().getCurrentParticipants());
    }

    public List<ResponseEventDto> getEventsBy(RequestSearchEventDto dto){
        return event_repo.findAll().stream()
                .filter(event -> (event.getTitle().contains(dto.getTitle())) ||
                        ((event.getEventDate().isAfter(dto.getStartDate())) &&
                        (event.getEventDate().isBefore(dto.getEndDate()))) ||
                        ((event.getMaxParticipants() - event.getCurrentParticipants()) >=
                                dto.getPlacesLeft())).map(this::convertToEvent).toList();
    }
}
