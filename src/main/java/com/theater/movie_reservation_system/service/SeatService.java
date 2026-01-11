package com.theater.movie_reservation_system.service;

import com.theater.movie_reservation_system.entity.Auditorium;
import com.theater.movie_reservation_system.entity.Seat;
import com.theater.movie_reservation_system.enums.SeatType;
import com.theater.movie_reservation_system.repository.AuditoriumRepository;
import com.theater.movie_reservation_system.repository.SeatRepository;
import com.theater.movie_reservation_system.repository.TheaterRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
	
	private final SeatRepository seatRepository;
	private final AuditoriumRepository auditoriumRepository;
	private final TheaterRepository theaterRepository;
	
	public SeatService(SeatRepository seatRepository, AuditoriumRepository auditoriumRepository, TheaterRepository theaterRepository) {
		this.seatRepository = seatRepository;
		this.auditoriumRepository = auditoriumRepository;
		this.theaterRepository = theaterRepository;
	}
	
	public Seat createSeat(Long auditoriumId,
	                       String row_label,
	                       int seatNumber,
	                       SeatType seatType) {
		
		Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
				.orElseThrow(() -> new RuntimeException(("Auditorium not found with id: " + auditoriumId)));
		
		Seat seat = new Seat(row_label, seatNumber, seatType);
		
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
