package com.event.repository;

import com.event.dto.response.ResponseBookingDto;
import com.event.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @NativeQuery("SELECT title, booking_time, event_date FROM bookings " +
                    "JOIN events ON (bookings.event_id = events.id) " +
                    "JOIN users ON (bookings.user_id = users.id) " +
                    "WHERE (users.email = ?1)")
    List<ResponseBookingDto> getUserBookings(String userEmail);

    @NativeQuery("SELECT title, booking_time, event_date FROM bookings " +
                    "JOIN events ON (bookings.event_id = events.id) " +
                    "JOIN users ON (bookings.user_id = users.id)" +
                    "WHERE (title = ?1) AND (users.email = ?2)")
    Optional<ResponseBookingDto> getSpecificBooking(String eventName, String email);

    @Query("select b from Booking b " +
            "join User u ON (b.user.id = u.id) " +
            "join Event e ON (b.event.id = e.id) " +
            "where (u.email = :email) and (e.title = :eventName)")
    Optional<Booking> getBookingBy(@Param("email") String email, @Param("eventName") String eventName);
}
