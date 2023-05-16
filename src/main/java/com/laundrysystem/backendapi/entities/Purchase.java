package com.laundrysystem.backendapi.entities;

import java.sql.Timestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchases")
public class Purchase {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "amount_paid")
	private Double amountPaid;

	@Column(name = "paid_in_currency")
	private String paidInCurrency;

	@Column(name = "loyalty_points_used")
	private Integer loyaltyPointsUsed;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "payment_card_id", referencedColumnName = "id")
	private PaymentCard paymentCard;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "laundry_asset_id", referencedColumnName = "id")
	private LaundryAsset laundryAsset;
	
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "booking_id", referencedColumnName = "id")
	private Booking booking;
	
	public Purchase() {}
	
	public Purchase(Timestamp createdDate, double amountPaid, String paidInCurrency,
		User user, PaymentCard paymentCard, LaundryAsset laundryAsset) {
		super();
		this.createdDate = createdDate;
		this.amountPaid = amountPaid;
		this.paidInCurrency = paidInCurrency;
		this.user = user;
		this.paymentCard = paymentCard;
		this.laundryAsset = laundryAsset;
	}
	
	public Purchase(Timestamp createdDate, int loyaltyPointsUsed, User user, LaundryAsset laundryAsset) {
		this.createdDate = createdDate;
		this.loyaltyPointsUsed = loyaltyPointsUsed;
		this.user = user;
		this.laundryAsset = laundryAsset;
	}

	public Purchase(int id, Timestamp createdDate, User user, PaymentCard paymentCard, LaundryAsset laundryAsset,
			Booking booking) {
		super();
		this.id = id;
		this.createdDate = createdDate;
		this.user = user;
		this.paymentCard = paymentCard;
		this.laundryAsset = laundryAsset;
		this.booking = booking;
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

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getPaidInCurrency() {
		return paidInCurrency;
	}

	public void setPaidInCurrency(String paidInCurrency) {
		this.paidInCurrency = paidInCurrency;
	}

	public Integer getLoyaltyPointsUsed() {
		return loyaltyPointsUsed;
	}

	public void setLoyaltyPointsUsed(int loyaltyPointsUsed) {
		this.loyaltyPointsUsed = loyaltyPointsUsed;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PaymentCard getPaymentCard() {
		return paymentCard;
	}

	public void setPaymentCard(PaymentCard paymentCard) {
		this.paymentCard = paymentCard;
	}

	public LaundryAsset getLaundryAsset() {
		return laundryAsset;
	}

	public void setLaundryAsset(LaundryAsset laundryAsset) {
		this.laundryAsset = laundryAsset;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	@Override
	public String toString() {
		return "Purchase [id=" + id + ", createdDate=" + createdDate + ", user=" + user + "]";
	}
}


