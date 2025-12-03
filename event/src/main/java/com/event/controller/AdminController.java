package com.event.controller;

import com.event.dto.request.CreateUserDto;
import com.event.dto.request.RequestCreateEventDto;
import com.event.dto.request.RequestDeleteBookingAdmin;
import com.event.dto.request.RequestUpdateEventDto;
import com.event.dto.response.ResponseBookingAdminDto;
import com.event.dto.response.ResponseUserDto;
import com.event.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<List<ResponseBookingAdminDto>> getAllBookings(){
        return ResponseEntity.ok(service.getAllBookings());
    }

    @DeleteMapping("/deleteBooking")
    public ResponseEntity<String> deleteSpecificBooking(@RequestBody RequestDeleteBookingAdmin dto){
        return ResponseEntity.ok(service.deleteSpecificBooking(dto));
    }

    @PostMapping("/createEvent")
    public ResponseEntity<String> createEvent(@RequestBody RequestCreateEventDto dto){
        return ResponseEntity.ok(service.createEvent(dto));
    }

    @PatchMapping("/updateEvent")
    public ResponseEntity<String> updateEvent(@RequestBody RequestUpdateEventDto dto){
        return ResponseEntity.ok(service.updateEvent(dto));
    }

    @DeleteMapping("deleteEvent/{event_name}")
    public ResponseEntity<String> deleteEvent(@PathVariable String event_name){
        return ResponseEntity.ok(service.deleteEvent(event_name));
    }

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody CreateUserDto dto){
        return ResponseEntity.ok(service.createUser(dto));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<ResponseUserDto>> getAllUsers(){
        return ResponseEntity.ok(service.getAllUsers());
    }
}
