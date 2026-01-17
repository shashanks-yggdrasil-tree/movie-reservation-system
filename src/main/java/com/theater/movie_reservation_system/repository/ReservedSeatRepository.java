package com.theater.movie_reservation_system.repository;

import com.theater.movie_reservation_system.entity.ReservedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {

}
