package com.laundrysystem.backendapi.dtos;

public class AuthRequest {
	
	private String username;
	private String password;
	private short role;
	
	public AuthRequest() {}

	public AuthRequest(String username, String password, short role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public short getRole() {
		return role;
	}

	public void setRole(short role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "SignupRequest [username=" + username + ", password=" + password + ", role=" + role + "]";
	}
}
