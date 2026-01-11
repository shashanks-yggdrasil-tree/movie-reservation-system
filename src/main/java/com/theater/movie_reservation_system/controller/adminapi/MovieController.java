package com.theater.movie_reservation_system.controller.adminapi;

import com.theater.movie_reservation_system.entity.Movie;
import com.theater.movie_reservation_system.enums.DeleteResult;
import com.theater.movie_reservation_system.service.MovieService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping("/api/v1/movies") // We'll use a different path for final API
public class MovieController {
	
	private final MovieService movieService;
	
	// Constructor Injection
	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}
	
	@GetMapping
	public ResponseEntity<List<Movie>> getAllMovies() {
		return ResponseEntity.ok(movieService.getAllMovies());
	}
	
	@PostMapping
	public ResponseEntity<Movie> createTestMovie(
			@RequestParam String movieName,
			@RequestParam String description,
			@RequestParam String genre,
			@RequestParam Integer durationMinutes,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate releaseDate,
			@RequestParam String language
	) {
	
		Movie movie = new Movie(movieName, description, genre, durationMinutes, releaseDate, language);
		
		Movie savedMovie = movieService.createMovie(movie);
		
		return ResponseEntity.ok(savedMovie);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
		return ResponseEntity.ok(movieService.getMovieById(id));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<DeleteResult> deleteTestMovie(@PathVariable Long id) {
		return ResponseEntity.ok(movieService.deleteMovieById(id));
	}
}













