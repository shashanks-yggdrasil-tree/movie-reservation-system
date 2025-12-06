package com.theater.movie_reservation_system.repository;

import com.theater.movie_reservation_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	// Find user by the phoneNumber
	Optional<User> findUserByPhoneNumber(String phoneNumber);
	
	// Check if the phoneNumber exists
	boolean existsByPhoneNumber(String phoneNumber);

}
