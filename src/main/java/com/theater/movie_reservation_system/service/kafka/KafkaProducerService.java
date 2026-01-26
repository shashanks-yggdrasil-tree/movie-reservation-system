package com.theater.movie_reservation_system.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theater.movie_reservation_system.entity.Movie;
import com.theater.movie_reservation_system.service.MovieService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class KafkaProducerService {
	
	private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
	
	@Value("${kafka.topics.user-actions}")
	private String userActionsTopic;
	
	@Value("${kafka.topics.ai-responses}")
	private String aiResponsesTopic;
	
	private final MovieService movieService;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;
	
	public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate,
	                            ObjectMapper objectMapper,
	                            MovieService movieService
	                            ) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
		this.movieService = movieService;
	}
	
	public void sendUserAction(String sessionId, String actionType, Map<String, Object> data) {
		try {
			// Build the message
			Map<String, Object> message = new HashMap<>();
			message.put("session_id", sessionId);
			message.put("action_type", actionType);
			message.put("timestamp", System.currentTimeMillis());
			message.putAll(data);
			
			String jsonMessage = objectMapper.writeValueAsString(message);
			
			// Send to Kafka
			kafkaTemplate.send(userActionsTopic, sessionId, jsonMessage)
					.whenComplete((result, ex) -> {
						if (ex == null) {
							log.debug("Sent user action to Kafka: {}", jsonMessage);
						} else {
							log.error("Failed to send user action to Kafka", ex);
						}
					});
			
			log.info("Sent {} action for session {}", actionType, sessionId);
			
		} catch (Exception e) {
			log.error("Error sending user action to Kafka", e);
		}
	}
	
	// Movie-specific actions
	public void sendMovieView(String sessionId, Long movieId) {
		Movie movie = movieService.getMovieById(movieId);
		
		Map<String, Object> data = new HashMap<>();
		data.put("movie_id", movieId);
		data.put("movie_name", movie.getMovieName());
		data.put("action_type", "MOVIE_CLICK");
		data.put("page_title", "Movie Details");
//		data.put("page_url", "/movies/" + movieId);
		
		sendUserAction(sessionId, "MOVIE_CLICK", data);
	}
	
	public void sendMovieHover(String sessionId, Long movieId, String movieTitle) {
		Map<String, Object> data = new HashMap<>();
		data.put("movie_id", movieId);
		data.put("movie_title", movieTitle);
		data.put("action_type", "MOVIE_HOVER");
		data.put("page_title", "Movie List");
		data.put("page_url", "/movies");
		
		sendUserAction(sessionId, "MOVIE_HOVER", data);
	}
	
	public void sendSearchAction(String sessionId, String query) {
		Map<String, Object> data = new HashMap<>();
		data.put("search_query", query);
		data.put("action_type", "SEARCH_QUERY");
		data.put("page_title", "Search Results");
		data.put("page_url", "/search?q=" + query);
		
		sendUserAction(sessionId, "SEARCH_QUERY", data);
	}
	
	public void sendSeatSelection(String sessionId, Long showtimeId, List<Integer> seatNumbers) {
		Map<String, Object> data = new HashMap<>();
		data.put("showtime_id", showtimeId);
		data.put("selected_seats", seatNumbers);
		data.put("action_type", "SEAT_SELECTION");
		data.put("page_title", "Seat Selection");
		data.put("page_url", "/booking/seats/" + showtimeId);
		
		sendUserAction(sessionId, "SEAT_SELECTION", data);
	}
	
	// For AI responses coming FROM Python
	public void sendAIResponse(String sessionId, String aiMessage) {
		try {
			Map<String, Object> message = new HashMap<>();
			message.put("session_id", sessionId);
			message.put("message", aiMessage);
			message.put("timestamp", System.currentTimeMillis());
			message.put("source", "paperclip-ai");
			
			String jsonMessage = objectMapper.writeValueAsString(message);
			
			kafkaTemplate.send(aiResponsesTopic, sessionId, jsonMessage)
					.whenComplete(
							(result, ex)-> {
								if (ex == null) {
									log.debug("Sent AI response to Kafka: {}", aiMessage);
								} else {
									log.error("Failed to send AI response to Kafka", ex);
								}
							});
			
		} catch (Exception e) {
			log.error("Error sending AI response to Kafka", e);
		}
	}
}