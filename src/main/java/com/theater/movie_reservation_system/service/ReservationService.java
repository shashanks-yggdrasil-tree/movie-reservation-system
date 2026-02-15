package com.theater.movie_reservation_system.service;

import com.theater.movie_reservation_system.entity.Reservation;
import com.theater.movie_reservation_system.entity.ReservedSeat;
import com.theater.movie_reservation_system.entity.ShowtimeSeat;
import com.theater.movie_reservation_system.enums.ReservationStatus;
import com.theater.movie_reservation_system.enums.SeatStatus;
import com.theater.movie_reservation_system.exception.ReservationNotAvailableException;
import com.theater.movie_reservation_system.exception.SeatNotAvailableException;
import com.theater.movie_reservation_system.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservationService {
	
	private final SeatLockService seatLockService;
	private final ReservationRepository reservationRepository;
	private final ReservedSeatRepository reservedSeatRepository;
	private final ShowtimeSeatRepository showtimeSeatRepository;
	private final UserRepository userRepository;
	private final ShowTimeRepository showtimeRepository;
	
	public ReservationService(SeatLockService seatLockService,
	                          ReservationRepository reservationRepository,
	                          ReservedSeatRepository reservedSeatRepository,
	                          ShowtimeSeatRepository showtimeSeatRepository,
	                          UserRepository userRepository,
	                          ShowTimeRepository showtimeRepository
	) {
		this.seatLockService = seatLockService;
		this.reservationRepository = reservationRepository;
		this.reservedSeatRepository = reservedSeatRepository;
		this.showtimeSeatRepository = showtimeSeatRepository;
		this.userRepository = userRepository;
		this.showtimeRepository = showtimeRepository;
	}
	
	@Transactional
	public Reservation createReservation(Long userId, Long showtimeId, List<Long> showtimeSeatIds) {
		
		// 0. TRY TO LOCK ALL SEATS IN REDIS FIRST
		for (Long seatId : showtimeSeatIds) {
			if (!seatLockService.lockSeat(seatId, userId)) {
				throw new SeatNotAvailableException("Seat " + seatId + " is already taken");
			}
		}
		
		try {
			// 1. Get showtime seats with prices (Find availability of the seats)
			List<ShowtimeSeat> availableSeats = showtimeSeatRepository
					.findAvailableSeats(showtimeSeatIds, SeatStatus.AVAILABLE);
			
			if (availableSeats.size() != showtimeSeatIds.size()) {
				throw new SeatNotAvailableException("Some seats are already taken");
			}
			
			// 2. Calculate Total
			BigDecimal total = availableSeats.stream()
					.map(ShowtimeSeat::getPrice)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			
			// 3. Create reservation
			Reservation reservation = new Reservation();
			reservation.setUser(userRepository.findById(userId).orElseThrow());
			reservation.setShowtime(showtimeRepository.findById(showtimeId).orElseThrow());
			reservation.setTotalAmount(total);
			reservation.setStatus(ReservationStatus.PENDING);
			Reservation savedReservation = reservationRepository.save(reservation);
			
			// 4. Update seat statuses to RESERVED and create reserved_seats records
			for (ShowtimeSeat showtimeSeat : availableSeats) {
				showtimeSeat.setSeatStatus(SeatStatus.RESERVED);
				showtimeSeatRepository.save(showtimeSeat);
				
				ReservedSeat reservedSeat = new ReservedSeat();
				reservedSeat.setReservation(savedReservation);
				reservedSeat.setShowtimeSeat(showtimeSeat);
				reservedSeatRepository.save(reservedSeat);
				
			}
			return savedReservation;
			
		} catch (Exception e) {
			// Release locks if anything fails
			for (Long seatId : showtimeSeatIds) {
				seatLockService.releaseSeat(seatId);
			}
			throw e;
		}
		
	}
	
	@Transactional
	public Reservation confirmReservation(Long reservationId, Long userId) {
		
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow();
		
		if (!reservation.getUser().getId().equals(userId)) {
			throw new ReservationNotAvailableException("Not your reservation");
		}
		
		if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
			return reservation; // idempotent
		}
		
		if (reservation.getStatus() != ReservationStatus.PENDING) {
			throw new ReservationNotAvailableException("Invalid state");
		}
		
		if (reservation.getExpiryTime().isBefore(LocalDateTime.now())) {
			throw new ReservationNotAvailableException("Reservation expired");
		}
		
		List<ReservedSeat> reservedSeats =
				reservedSeatRepository.findByReservationId(reservationId);
		
		if (reservedSeats.isEmpty()) {
			throw new IllegalStateException("No seats found for reservation");
		}
		
		if (!simulatePaymentProcessing(reservation)) {
			throw new RuntimeException("Payment failed");
		}
		
		reservation.setStatus(ReservationStatus.CONFIRMED);
		
		for (ReservedSeat rs : reservedSeats) {
			ShowtimeSeat seat = rs.getShowtimeSeat();
			seat.setSeatStatus(SeatStatus.BOOKED);
			seatLockService.releaseSeat(seat.getId());
		}
		
		return reservation;
	}

//	public Reservation cancelReservation() {}
//	public Reservation refundReservation() {}
	
	private boolean simulatePaymentProcessing(Reservation reservation) {
		try {
			// Simulate payment gateway call
			Thread.sleep(2000); // 2 second delay to mimic real payment
			
			// 90% success rate for simulation
			if (Math.random() > 0.9) {
				return false;
			}
			
			// Log payment success
			System.out.println("Payment processed for reservation: " + reservation.getId());
			return true;
			
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Payment simulation interrupted");
		}
	}
	
	
}


// Comment

// Better approach to handle exception is
// In service method
// User user = userRepository.findById(userId)
//     .orElseThrow(() -> new UserNotFoundException(userId));
// Showtime showtime = showtimeRepository.findById(showtimeId)
//     .orElseThrow(() -> new ShowtimeNotFoundException(showtimeId));
//






