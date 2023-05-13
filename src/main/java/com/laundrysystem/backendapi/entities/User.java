package com.laundrysystem.backendapi.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "role")
	private short role;
	
	@Column(name = "jwt")
	private String jwt;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "surname")
	private String surname;
	
	@Column(name = "email")
	private String email;

	@Column(name = "country_dial_code")
	private String countryDialCode;
	
	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "password_reset_token")
	private String passwordResetToken;

	@Column(name = "password_reset_valid_until")
	private Timestamp passwordResetValidUntil;
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
		CascadeType.DETACH, CascadeType.REFRESH})
	private List<PaymentCard> paymentCards;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
		CascadeType.DETACH, CascadeType.REFRESH})
	private List<Booking> bookings;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
		CascadeType.DETACH, CascadeType.REFRESH})
	private List<Purchase> purchases;
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
		CascadeType.DETACH, CascadeType.REFRESH})
	private List<UserResidence> userResidences;
	
	public User() {
	}
	
	public User(Timestamp createdDate, String username, String password, short role, String name, String surname) {
		this.createdDate = createdDate;
		this.username = username;
		this.password = password;
		this.role = role;
		this.name = name;
		this.surname = surname;
	}
	
	public User(Timestamp createdDate, String username, String password, short role, String name, String surname,
			String email, String countryDialCode, String mobileNumber) {
		this.createdDate = createdDate;
		this.username = username;
		this.password = password;
		this.role = role;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.countryDialCode = countryDialCode;
		this.mobileNumber = mobileNumber;
	}

	public User(int id, Timestamp createdDate, String username, String password, short role, String name, String surname,
		String jwt, String email, String countryDialCode, String mobileNumber, String passwordResetToken, Timestamp passwordResetValidUntil,
		List<PaymentCard> paymentCards, List<Booking> bookings, List<Purchase> purchases, List<UserResidence> userResidences) {
		super();
		this.id = id;
		this.createdDate = createdDate;
		this.username = username;
		this.password = password;
		this.role = role;
		this.name = name;
		this.surname = surname;
		this.jwt = jwt;
		this.email = email;
		this.countryDialCode = countryDialCode;
		this.mobileNumber = mobileNumber;
		this.passwordResetToken = passwordResetToken;
		this.passwordResetValidUntil = passwordResetValidUntil;
		this.paymentCards = paymentCards;
		this.bookings = bookings;
		this.purchases = purchases;
		this.userResidences = userResidences;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
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

	public void setRole(short role) {
		this.role = role;
	}
	
	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
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


	public String getPasswordResetToken() {
		return passwordResetToken;
	}

	public void setPasswordResetToken(String passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}

	public Timestamp getPasswordResetValidUntil() {
		return passwordResetValidUntil;
	}

	public void setPasswordResetValidUntil(Timestamp passwordResetValidUntil) {
		this.passwordResetValidUntil = passwordResetValidUntil;
	}

	public List<PaymentCard> getPaymentCards() {
		return paymentCards;
	}

	public void setPaymentCards(List<PaymentCard> paymentCards) {
		this.paymentCards = paymentCards;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	public List<UserResidence> getUserResidences() {
		return userResidences;
	}

	public void setUserResidences(List<UserResidence> userResidences) {
		this.userResidences = userResidences;
	}
	
	public void addUserResidence(UserResidence userResidence) {
		if (this.userResidences == null) {
			this.userResidences = new ArrayList<>();
		}
		
		this.userResidences.add(userResidence);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", createdDate=" + createdDate + ", username=" + username + ", password=" + password
				+ ", role=" + role + ", jwt=" + jwt + ", name=" + name + ", surname=" + surname + ", email=" + email
				+ ", countryDialCode=" + countryDialCode + ", mobileNumber=" + mobileNumber 
				+ ", passwordResetToken=" + passwordResetToken + ", passwordResetValidUntil=" + passwordResetValidUntil + "]";
	}
}
