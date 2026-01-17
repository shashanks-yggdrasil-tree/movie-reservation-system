package com.theater.movie_reservation_system.controller.publicapi;

import com.theater.movie_reservation_system.entity.Reservation;
import com.theater.movie_reservation_system.service.redis.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test/bookings")
public class TestBookingController {
	
	private final BookingService bookingService;
	
	public TestBookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}
	
	@PostMapping("/reserve")
	public ResponseEntity<?> testReservation(
			@RequestParam Long userId,
			@RequestParam Long showtimeId,
			@RequestParam List<Long> seatIds) {
		
		try {
			Reservation reservation = bookingService.createReservation(userId, showtimeId, seatIds);
			return ResponseEntity.ok(reservation);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}