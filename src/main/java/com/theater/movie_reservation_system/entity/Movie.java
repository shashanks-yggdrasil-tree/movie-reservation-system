package com.theater.movie_reservation_system.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies") // this tells JPA to map to a table names "movies"
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "movie_name", nullable = false, unique = true)
	private String movieName;
	
	@Column(length = 1000) // Allows longer description
	private String description;
	
	@Column(nullable = false)
	private String genre;
	
	@Column(name = "duration_minutes", nullable = false)
	private Integer durationMinutes;
	
	@Column(name = "release_date")
	private LocalDate releaseDate;
	
	@Column(nullable = false)
	private String language;
	
	// Timestamps for tracking
	@Column(name="created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name="updated_at")
	private LocalDateTime updatedAt;
	
	// ======== Constructor =========
	public Movie() {}
	
	public Movie(String movieName, String description, String genre, Integer durationMinutes, LocalDate releaseDate, String language) {
		this.movieName = movieName;
		this.description = description;
		this.genre = genre;
		this.durationMinutes = durationMinutes;
		this.releaseDate = releaseDate;
		this.language = language;
	};
	
	// ========= Lifecycle Callbacks =========
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMovieName() {
		return movieName;
	}
	
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public Integer getDurationMinutes() {
		return durationMinutes;
	}
	
	public void setDurationMinutes(Integer durationMinutes) {
		this.durationMinutes = durationMinutes;
	}
	
	public LocalDate getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
























