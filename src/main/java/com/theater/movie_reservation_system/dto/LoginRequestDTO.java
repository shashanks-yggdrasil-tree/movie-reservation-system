package com.theater.movie_reservation_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDTO {
	
	// Getters and Setters
	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
	private String phoneNumber;
	
	@NotBlank(message = "Password is required")
	private String password;
	
	// Default constructor
	public LoginRequestDTO() {}
	
	// Parameterized constructor
	public LoginRequestDTO(String phoneNumber, String password) {
		this.phoneNumber = phoneNumber;
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "LoginRequestDTO{" +
				"phoneNumber='" + phoneNumber + '\'' +
				'}';
	}
}