package com.theater.movie_reservation_system.service.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class SeatLockService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	// Lock seat for 10 minutes
	public boolean lockSeat(Long showtimeId, Long seatId, Long userId) {
		String key = String.format("showtime:%d:seat%d", showtimeId, seatId); // object:id:field or object:id
		
		// SET if NOT exists with 10-minute expiry. Here key is key, value is userId
		return Boolean.TRUE.equals(
				redisTemplate.opsForValue().setIfAbsent(key, userId.toString(), 10, TimeUnit.MINUTES)
		);
	}
	
	// Release seat lock
	public void releaseSeat(Long showtimeId, Long seatId) {
		String key = String.format("showtime:%d:seat%d", showtimeId, seatId); // object:id:field or object:id
		redisTemplate.delete(key);
	} // later task: Question: I do not understand why this pattern of not sending back a confirmation where the user is
//					 confirmed deleted or not is practiced? and why usually they do not send back anything!
	
	
	// Check is seat is available
	public boolean isSeatAvailable(Long showtimeId, Long seatId) {
		String key = String.format("showtime:%d:seat%d", showtimeId, seatId); // object:id:field or object:id
		return !Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
	
	// Get user who locked the seat
	public Optional<Long> getLockingUser(Long showtimeId, Long seatId) {
		String key = String.format("showtime:%d:seat%d", showtimeId, seatId);
		String userId = redisTemplate.opsForValue().get(key);
		return Optional.ofNullable(userId).map(Long::valueOf);
	}
	
}






















