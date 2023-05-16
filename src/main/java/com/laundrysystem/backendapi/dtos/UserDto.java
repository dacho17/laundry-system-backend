package com.laundrysystem.backendapi.dtos;

import com.laundrysystem.backendapi.enums.UserRole;

public class UserDto {
	private String username;
	private UserRole role;
	private int loyaltyPoints;
	private String jwt;
	
	public UserDto() {}

	public UserDto(String username, UserRole role, int loyaltyPoints, String jwt) {
		super();
		this.username = username;
		this.role = role;
		this.loyaltyPoints = loyaltyPoints;
		this.jwt = jwt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public int getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(int loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	@Override
	public String toString() {
		return "UserDto [username=" + username + ", role=" + role + ", loyaltyPoints=" + loyaltyPoints + ", jwt=" + jwt + "]";
	}
}
