package com.theater.movie_reservation_system.service;

import com.theater.movie_reservation_system.entity.Theater;
import com.theater.movie_reservation_system.repository.TheaterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterService {

	public final TheaterRepository theaterRepository;
	
	public TheaterService(TheaterRepository theaterRepository) {
		this.theaterRepository = theaterRepository;
	}
	
	public Theater createTheater(Theater theater) {
		return theaterRepository.save(theater);
	}
	
	public List<Theater> getAllTheaters() {
		return theaterRepository.findAll();
	}
	
	public Theater getTheaterById(Long id) {
		return theaterRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Theater not found with id: " + id));
	}

}
