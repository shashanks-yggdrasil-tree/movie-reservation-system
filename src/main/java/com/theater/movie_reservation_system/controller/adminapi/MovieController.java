package com.theater.movie_reservation_system.controller.adminapi;

import com.theater.movie_reservation_system.entity.Movie;
import com.theater.movie_reservation_system.enums.DeleteResult;
import com.theater.movie_reservation_system.service.kafka.KafkaProducerService;
import com.theater.movie_reservation_system.service.MovieService;
import com.theater.movie_reservation_system.service.WebSocketService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController()
@RequestMapping("/api/v1/movies") // We'll use a different path for final API
public class MovieController {
	
	private static final Logger log = LoggerFactory.getLogger(MovieController.class);
	
	private final MovieService movieService;
	private final KafkaProducerService kafkaProducer;
	private final WebSocketService webSocketService;
	
	// Constructor Injection
	public MovieController(MovieService movieService, KafkaProducerService kafkaProducer,WebSocketService webSocketService) {
		this.movieService = movieService;
		this.kafkaProducer = kafkaProducer;
		this.webSocketService = webSocketService;
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
	public ResponseEntity<Movie> getMovieById(@PathVariable Long id, HttpServletRequest request) {
		String sessionId = request.getSession().getId();
		
		// Send to Kafka for AI processing
		kafkaProducer.sendMovieView(sessionId, id);
		
		// Also register WebSocket session
		webSocketService.registerSession(sessionId, "user");
		
		return ResponseEntity.ok(movieService.getMovieById(id));
	}
	
	@PostMapping("/{id}/hover")
	public ResponseEntity<Void> trackMovieHover(@PathVariable Long id,
	                                            @RequestParam String title,
	                                            HttpServletRequest request) {
		String sessionId = request.getSession().getId();
		kafkaProducer.sendMovieHover(sessionId, id, title);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<String>> searchMovies(@RequestParam String query,
	                                                HttpServletRequest request) {
		String sessionId = request.getSession().getId();
		
		// Send search action
		kafkaProducer.sendSearchAction(sessionId, query);
		
		// Return search results
		return ResponseEntity.ok(List.of("movie 1", "movie 2", "movie 3"));
	}
	
	@GetMapping("/ai-test")
	public ResponseEntity<String> testAI(@RequestParam String message,
	                                     HttpServletRequest request) {
		String sessionId = request.getSession().getId();
		
		// Send test message to AI via Kafka
		Map<String, Object> data = new HashMap<>();
		data.put("test_message", message);
		data.put("action_type", "TEST");
		data.put("page_title", "Test Page");
		kafkaProducer.sendUserAction(sessionId, "TEST", data);
		
		return ResponseEntity.ok("AI test message sent for session: " + sessionId);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<DeleteResult> deleteTestMovie(@PathVariable Long id) {
		return ResponseEntity.ok(movieService.deleteMovieById(id));
	}
}













