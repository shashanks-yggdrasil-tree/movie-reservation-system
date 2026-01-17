package com.theater.movie_reservation_system.entity;

import com.theater.movie_reservation_system.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "showtime_seat",
		uniqueConstraints = @UniqueConstraint(
		name = "uk_showtime_seat",
		columnNames = {"showtime_id", "auditorium_seat_id"}))
@Getter
@Setter
public class ShowtimeSeat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "showtime_id", nullable = false)
	private Showtime showtime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auditorium_seat_id", nullable = false)
	private AuditoriumSeat auditoriumSeat;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "seat_status", nullable = false)
	private SeatStatus seatStatus;
	
	@Column(name = "price", nullable = false, precision = 10, scale = 2)
	private BigDecimal price;
	
}
