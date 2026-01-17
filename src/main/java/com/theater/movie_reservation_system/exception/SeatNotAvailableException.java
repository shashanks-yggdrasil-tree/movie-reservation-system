package com.theater.movie_reservation_system.exception;

// created custom exceptions
public class SeatNotAvailableException extends RuntimeException {
		public SeatNotAvailableException(String message) {
			super(message);
		}
		
		public SeatNotAvailableException(String message, Throwable cause) {
			super(message, cause);
		}
	
}
