package com.laundrysystem.backendapi.dtos;

public class ResidenceDto {
	private String name;
	private String streetName;
	private String streetNumber;	
	private String postalCode;	
	private String city;
	private String country;
	
	public ResidenceDto(String name, String streetName, String streetNumber, String postalCode, String city,
			String country) {
		super();
		this.name = name;
		this.streetName = streetName;
		this.streetNumber = streetNumber;
		this.postalCode = postalCode;
		this.city = city;
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "ResidenceDto [name=" + name + ", streetName=" + streetName + ", streetNumber=" + streetNumber
				+ ", postalCode=" + postalCode + ", city=" + city + ", country=" + country + "]";
	}
}
