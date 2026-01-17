			package com.theater.movie_reservation_system.controller.adminapi;
			
			import com.theater.movie_reservation_system.entity.Auditorium;
			import com.theater.movie_reservation_system.entity.AuditoriumSeat;
			import com.theater.movie_reservation_system.entity.Showtime;
			import com.theater.movie_reservation_system.entity.ShowtimeSeat;
			import com.theater.movie_reservation_system.enums.SeatStatus;
			import com.theater.movie_reservation_system.enums.SeatType;
			import com.theater.movie_reservation_system.repository.*;
			import com.theater.movie_reservation_system.service.AuditoriumSeatService;
			import com.theater.movie_reservation_system.service.redis.SeatLockService;
			import org.springframework.http.ResponseEntity;
			import org.springframework.web.bind.annotation.*;
			
			import java.math.BigDecimal;
			import java.util.List;
			
			@RestController
			@RequestMapping("/api/v1/auditorium-seats")
			public class AuditoriumSeatController {
				
				private final AuditoriumSeatService auditoriumSeatService;
				private final SeatLockService seatLockService;
				private final ReservationRepository reservationRepository;
				private final ReservedSeatRepository reservedSeatRepository;
				private final ShowtimeSeatRepository showtimeSeatRepository;
				private final UserRepository userRepository;
				private final ShowTimeRepository showtimeRepository;
				private final AuditoriumSeatRepository auditoriumSeatRepository;
				
				public AuditoriumSeatController(AuditoriumSeatService auditoriumSeatService,
				SeatLockService seatLockService,
				                                ReservationRepository reservationRepository,
				                                ReservedSeatRepository reservedSeatRepository,
				                                ShowtimeSeatRepository showtimeSeatRepository,
				                                UserRepository userRepository,
				                                ShowTimeRepository showtimeRepository,
				                                AuditoriumSeatRepository auditoriumSeatRepository
				) {
					this.auditoriumSeatService = auditoriumSeatService;
					this.seatLockService = seatLockService;
					this.reservationRepository = reservationRepository;
					this.reservedSeatRepository = reservedSeatRepository;
					this.showtimeSeatRepository = showtimeSeatRepository;
					this.userRepository = userRepository;
					this.showtimeRepository = showtimeRepository;
					this.auditoriumSeatRepository = auditoriumSeatRepository;
				}
				
				@GetMapping("/by-auditorium/{auditoriumId}")
				public ResponseEntity<List<AuditoriumSeat>> getByAuditorium(@PathVariable Long auditoriumId) {
					return ResponseEntity.ok(auditoriumSeatService.getAllSeatsByAuditorium(auditoriumId));
				}
				
				@GetMapping
				public ResponseEntity<List<AuditoriumSeat>> getAllSeats() {
					return ResponseEntity.ok(auditoriumSeatService.getAllSeats());
				}
				
				@PostMapping
				public  ResponseEntity<AuditoriumSeat> createSeats(
						@RequestParam Long auditoriumId,
						@RequestParam String row_label,
						@RequestParam int seatNumber,
						@RequestParam SeatType seatType
						) {
					AuditoriumSeat auditoriumSeat = auditoriumSeatService.createSeat(auditoriumId, row_label, seatNumber, seatType);
					return ResponseEntity.ok(auditoriumSeat);
				}
				
				@PostMapping("/populate-seats/{showtimeId}")
				public String populateSeats(@PathVariable Long showtimeId,
				                            @RequestParam BigDecimal basePrice) {
					
					Showtime showtime = showtimeRepository.findById(showtimeId).orElseThrow();
					Auditorium auditorium = showtime.getAuditorium();
					
					// Create 20 sample seats A1-A10, B1-B10
					for (char row = 'A'; row <= 'B'; row++) {
						for (int num = 1; num <= 10; num++) {
							ShowtimeSeat showtimeSeat = new ShowtimeSeat();
							showtimeSeat.setShowtime(showtime);
							
							// Create or find seat
							char finalRow = row;
							int finalNum = num;
							AuditoriumSeat seat = auditoriumSeatRepository
									.findByAuditoriumAndRowLabelAndSeatNumber(
											auditorium,
											String.valueOf(row),
											num
									).orElseGet(() -> createSeat(auditorium, finalRow, finalNum));
							
							showtimeSeat.setAuditoriumSeat(seat);  // ✅ Use setAuditoriumSeat, not setSeat
							showtimeSeat.setSeatStatus(SeatStatus.AVAILABLE);
							showtimeSeat.setPrice(basePrice);
							showtimeSeatRepository.save(showtimeSeat);
						}
					}
					
					return "Created 20 seats for showtime " + showtimeId;
				}
				
				private AuditoriumSeat createSeat(Auditorium auditorium, char row, int number) {
					AuditoriumSeat seat = new AuditoriumSeat();
					seat.setAuditorium(auditorium);
					seat.setRowLabel(String.valueOf(row));
					seat.setSeatNumber(number);
					seat.setSeatType(SeatType.PRIME); // Default type
					return auditoriumSeatRepository.save(seat);
				}
				
				
			}
			
			
			
			
			
			
			
			
			
			
