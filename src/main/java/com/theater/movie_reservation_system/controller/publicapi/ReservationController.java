package com.theater.movie_reservation_system.controller.publicapi;

import com.theater.movie_reservation_system.entity.Reservation;
import com.theater.movie_reservation_system.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
	
	private final ReservationService reservationService;
	
	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}
	
	@PostMapping("/reserve")
	public ResponseEntity<?> reservation(
			@RequestParam Long userId,
			@RequestParam Long showtimeId,
			@RequestParam List<Long> seatIds) {
		
		try {
			Reservation reservation = reservationService.createReservation(userId, showtimeId, seatIds);
			return ResponseEntity.ok(reservation);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/confirm-reservations")
	public ResponseEntity<?> confirmReservation(
			@RequestParam Long reservationId,
			@RequestParam Long userId) {
		
		try {
			Reservation reservation = reservationService.confirmReservation(reservationId, userId);
			return ResponseEntity.ok(reservation);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
}