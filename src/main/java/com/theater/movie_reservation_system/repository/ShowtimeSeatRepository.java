package com.theater.movie_reservation_system.repository;

import com.theater.movie_reservation_system.entity.ShowtimeSeat;
import com.theater.movie_reservation_system.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowtimeSeatRepository extends JpaRepository<ShowtimeSeat, Long> {
	
	@Query("SELECT s FROM ShowtimeSeat s WHERE s.id IN :ids AND s.seatStatus = :status")
	List<ShowtimeSeat> findAvailableSeats(@Param("ids") List<Long> ids,
	                                      @Param("status") SeatStatus status);
}
