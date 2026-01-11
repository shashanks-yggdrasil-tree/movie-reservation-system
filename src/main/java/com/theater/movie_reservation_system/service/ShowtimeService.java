package com.theater.movie_reservation_system.service;

import com.theater.movie_reservation_system.entity.Auditorium;
import com.theater.movie_reservation_system.entity.Movie;
import com.theater.movie_reservation_system.entity.Showtime;
import com.theater.movie_reservation_system.entity.Theater;
import com.theater.movie_reservation_system.repository.AuditoriumRepository;
import com.theater.movie_reservation_system.repository.MovieRepository;
import com.theater.movie_reservation_system.repository.ShowTimeRepository;
import com.theater.movie_reservation_system.repository.TheaterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowtimeService {

	private final ShowTimeRepository showTimeRepository;
	private final MovieRepository movieRepository;
	private final TheaterRepository theaterRepository;
	private final AuditoriumRepository auditoriumRepository;
	
	public ShowtimeService(ShowTimeRepository showTimeRepository,
	                       MovieRepository movieRepository,
	                       TheaterRepository theaterRepository,
	                       AuditoriumRepository auditoriumRepository) {
		this.showTimeRepository = showTimeRepository;
		this.movieRepository = movieRepository;
		this.theaterRepository = theaterRepository;
		this.auditoriumRepository = auditoriumRepository;
	}
	
	@Transactional
	public Showtime createShowtime(Long movieId, Long theaterId, Long auditoriumId, LocalDateTime showTime, BigDecimal price) {
		
		// Find all related entities first
		Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie Not Found"));
		Theater theater = theaterRepository.findById(theaterId).orElseThrow(() -> new RuntimeException("Theater Not Found"));
		Auditorium auditorium = auditoriumRepository.findById(auditoriumId).orElseThrow(() -> new RuntimeException("Auditorium Not Found"));
		
		// Verify auditorium belongs to the theater
		if (!auditorium.getTheater().getId().equals(theaterId)) {
			throw new RuntimeException("Auditorium does not belongs to the specified theater");
		}
		
		// Create and save showtime
		Showtime showtime = new Showtime(movie, theater, auditorium, showTime, price);
		
		return showTimeRepository.save(showtime);
	}
	
	public List<Showtime> getShowtimesByMovie(Long movieId) {
		return showTimeRepository.findByMovieId(movieId);
	}
	
	public List<Showtime> getShowtimesByTheater(Long theaterId) {
		return showTimeRepository.findByTheaterId(theaterId);
	}
	
	public List<Showtime> getShowtimesByMovieAndDate(Long movieId, LocalDate date) {
		return showTimeRepository.findByMovieIdAndDate(movieId, date);
	}
	
	public List<Showtime> getShowtimesByDate(LocalDate date) {
		return showTimeRepository.findByDate(date);
	}
	
	public List<Showtime> getALlShowtimes() {
		return   showTimeRepository.findAll();
	}

}












