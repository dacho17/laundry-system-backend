package com.laundrysystem.backendapi.dtos;

public class ResidenceAdminDto {
	private String username;
	private String name;
	private String surname;
	private String email;
	private String countryDialCode;
	private String mobileNumber;
	
	public ResidenceAdminDto() {}

	public ResidenceAdminDto(String username, String name, String surname, String email, String countryDialCode, String mobileNumber) {
		super();
		this.username = username;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.countryDialCode = countryDialCode;
		this.mobileNumber = mobileNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getCountryDialCode() {
		return countryDialCode;
	}

	public void setCountryDialCode(String countryDialCode) {
		this.countryDialCode = countryDialCode;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public String toString() {
		return "ResidenceAdminDto [username=" + username + ", name=" + name + ", surname=" + surname + ", email="
				+ email + ", countryDialCode=" + countryDialCode + ", mobileNumber=" + mobileNumber + "]";
	}
}
