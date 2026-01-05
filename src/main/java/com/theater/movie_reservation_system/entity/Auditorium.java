package com.theater.movie_reservation_system.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoriums")
public class Auditorium {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "screen_name", nullable = false)
	private String screenName; // e.g., "Screen 1", "IMAX Hall"
	
	@Column(name = "capacity", nullable = false)
	public Integer capacity; // Total seats
	
	// ===== FIRST FOREIGN KEY =====
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id", nullable = false) // theater_id refers to theater.id somehow
	private Theater theater;
	// Auditorium BELONGS TO a Theater (ManyToOne)
	
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	// ===== Constructors =====
	public Auditorium() {}
	
	public Auditorium(String screenName, Integer capacity, Theater theater) {
		this.screenName = screenName;
		this.capacity = capacity;
		this.theater = theater;
	}
	
	// ===== Lifecycle Callbacks =====
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	
	// ===== Getters & Setters =====
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public String getScreenName() { return screenName; }
	public void setScreenName(String screenName) { this.screenName = screenName; }
	
	public Integer getCapacity() { return capacity; }
	public void setCapacity(Integer capacity) { this.capacity = capacity; }
	
	public Theater getTheater() { return theater; }
	public void setTheater(Theater theater) { this.theater = theater; }
	
	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
	
	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
	
	@Override
	public String toString() {
		return "Auditorium{" +
				"id=" + id +
				", name='" + screenName + '\'' +
				", capacity=" + capacity +
				", theater=" + (theater != null ? theater.getTheaterName() : "null") +
				'}';
	}
	
	
}
