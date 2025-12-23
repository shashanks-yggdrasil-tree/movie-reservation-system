package com.theater.movie_reservation_system.repository;

import com.theater.movie_reservation_system.entity.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {
	// Find all auditoriums in a specific theater
	List<Auditorium> findByTheaterId(Long theaterId);
}