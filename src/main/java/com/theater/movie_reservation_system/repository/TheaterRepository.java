package com.theater.movie_reservation_system.repository;

import com.theater.movie_reservation_system.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
	// Basic CRUD is provided automatically
}
