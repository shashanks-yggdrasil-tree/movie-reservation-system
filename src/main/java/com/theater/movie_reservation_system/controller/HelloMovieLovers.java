package com.theater.movie_reservation_system.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class HelloMovieLovers {
	
	public HelloMovieLovers() {
		System.out.println("HelloMovieLovers controller initialized!");
	}
	
	@GetMapping("/hello-world")
	public String getHelloWorld() {
		return "Hello Dear Movie and Music Lovers";
	}
	
}
