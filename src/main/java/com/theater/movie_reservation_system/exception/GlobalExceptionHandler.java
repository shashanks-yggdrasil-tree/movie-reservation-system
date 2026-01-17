package com.theater.movie_reservation_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Handle validation errors from @Valid
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handle(MethodArgumentNotValidException ex) {
		
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
			
		});
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", false);
		response.put("message", "Validation failed");
		response.put("errors", errors);
		response.put("error", "VALIDATION_ERROR");
		
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		
	}
	
	
	// Handle all other exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", false);
		response.put("message", "An error occurred: " + ex.getMessage());
		response.put("error", "INTERNAL_SERVER_ERROR");
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		
	}
	
	@ExceptionHandler(SeatNotAvailableException.class)
	public ResponseEntity<Map<String, Object>> handleSeatNotAvailable(SeatNotAvailableException ex) {
		Map<String, Object> response = new HashMap<>();
		
		response.put("success", false);
		response.put("message", ex.getMessage());
		response.put("error", "SEAT_NOT_AVAILABLE");
		response.put("timestamp", LocalDateTime.now());
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
}
