package com.theater.movie_reservation_system.repository;

import com.theater.movie_reservation_system.entity.Auditorium;
import com.theater.movie_reservation_system.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
	// find a specific seat by using the combination of auditorium, row label and seat number
	Optional<Seat> findByAuditoriumAndRowLabelAndSeatNumber(Auditorium auditorium, String rowLabel, int seatNumber);
	List<Seat> findAllByAuditorium(Auditorium auditorium);
}
