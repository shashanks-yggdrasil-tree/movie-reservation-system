package com.theater.movie_reservation_system.controller;

import com.theater.movie_reservation_system.dto.LoginRequestDTO;
import com.theater.movie_reservation_system.dto.SignUpRequestDTO;
import com.theater.movie_reservation_system.dto.UserResponseDTO;
import com.theater.movie_reservation_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	// POST /api/users/signup
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid
	                                @RequestBody
	                                SignUpRequestDTO signUpRequest
	) {
		try {
			
			UserResponseDTO createdUser = userService.signUp(signUpRequest);
			
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "User registered successfully");
			response.put("data", createdUser);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
			
		} catch (IllegalArgumentException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", e.getMessage());
			errorResponse.put("error", "VALIDATION_ERROR");
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}
	
	// POST /api/users/login
	@PostMapping("login")
	public ResponseEntity<?> login(@Valid
	                               @RequestBody
	                               LoginRequestDTO loginRequest
	) {
		try {
			UserResponseDTO user = userService.login(loginRequest);
			
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Login successful");
			response.put("data", user);
			
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			
			errorResponse.put("success", false);
			errorResponse.put("message", e.getMessage());
			errorResponse.put("error", "AUTHENTICATION_ERROR");
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
			
		}
	}
	
	
	// GET /api/users/{id} - Optional: Get user by ID
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		
		try {
			UserResponseDTO user = userService.getUserById(id);
			
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("data", user);
			
			return ResponseEntity.ok(response);
			
		} catch (IllegalArgumentException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", e.getMessage());
			errorResponse.put("error", "NOT_FOUND");
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
		
	}
 
}










