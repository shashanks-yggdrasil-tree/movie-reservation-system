package com.theater.movie_reservation_system.controller;

import com.theater.movie_reservation_system.entity.Theater;
import com.theater.movie_reservation_system.service.TheaterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theaters")
public class TheaterController {
	
	private final TheaterService theaterService;
	
	public TheaterController(TheaterService theaterService) {
		this.theaterService = theaterService;
	}
	
	@PostMapping
	public ResponseEntity<Theater> createTestTheater(
			@RequestParam String name,
			@RequestParam String addressLine1,
			@RequestParam String city,
			@RequestParam String state) {
		
		Theater theater = new Theater(name, addressLine1, city, state);
		Theater savedTheater = theaterService.createTheater(theater);
		return ResponseEntity.ok(savedTheater);
	}
	
	@GetMapping
	public ResponseEntity<List<Theater>> getAllTheaters() {
		return ResponseEntity.ok(theaterService.getAllTheaters());
	}
}