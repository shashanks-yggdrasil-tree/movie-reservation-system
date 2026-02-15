package com.theater.movie_reservation_system.service;

import com.theater.movie_reservation_system.entity.Reservation;
import com.theater.movie_reservation_system.entity.ReservedSeat;
import com.theater.movie_reservation_system.entity.ShowtimeSeat;
import com.theater.movie_reservation_system.enums.ReservationStatus;
import com.theater.movie_reservation_system.enums.SeatStatus;
import com.theater.movie_reservation_system.repository.ReservationRepository;
import com.theater.movie_reservation_system.repository.ReservedSeatRepository;
import com.theater.movie_reservation_system.repository.ShowtimeSeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationExpiryService {
	
	private final ReservationRepository reservationRepository;
	private final ReservedSeatRepository reservedSeatRepository;
	private final ShowtimeSeatRepository showtimeSeatRepository;
	private final SeatLockService seatLockService;
	
	@Scheduled(fixedRate = 60000) // every 1 minute
	@Transactional
	public void expireReservations() {
		
		List<Reservation> expiredReservations =
				reservationRepository.findExpiredReservations(
						ReservationStatus.PENDING,
						LocalDateTime.now());
		
		for (Reservation reservation : expiredReservations) {
			
			List<ReservedSeat> reservedSeats =
					reservedSeatRepository.findByReservationId(reservation.getId());
			
			for (ReservedSeat rs : reservedSeats) {
				
				ShowtimeSeat seat = rs.getShowtimeSeat();
				seat.setSeatStatus(SeatStatus.AVAILABLE);
				
				seatLockService.releaseSeat(seat.getId());
			}
			
			reservedSeatRepository.deleteAll(reservedSeats);
			
			reservation.setStatus(ReservationStatus.CANCELLED);
		}
	}
}
