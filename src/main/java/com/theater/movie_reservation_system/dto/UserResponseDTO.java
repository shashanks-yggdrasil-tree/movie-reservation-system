package com.theater.movie_reservation_system.dto;

import java.time.LocalDateTime;

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
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
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