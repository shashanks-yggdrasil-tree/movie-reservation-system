package com.theater.movie_reservation_system.mapper;

import com.theater.movie_reservation_system.dto.TheaterRequestDTO;
import com.theater.movie_reservation_system.dto.TheaterResponseDTO;
import com.theater.movie_reservation_system.entity.Theater;

// 🔥 This keeps your controller clean and readable.
public class TheaterMapper {
	
	public static Theater toEntity(TheaterRequestDTO dto) {
		return new Theater(
				dto.getTheaterName(),
				dto.getAddressLine1(),
				dto.getAddressLine2(),
				dto.getCity(),
				dto.getState(),
				dto.getZipCode(),
				dto.getPhoneNumber(),
				dto.getEmail()
		);
	}
	
	public static TheaterResponseDTO toResponse(Theater theater) {
		return new TheaterResponseDTO(
				theater.getId(),
				theater.getTheaterName(),
				theater.getCity(),
				theater.getState(),
				theater.getTotalScreens()
		);
	}
}
