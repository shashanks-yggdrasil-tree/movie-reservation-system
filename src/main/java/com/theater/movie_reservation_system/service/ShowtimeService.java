package com.theater.movie_reservation_system.service;

import com.theater.movie_reservation_system.entity.*;
import com.theater.movie_reservation_system.enums.SeatStatus;
import com.theater.movie_reservation_system.enums.SeatType;
import com.theater.movie_reservation_system.repository.*;
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
	private final AuditoriumSeatRepository auditoriumSeatRepository;
	private final ShowtimeSeatRepository showtimeSeatRepository;
	private final AuditoriumRepository auditoriumRepository;
	
	public ShowtimeService(ShowTimeRepository showTimeRepository,
	                       MovieRepository movieRepository,
	                       TheaterRepository theaterRepository,
						   AuditoriumSeatRepository auditoriumSeatRepository,
	                       ShowtimeSeatRepository showtimeSeatRepository,
	                       AuditoriumRepository auditoriumRepository) {
		this.showTimeRepository = showTimeRepository;
		this.movieRepository = movieRepository;
		this.auditoriumSeatRepository = auditoriumSeatRepository;
		this.theaterRepository = theaterRepository;
		this.showtimeSeatRepository = showtimeSeatRepository;
		this.auditoriumRepository = auditoriumRepository;
	}
	
	@Transactional
	public Showtime createShowtime(Long movieId, Long theaterId, Long auditoriumId, LocalDateTime showTime, BigDecimal basePrice) {
		
		// Find all related entities first
		Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie Not Found"));
		Theater theater = theaterRepository.findById(theaterId).orElseThrow(() -> new RuntimeException("Theater Not Found"));
		Auditorium auditorium = auditoriumRepository.findById(auditoriumId).orElseThrow(() -> new RuntimeException("Auditorium Not Found"));
		
		// Verify auditorium belongs to the theater
		if (!auditorium.getTheater().getId().equals(theaterId)) {
			throw new RuntimeException("Auditorium does not belongs to the specified theater");
		}
		
		// Create and save showtime
		Showtime showtime = new Showtime(movie, theater, auditorium, showTime);
		Showtime savedShowtime = showTimeRepository.save(showtime);
		
		// 🔴 CRITICAL: Create ShowtimeSeat records for ALL seats in auditorium
		createShowtimeSeatsForAuditorium(savedShowtime, auditorium, basePrice);
		
		return savedShowtime;
		
//		return showTimeRepository.save(showtime);
	}
	private void createShowtimeSeatsForAuditorium(Showtime showtime, Auditorium auditorium, BigDecimal basePrice) {
		// 1. Get all seats for this auditorium
		List<AuditoriumSeat> seats = auditoriumSeatRepository.findAllByAuditorium(auditorium);
		
		// 2. Create ShowtimeSeat for each seat
		for (AuditoriumSeat seat : seats) {
			ShowtimeSeat showtimeSeat = new ShowtimeSeat();
			showtimeSeat.setShowtime(showtime);
			showtimeSeat.setAuditoriumSeat(seat);
			showtimeSeat.setSeatStatus(SeatStatus.AVAILABLE);
			
			// Set price (could vary by seat type)
			BigDecimal finalPrice = calculatePrice(basePrice, seat.getSeatType());
			showtimeSeat.setPrice(finalPrice);
			
			showtimeSeatRepository.save(showtimeSeat);
		}
	}
	
	private BigDecimal calculatePrice(BigDecimal basePrice, SeatType seatType) {
		return switch (seatType) {
			case XTRA_LEGROOM -> basePrice.multiply(new BigDecimal("1.5"));
			case PRIME_PLUS -> basePrice.multiply(new BigDecimal("2.0"));
			default -> basePrice; // NORMAL
		};
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












