package com.theater.movie_reservation_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserved_seats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservedSeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id", nullable = false)
	private Reservation reservation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "showtime_seat_id", nullable = false)
	private ShowtimeSeat showtimeSeat;
	
	@Column(name = "locked_at", nullable = false)
	private LocalDateTime lockedAt = LocalDateTime.now();
}
