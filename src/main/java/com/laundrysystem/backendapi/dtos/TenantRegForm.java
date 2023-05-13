package com.laundrysystem.backendapi.dtos;

import java.sql.Timestamp;

public class TenantRegForm {
	private String name;
	private String surname;
	private String username;
	private String password;
	private Timestamp tenancyFrom;
	private Timestamp tenancyTo;
	private String email;
	private String countryDialCode;
	private String mobileNumber;
	
	public TenantRegForm() {}

	public TenantRegForm(String name, String surname, String username, String password, Timestamp tenancyFrom,
			Timestamp tenancyTo, String email, String countryDialCode, String mobileNumber) {
		super();
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password = password;
		this.tenancyFrom = tenancyFrom;
		this.tenancyTo = tenancyTo;
		this.email = email;
		this.countryDialCode = countryDialCode;
		this.mobileNumber = mobileNumber;
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

	public Timestamp getTenancyFrom() {
		return tenancyFrom;
	}

	public void setTenancyFrom(Timestamp tenancyFrom) {
		this.tenancyFrom = tenancyFrom;
	}

	public Timestamp getTenancyTo() {
		return tenancyTo;
	}

	public void setTenancyTo(Timestamp tenancyTo) {
		this.tenancyTo = tenancyTo;
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
		return "TenantRegForm [name=" + name + ", surname=" + surname + ", username=" + username + ", password="
				+ password + ", tenancyFrom=" + tenancyFrom + ", tenancyTo=" + tenancyTo + ", email=" + email
				+ ", countryDialCode=" + countryDialCode + ", mobileNumber=" + mobileNumber + "]";
	}
}
