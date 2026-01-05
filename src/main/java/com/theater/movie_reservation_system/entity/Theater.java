package com.theater.movie_reservation_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "theaters")
public class Theater {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "theater_name", nullable = false, unique = true)
	private String theaterName;
	
	@Column(name = "address_line_1", nullable = false)
	private String addressLine1;
	
	@Column(name = "address_line_2")
	private String addressLine2;
	
	private String city;
	private String state;
	
	@Column(name = "zip_code")
	private String zipCode;
	
	@Column(name="phone_number", unique = true)
	private String phoneNumber;
	
	@Column(unique = true)
	private String email;
	
	@Column(name = "total_screens")
	private Integer totalScreens;
	
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	// ========= Constructor =========
	public Theater() {}
	
	// All-args constructor (for completeness)
	public Theater(String theaterName, String addressLine1, String addressLine2,
	               String city, String state, String zipCode,
	               String phoneNumber, String email, Integer totalScreens) {
		this.theaterName = theaterName;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.totalScreens = totalScreens;
	}
	
	// Convenience constructor (for simple cases)
	public Theater(String theaterName, String addressLine1, String city, String state) {
		this(theaterName, addressLine1, null, city, state, null, null, null, null);
	}
	
	// ======= Lifecycle Callbacks ========
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	
	@Override
	public String toString() {
		return "Theater{" +
				"id=" + id +
				", name='" + theaterName + '\'' +
				", city='" + city + '\'' +
				'}';
	}
}



















