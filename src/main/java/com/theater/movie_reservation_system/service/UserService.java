package com.theater.movie_reservation_system.service;

import com.theater.movie_reservation_system.dto.LoginRequestDTO;
import com.theater.movie_reservation_system.dto.SignUpRequestDTO;
import com.theater.movie_reservation_system.dto.UserResponseDTO;
import com.theater.movie_reservation_system.entity.User;
import com.theater.movie_reservation_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	// Sign up a new user
	public UserResponseDTO signUp(SignUpRequestDTO signUpRequest) {
		// Check if email already exists
		if (userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
			throw new IllegalArgumentException("Phone number already registered");
		}
		
		// Create new user entity
		User user = new User();
		user.setPhoneNumber(signUpRequest.getPhoneNumber());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(signUpRequest.getPassword()); // In real app, encrypt this!
		user.setFirstName(signUpRequest.getFirstName());
		user.setLastName(signUpRequest.getLastName());
		
		// Save to database
		User savedUser = userRepository.save(user);
		
		// Convert to response DTO
		return convertToDTO(savedUser);
	}
	
	// Login user
	public UserResponseDTO login(LoginRequestDTO loginRequest) {
		// Find user by email
		User user = userRepository.findUserByPhoneNumber(loginRequest.getPhoneNumber())
				.orElseThrow(() -> new IllegalArgumentException("Invalid phone number or password"));
		
		// Check password (plain text for now - we'll add encryption later)
		if (!user.getPassword().equals(loginRequest.getPassword())) {
			throw new IllegalArgumentException("Invalid password");
		}
		
		// Convert to response DTO
		return convertToDTO(user);
	}
	
	// Helper method to convert User entity to UserResponseDTO
	private UserResponseDTO convertToDTO(User user) {
		return new UserResponseDTO(
				user.getId(),
				user.getPhoneNumber(),
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				user.getCreatedAt()
		);
	}
	
	// Get user by ID (optional - for future use)
	public UserResponseDTO getUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		return convertToDTO(user);
	}
}
