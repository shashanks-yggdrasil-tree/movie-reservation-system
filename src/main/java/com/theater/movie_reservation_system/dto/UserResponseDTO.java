package com.theater.movie_reservation_system.dto;

import java.time.LocalDateTime;

public class UserResponseDTO {
	
	private Long id;
	private String email;
	private String fullName;
	private String phone;
	private LocalDateTime createdAt;
	
	// Default constructor
	public UserResponseDTO() {}
	
	// Parameterized constructor
	public UserResponseDTO(Long id, String email, String fullName, String phone, LocalDateTime createdAt) {
		this.id = id;
		this.email = email;
		this.fullName = fullName;
		this.phone = phone;
		this.createdAt = createdAt;
	}
	
	// Getters and Setters
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
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
				", email='" + email + '\'' +
				", fullName='" + fullName + '\'' +
				", phone='" + phone + '\'' +
				", createdAt=" + createdAt +
				'}';
	}
}