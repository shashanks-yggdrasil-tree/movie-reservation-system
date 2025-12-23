package com.theater.movie_reservation_system.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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
	
	@Column
	private String city;
	
	@Column
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
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTheaterName() {
		return theaterName;
	}
	
	public void setTheaterName(String theaterName) {
		this.theaterName = theaterName;
	}
	
	public String getAddressLine1() {
		return addressLine1;
	}
	
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	
	public String getAddressLine2() {
		return addressLine2;
	}
	
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Integer getTotalScreens() {
		return totalScreens;
	}
	
	public void setTotalScreens(Integer totalScreens) {
		this.totalScreens = totalScreens;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
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



















