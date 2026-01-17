package com.theater.movie_reservation_system.repository;

import com.theater.movie_reservation_system.entity.Auditorium;
import com.theater.movie_reservation_system.entity.AuditoriumSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuditoriumSeatRepository extends JpaRepository<AuditoriumSeat, Long> {
	// find a specific seat by using the combination of auditorium, row label and seat number
	Optional<AuditoriumSeat> findByAuditoriumAndRowLabelAndSeatNumber(Auditorium auditorium, String rowLabel, int seatNumber);
	List<AuditoriumSeat> findAllByAuditorium(Auditorium auditorium);
}
