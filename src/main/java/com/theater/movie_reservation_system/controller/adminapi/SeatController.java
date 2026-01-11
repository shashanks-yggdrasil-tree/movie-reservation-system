package com.theater.movie_reservation_system.controller.adminapi;

import com.theater.movie_reservation_system.entity.Seat;
import com.theater.movie_reservation_system.enums.SeatType;
import com.theater.movie_reservation_system.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auditorium-seats")
public class SeatController {
	
	private final SeatService seatService;
	
	public SeatController(SeatService seatService) {
		this.seatService = seatService;
	}
	
	@GetMapping("/by-auditorium/{auditoriumId}")
	public ResponseEntity<List<Seat>> getByAuditorium(@PathVariable Long auditoriumId) {
		return ResponseEntity.ok(seatService.getAllSeatsByAuditorium(auditoriumId));
	}
	
	@GetMapping
	public ResponseEntity<List<Seat>> getAllSeats() {
		return ResponseEntity.ok(seatService.getAllSeats());
	}
	
	@PostMapping
	public  ResponseEntity<Seat> createSeats(
			@RequestParam Long auditoriumId,
			@RequestParam String row_label,
			@RequestParam int seatNumber,
			@RequestParam SeatType seatType
			) {
		Seat seat = seatService.createSeat(auditoriumId, row_label, seatNumber, seatType);
		return ResponseEntity.ok(seat);
	}
	
	
	
}










