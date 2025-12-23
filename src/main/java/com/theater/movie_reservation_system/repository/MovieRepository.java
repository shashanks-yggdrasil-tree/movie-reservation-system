package com.theater.movie_reservation_system.repository;

import com.theater.movie_reservation_system.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
	// Spring Data JPA will provide all basic CRUD methods (save, findById, findAll, delete) automatically.
	
	// We can add custom method later.
}
