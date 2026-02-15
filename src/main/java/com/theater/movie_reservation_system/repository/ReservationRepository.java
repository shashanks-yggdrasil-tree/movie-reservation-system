package com.theater.movie_reservation_system.repository;

import com.theater.movie_reservation_system.entity.Reservation;
import com.theater.movie_reservation_system.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * Because Spring Data JPA needs to know TWO things at compile time:
 * 1. Which Entity this repository manages
 * 2. What is the type of the entity’s primary key
 * * */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	
	@Query("""
           SELECT r
           FROM Reservation r
           WHERE r.status = :status
           AND r.expiryTime <= :now
           """)
	List<Reservation> findExpiredReservations(
			@Param("status") ReservationStatus status,
			@Param("now") LocalDateTime now);

}
