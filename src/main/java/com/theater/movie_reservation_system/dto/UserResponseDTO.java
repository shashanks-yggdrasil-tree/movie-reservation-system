package com.theater.movie_reservation_system.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserResponseDTO {
	
	private Long id;
	private String phoneNumber;
	private String email;
	private String firstName;
	private String lastName;
	private LocalDateTime createdAt;
	
	// Default constructor
	public UserResponseDTO() {}
	
	// Parameterized constructor
	public UserResponseDTO(Long id, String phoneNumber, String email, String firstName, String lastName, LocalDateTime createdAt) {
		this.id = id;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
		return "UserResponseDTO{" +
				"id=" + id +
				", phoneNumber='" + phoneNumber + '\'' +
				", email='" + email + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", createdAt=" + createdAt +
				'}';
	}
}