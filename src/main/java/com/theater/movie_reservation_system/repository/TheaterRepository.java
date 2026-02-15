package com.theater.movie_reservation_system.repository;

import com.theater.movie_reservation_system.dto.TheaterResponseDTO;
import com.theater.movie_reservation_system.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
	// Basic CRUD is provided automatically
}
