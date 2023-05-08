package com.laundrysystem.backendapi.dtos;

import java.sql.Timestamp;

public class TenantDto {
	private String name;
	private String surname;
	private String username;
	private Timestamp tenancyFrom;
	private Timestamp tenancyTo;
	private boolean isTenancyActive;
	private String email;
	private String mobileNumber;
	
	public TenantDto() {}
	
	public TenantDto(String name, String surname, String username, Timestamp tenancyFrom, Timestamp tenancyTo,
			boolean isTenancyActive, String email, String mobileNumber) {
		super();
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.tenancyFrom = tenancyFrom;
		this.tenancyTo = tenancyTo;
		this.isTenancyActive = isTenancyActive;
		this.email = email;
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

	public boolean isTenancyActive() {
		return isTenancyActive;
	}

	public void setTenancyActive(boolean isTenancyActive) {
		this.isTenancyActive = isTenancyActive;
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
		return "TenantDto [name=" + name + ", surname=" + surname + ", username=" + username + ", tenancyFrom="
				+ tenancyFrom + ", tenancyTo=" + tenancyTo + ", isTenancyActive=" + isTenancyActive + ", email=" + email
				+ ", mobileNumber=" + mobileNumber + "]";
	}
}
