package com.theater.movie_reservation_system.service;

import com.theater.movie_reservation_system.entity.Auditorium;
import com.theater.movie_reservation_system.entity.AuditoriumSeat;
import com.theater.movie_reservation_system.enums.SeatType;
import com.theater.movie_reservation_system.repository.AuditoriumRepository;
import com.theater.movie_reservation_system.repository.AuditoriumSeatRepository;
import com.theater.movie_reservation_system.repository.TheaterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriumSeatService {
	
	private final AuditoriumSeatRepository auditoriumSeatRepository;
	private final AuditoriumRepository auditoriumRepository;
	private final TheaterRepository theaterRepository;
	
	public AuditoriumSeatService(AuditoriumSeatRepository auditoriumSeatRepository, AuditoriumRepository auditoriumRepository, TheaterRepository theaterRepository) {
		this.auditoriumSeatRepository = auditoriumSeatRepository;
		this.auditoriumRepository = auditoriumRepository;
		this.theaterRepository = theaterRepository;
	}
	
	public AuditoriumSeat createSeat(Long auditoriumId,
	                                 String row_label,
	                                 int seatNumber,
	                                 SeatType seatType) {
		
		Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
				.orElseThrow(() -> new RuntimeException(("Auditorium not found with id: " + auditoriumId)));
		
		AuditoriumSeat auditoriumSeat = new AuditoriumSeat(row_label, seatNumber, seatType,auditorium);
		
		return auditoriumSeatRepository.save(auditoriumSeat);
	}
	
	public List<AuditoriumSeat> getAllSeatsByAuditorium(Long auditoriumId) {
		Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
				.orElseThrow(() -> new RuntimeException(("Auditorium not found with id: " + auditoriumId)));
		return auditoriumSeatRepository.findAllByAuditorium(auditorium);
	}
	
	public List<AuditoriumSeat> getAllSeats() {
		return auditoriumSeatRepository.findAll();
	}
}
