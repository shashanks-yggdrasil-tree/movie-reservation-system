package com.theater.movie_reservation_system.entity;

import com.theater.movie_reservation_system.enums.SeatType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "auditorium_seats",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uk_auditorium_row_seat",
						columnNames = {"auditorium_id", "row_label", "seat_number"}
				)
		}
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Seat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auditorium_id", nullable = false)
// I think it does not make sense to add unique or nullable properties to a foreign key, any ways I am adding it
	private Auditorium auditorium;
	
	@Column(name = "row_label", length = 1, nullable = false)
	private String rowLabel;
	
	@Column(name = "seat_number", nullable = false)
	private int seatNumber;
	
	@Column(name = "seat_type")
	private SeatType seatType;
}
