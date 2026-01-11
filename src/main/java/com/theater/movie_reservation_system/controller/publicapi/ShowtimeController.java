package com.theater.movie_reservation_system.controller.publicapi;

import com.theater.movie_reservation_system.entity.Showtime;
import com.theater.movie_reservation_system.service.ShowtimeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/showtimes")
public class ShowtimeController {
	
	private final ShowtimeService showtimeService;
	
	public ShowtimeController(ShowtimeService showtimeService) {
		this.showtimeService = showtimeService;
	}
	
	@PostMapping
	public ResponseEntity<Showtime> createShowTime(
			@RequestParam Long movieId,
			@RequestParam Long theaterId,
			@RequestParam Long auditoriumId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime showTime,
			@RequestParam BigDecimal price
	) {
		Showtime showtime = showtimeService.createShowtime(movieId, theaterId, auditoriumId, showTime, price);
		return ResponseEntity.ok(showtime);
	}
	
	@GetMapping("/by-movie/{movieId}")
	public ResponseEntity<List<Showtime>> getByMovieId(@PathVariable Long movieId) {
		return ResponseEntity.ok(showtimeService.getShowtimesByMovie(movieId));
	}
	
	@GetMapping("/by-date")
	public ResponseEntity<List<Showtime>> getByDate(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDate date
			) {
		return ResponseEntity.ok(showtimeService.getShowtimesByDate(date));
	}
	
	@GetMapping
	public ResponseEntity<List<Showtime>> getAll() {
		return ResponseEntity.ok(showtimeService.getALlShowtimes());
	}
	
}



















