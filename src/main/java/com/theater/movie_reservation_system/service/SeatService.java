package com.theater.movie_reservation_system.service;

import com.theater.movie_reservation_system.entity.Auditorium;
import com.theater.movie_reservation_system.entity.Seat;
import com.theater.movie_reservation_system.entity.Theater;
import com.theater.movie_reservation_system.enums.SeatType;
import com.theater.movie_reservation_system.repository.AuditoriumRepository;
import com.theater.movie_reservation_system.repository.SeatRepository;
import com.theater.movie_reservation_system.repository.TheaterRepository;

import java.util.List;

public class SeatService {

	private final SeatRepository seatRepository;
	private final AuditoriumRepository auditoriumRepository;
	private final TheaterRepository theaterRepository;
	
	public SeatService(SeatRepository seatRepository, AuditoriumRepository auditoriumRepository, TheaterRepository theaterRepository) {
		this.seatRepository = seatRepository;
		this.auditoriumRepository = auditoriumRepository;
		this.theaterRepository = theaterRepository;
	}
	
	public Seat createSeatSections(Long theaterId, Long auditoriumId, String row_label, int seatNumber, SeatType seatType) {
		// 1. Find the Theater and Auditorium first (Parents must exist!)
		Theater theater = theaterRepository.findById(theaterId)
				.orElseThrow(() -> new RuntimeException("Theater not found with id: " + theaterId));
		
		Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
				.orElseThrow(() -> new RuntimeException(("Auditorium not found with id: " + auditoriumId)));
		
		// 2. Create seats with the section
		Seat seat = new Seat(row_label, seatNumber, seatType);
		
		// 3. Save (Foreign key auditorium_id will be set automatically)
		return seatRepository.save(seat);
	}
	
	public List<Seat> getAllSeatsByAuditorium(Long auditoriumId) {
		Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
				.orElseThrow(() -> new RuntimeException(("Auditorium not found with id: " + auditoriumId)));
		return seatRepository.findAllByAuditorium(auditorium);
	}
	
	public List<Seat> getAllSeats() {
		return seatRepository.findAll();
	}
}
