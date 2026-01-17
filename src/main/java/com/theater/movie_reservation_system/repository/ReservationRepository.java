package com.theater.movie_reservation_system.repository;

import com.theater.movie_reservation_system.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * Because Spring Data JPA needs to know TWO things at compile time:
 * 1. Which Entity this repository manages
 * 2. What is the type of the entity’s primary key
 * * */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {



}
