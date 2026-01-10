package com.theater.movie_reservation_system.service;

import com.theater.movie_reservation_system.entity.Auditorium;
import com.theater.movie_reservation_system.entity.Theater;
import com.theater.movie_reservation_system.repository.AuditoriumRepository;
import com.theater.movie_reservation_system.repository.TheaterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriumService {
	
	private final AuditoriumRepository auditoriumRepository;
	private final TheaterRepository theaterRepository;
	
	public AuditoriumService(AuditoriumRepository auditoriumRepository,
	                         TheaterRepository theaterRepository) {
		this.auditoriumRepository = auditoriumRepository;
		this.theaterRepository = theaterRepository;
	}
	
	public Auditorium createAuditorium(Long theaterId, String name, Integer capacity) {
		// 1. Find the Theater first (Parent must exist!)
		Theater theater = theaterRepository.findById(theaterId)
				.orElseThrow(() -> new RuntimeException("Theater not found with id: " + theaterId));
		
		// 2. Create Auditorium with the Theater
		Auditorium auditorium = new Auditorium(name, capacity, theater);
		
		// 3. Save (Foreign key theater_id will be set automatically)
		return auditoriumRepository.save(auditorium);
	}
	
	public List<Auditorium> getAuditoriumsByTheater(Long theaterId) {
		Theater theater = theaterRepository.findById(theaterId)
				.orElseThrow(() -> new RuntimeException("Theater not found with id: " + theaterId));
		
		return auditoriumRepository.findByTheaterId(theaterId);
	}

	
	public List<Auditorium> getAllAuditoriums() {
		return auditoriumRepository.findAll();
	}
}