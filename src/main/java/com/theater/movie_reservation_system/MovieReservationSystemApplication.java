package com.theater.movie_reservation_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*")
@EnableKafka
public class MovieReservationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieReservationSystemApplication.class, args);
	}

}
