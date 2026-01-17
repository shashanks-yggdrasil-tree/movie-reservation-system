package com.theater.movie_reservation_system.entity;

import com.theater.movie_reservation_system.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reservation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user; // Entity
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "showtime_id", nullable = false)
	private Showtime showtime; // Entity
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ReservationStatus status = ReservationStatus.PENDING;
	
	@Column(name = "expiry_time")
	private LocalDateTime expiryTime;
	
	@Column(name = "total_amount")
	private BigDecimal totalAmount;
	
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = createdAt;
		if (this.expiryTime == null) {
			this.expiryTime = LocalDateTime.now().plusMinutes(10);
		}
	}
	
}
