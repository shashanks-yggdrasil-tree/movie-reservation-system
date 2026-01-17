package com.theater.movie_reservation_system.service.redis;

import com.theater.movie_reservation_system.entity.Reservation;
import com.theater.movie_reservation_system.entity.ReservedSeat;
import com.theater.movie_reservation_system.entity.ShowtimeSeat;
import com.theater.movie_reservation_system.entity.User;
import com.theater.movie_reservation_system.enums.ReservationStatus;
import com.theater.movie_reservation_system.enums.SeatStatus;
import com.theater.movie_reservation_system.exception.SeatNotAvailableException;
import com.theater.movie_reservation_system.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class BookingService {
	
	private final SeatLockService seatLockService;
	private final ReservationRepository reservationRepository;
	private final ReservedSeatRepository reservedSeatRepository;
	private final ShowtimeSeatRepository showtimeSeatRepository;
	private final UserRepository userRepository;
	private final ShowTimeRepository showtimeRepository;
	
	public BookingService(SeatLockService seatLockService,
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
			for (ShowtimeSeat showtimeSeat: availableSeats) {
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
	
	
}


// Comment

// Better approach to handle exception is
// In service method
// User user = userRepository.findById(userId)
//     .orElseThrow(() -> new UserNotFoundException(userId));
// Showtime showtime = showtimeRepository.findById(showtimeId)
//     .orElseThrow(() -> new ShowtimeNotFoundException(showtimeId));
//






