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
	public boolean lockSeat(Long showtimeSeatStatusId, Long userId) {
		String key = String.format("lock:showtime_seat:%d", showtimeSeatStatusId); // object:id:field or object:id
		
		// SET if NOT exists with 10-minute expiry. Here key is key, value is userId
		return Boolean.TRUE.equals(
				redisTemplate.opsForValue().setIfAbsent(key, userId.toString(), 10, TimeUnit.MINUTES)
		);
	}
	
	// Release seat lock
	public void releaseSeat(Long showtimeSeatStatusId) {
		String key = String.format("lock:showtime_seat:%d", showtimeSeatStatusId);
		redisTemplate.delete(key);
	}
	
	// Check if seat is available
	public boolean isSeatAvailable(Long showtimeSeatStatusId) {
		String key = String.format("lock:showtime_seat:%d", showtimeSeatStatusId);
		return !Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
	
	// Get user who locked the seat
	public Optional<Long> getLockingUser(Long showtimeSeatStatusId) {
		String key = String.format("lock:showtime_seat:%d", showtimeSeatStatusId);
		String userId = redisTemplate.opsForValue().get(key);
		return Optional.ofNullable(userId).map(Long::valueOf);
	}
}






















