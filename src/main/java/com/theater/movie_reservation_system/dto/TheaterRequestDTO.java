package com.theater.movie_reservation_system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TheaterRequestDTO {
	
	private String theaterName;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String zipCode;
	private String phoneNumber;
	private String email;
	private Integer totalScreens;
}
