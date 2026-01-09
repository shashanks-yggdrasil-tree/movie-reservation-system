package com.theater.movie_reservation_system.controller;

import com.theater.movie_reservation_system.entity.Auditorium;
import com.theater.movie_reservation_system.service.AuditoriumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auditoriums")
public class AuditoriumController {
	
	private final AuditoriumService auditoriumService;
	
	public AuditoriumController(AuditoriumService auditoriumService) {
		this.auditoriumService = auditoriumService;
	}
	
	@PostMapping
	public ResponseEntity<Auditorium> createAuditorium(
			@RequestParam Long theaterId,
			@RequestParam String name,
			@RequestParam Integer capacity
	) {
		
		Auditorium auditorium = auditoriumService.createAuditorium(theaterId, name, capacity);
		return ResponseEntity.ok(auditorium);
	}
	
	@GetMapping("/{theaterId}")
	public ResponseEntity<List<Auditorium>> getByTheater(@PathVariable Long theaterId) {
		return ResponseEntity.ok(auditoriumService.getAuditoriumsByTheater(theaterId));
	}
	
	@GetMapping
	public ResponseEntity<List<Auditorium>> getAll() {
		return ResponseEntity.ok(auditoriumService.getAllAuditoriums());
	}
}