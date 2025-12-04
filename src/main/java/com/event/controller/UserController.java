package com.event.controller;

import com.event.dto.request.RequestSearchEventDto;
import com.event.dto.response.ResponseBookingDto;
import com.event.dto.response.ResponseEventDto;
import com.event.dto.response.ResponseUserBookingsDto;
import com.event.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<ResponseUserBookingsDto> getAllBookings(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(service.getAllBookings(token));
    }

    @GetMapping("/getBookingByTitle/{title}")
    public ResponseEntity<ResponseBookingDto> getBookingByTitle(@RequestHeader("Authorization") String token,
                                                                @PathVariable String title){
        return ResponseEntity.ok(service.getBookingByTitle(title, token));
    }

    @PostMapping("/bookUser/{event_name}")
    public ResponseEntity<String> bookUser(@PathVariable String event_name,
                                           @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(service.bookUser(event_name, token));
    }

    @DeleteMapping("/deleteBooking/{event_name}")
    public ResponseEntity<String> deleteBooking(@PathVariable String event_name,
                                                @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(service.deleteBooking(event_name, token));
    }

    @GetMapping("/getAllEvents")
    public ResponseEntity<List<ResponseEventDto>> getBookings(){
         return ResponseEntity.ok(service.getAllEvents());
    }

    @GetMapping("/getEventsBy")
    public ResponseEntity<List<ResponseEventDto>> getEventsBy(@RequestBody RequestSearchEventDto dto){
        return ResponseEntity.ok(service.getEventsBy(dto));
    }
}
