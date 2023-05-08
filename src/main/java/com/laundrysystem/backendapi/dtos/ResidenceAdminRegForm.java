package com.laundrysystem.backendapi.dtos;

public class ResidenceAdminRegForm {
	private String username;
	private String password;
	private String name;
	private String surname;
	private String email;
	private String mobileNumber;
	
	public ResidenceAdminRegForm() {}

	public ResidenceAdminRegForm(String username, String password, String name, String surname, String email,
			String mobileNumber) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.mobileNumber = mobileNumber;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
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
		return "ResidenceAdminRegForm [username=" + username + ", password=" + password + ", name=" + name
				+ ", surname=" + surname + ", email=" + email + ", mobileNumber=" + mobileNumber + "]";
	}
}
