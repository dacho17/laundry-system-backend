package com.laundrysystem.backendapi.dtos;

public class UpdateUserInfoForm {
	private String username;
	private String email;
	private String mobileNumber;
	
	public UpdateUserInfoForm(String username, String email, String mobileNumber) {
		super();
		this.username = username;
		this.email = email;
		this.mobileNumber = mobileNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public String toString() {
		return "UpdateUserInfoForm [username=" + username + ", email=" + email + ", mobileNumber=" + mobileNumber + "]";
	}
}
