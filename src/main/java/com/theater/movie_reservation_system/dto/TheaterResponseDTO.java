package com.theater.movie_reservation_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TheaterResponseDTO {
	
	private Long id;
	private String theaterName;
	private String city;
	private String state;
	private Integer totalScreens;
}

