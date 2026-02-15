	package com.theater.movie_reservation_system.controller.adminapi;
	
	import com.theater.movie_reservation_system.dto.TheaterRequestDTO;
	import com.theater.movie_reservation_system.dto.TheaterResponseDTO;
	import com.theater.movie_reservation_system.entity.Theater;
	import com.theater.movie_reservation_system.mapper.TheaterMapper;
	import com.theater.movie_reservation_system.service.TheaterService;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.*;
	
	import java.util.List;
	
	@RestController
	@RequestMapping("/api/v1/theaters")
	public class TheaterController {
		
		private final TheaterService theaterService;
		
		
		public TheaterController(TheaterService theaterService) {
			this.theaterService = theaterService;
		}
		
		@PostMapping
		public ResponseEntity<Theater> createTheater(
				@RequestBody TheaterRequestDTO requestDTO) {
			
			Theater theater = TheaterMapper.toEntity(requestDTO);
			Theater savedTheater = theaterService.createTheater(theater);
			
			return ResponseEntity.ok(savedTheater);
		}
		
		@GetMapping
		public ResponseEntity<List<TheaterResponseDTO>> getAllTheaters() {
			
			List<TheaterResponseDTO> response =
					theaterService.getAllTheaters()
							.stream()
							.map(TheaterMapper::toResponse)
							.toList();
			
			return ResponseEntity.ok(response);
		}
	}