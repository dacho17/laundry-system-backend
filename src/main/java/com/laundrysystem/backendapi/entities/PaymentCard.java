package com.laundrysystem.backendapi.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_cards")
public class PaymentCard {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "is_being_used")
	private boolean isBeingUsed;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "card_holder_name")
	private String cardHolderName;
	
	@Column(name = "expiry_date")
	private String expiryDate;
	
	@Column(name = "last_four_digits")
	private String lastFourDigits;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@OneToMany(mappedBy = "paymentCard", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private List<Purchase> purchases;

	@OneToMany(mappedBy = "paymentCard", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
		CascadeType.DETACH, CascadeType.REFRESH})
	private List<LoyaltyOfferPurchase> loyaltyOfferPurchases;

	public PaymentCard() {}

	public PaymentCard(Timestamp createdDate, boolean isBeingUsed, String token, String cardHolderName,
			String expiryDate, String lastFourDigits) {
		super();
		this.createdDate = createdDate;
		this.isBeingUsed = isBeingUsed;
		this.token = token;
		this.cardHolderName = cardHolderName;
		this.expiryDate = expiryDate;
		this.lastFourDigits = lastFourDigits;
	}
	
	public PaymentCard(int id, Timestamp createdDate, boolean isBeingUsed, String token, String cardHolderName,
			String expiryDate, String lastFourDigits, User user, List<Purchase> purchases, List<LoyaltyOfferPurchase> loyaltyOfferPurchases) {
		super();
		this.createdDate = createdDate;
		this.isBeingUsed = isBeingUsed;
		this.token = token;
		this.cardHolderName = cardHolderName;
		this.expiryDate = expiryDate;
		this.lastFourDigits = lastFourDigits;
		this.user = user;
		this.purchases = purchases;
		this.loyaltyOfferPurchases = loyaltyOfferPurchases;
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

	public boolean isBeingUsed() {
		return isBeingUsed;
	}

	public void setBeingUsed(boolean isBeingUsed) {
		this.isBeingUsed = isBeingUsed;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getLastFourDigits() {
		return lastFourDigits;
	}

	public void setLastFourDigits(String lastFourDigits) {
		this.lastFourDigits = lastFourDigits;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	public List<LoyaltyOfferPurchase> getLoyaltyOfferPurchases() {
		return loyaltyOfferPurchases;
	}

	public void setLoyaltyOfferPurchases(List<LoyaltyOfferPurchase> loyaltyOfferPurchases) {
		this.loyaltyOfferPurchases = loyaltyOfferPurchases;
	}

	@Override
	public String toString() {
		return "PaymentCard [id=" + id + ", createdDate=" + createdDate + ", isBeingUsed=" + isBeingUsed + ", token="
				+ token + ", cardHolderName=" + cardHolderName + ", expiryDate=" + expiryDate + ", lastFourDigits="
				+ lastFourDigits + "]";
	}
}
