package com.theater.movie_reservation_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "showtimes")
@Getter
@Setter
public class Showtime {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// ========= 3 FOREIGN KEYS ==========
	
	// Showtime is for ONE movie
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;
	
	// Showtime is for ONE theater
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id", nullable = false)
	private Theater theater;
	
	// Showtime is for ONE Auditorium (Screen)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auditorium_id", nullable = false)
	private Auditorium auditorium;
	
	@Column(name = "show_time", nullable = false)
	private LocalDateTime showTime; // later task: add start time and end time instead of just showTime

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	// ========= Constructors =========
	public Showtime() {}
	
	public Showtime(Movie movie, Theater theater, Auditorium auditorium, LocalDateTime showTime) {
		this.movie = movie;
		this.theater = theater;
		this.auditorium = auditorium;
		this.showTime = showTime;
	}
	
	// ======== Lifecycle =========
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
	
	public Movie getMovie() {
		return movie;
	}
	
	public Theater getTheater() {
		return theater;
	}
	
	public Auditorium getAuditorium() {
		return auditorium;
	}
	
	public LocalDateTime getShowTime() {
		return showTime;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	public void setTheater(Theater theater) {
		this.theater = theater;
	}
	
	public void setAuditorium(Auditorium auditorium) {
		this.auditorium = auditorium;
	}
	
	public void setShowTime(LocalDateTime showTime) {
		this.showTime = showTime;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	@Override
	public String toString() {
		return "Showtime{" +
				"id=" + id +
				", movie=" + (movie != null ? movie.getMovieName() : "null") +
				", theater=" + (theater != null ? theater.getTheaterName() : "null") +
				", auditorium=" + (auditorium != null ? auditorium.getScreenName() : "null") +
				", showTime=" + showTime +
				'}';
	}
}




















