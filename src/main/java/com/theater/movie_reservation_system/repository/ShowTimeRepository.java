package com.theater.movie_reservation_system.repository;

import com.theater.movie_reservation_system.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShowTimeRepository extends JpaRepository<Showtime, Long> {

	// Find showtimes by movie
	List<Showtime> findByMovieId(Long movieId);
	
	// Find showtimes by theater
	List<Showtime> findByTheaterId(Long theaterId);
	
	// Find showtimes by date (custom query)
	@Query("SELECT s FROM Showtime s WHERE DATE(s.showTime) = :date") // later task question :  is showTime a date or datetime
	List<Showtime> findByDate(@Param("date")LocalDate date);

	// Find showtimes by movie and date
	@Query("SELECT s FROM Showtime s WHERE s.movie.id = :movieId AND DATE(s.showTime) = :date")
	List<Showtime> findByMovieIdAndDate(@Param("movieId") Long movieId, @Param("date") LocalDate date);
}


