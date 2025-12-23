package com.theater.movie_reservation_system.service;

import com.theater.movie_reservation_system.entity.Movie;
import com.theater.movie_reservation_system.enums.DeleteResult;
import com.theater.movie_reservation_system.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
	
	private final MovieRepository movieRepository;
	
	// Constructor Injection
	public MovieService(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}
	
	public Movie createMovie(Movie movie) {
		return movieRepository.save(movie); // Saves to the 'movie' table
	}
	
	public List<Movie> getAllMovies() {
		return movieRepository.findAll(); // get all movies
	}
	
	public Movie getMovieById(Long id) {
		return movieRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
	}
	
	@Transactional
	public DeleteResult deleteMovieById(Long id) {
		
		// Check if entity exists before deletion
		if (!movieRepository.existsById(id)) {
			return DeleteResult.NOT_FOUND;
		}
		
		try {
			movieRepository.deleteById(id);
			
			if (movieRepository.existsById(id)) {
				return DeleteResult.FAILED;
			}
			
			return DeleteResult.SUCCESS;
			
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
			return DeleteResult.FAILED;
		
		}
		
	}
}














