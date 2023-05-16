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
import jakarta.persistence.Table;

@Entity
@Table(name = "loyalty_offer_purchases")
public class LoyaltyOfferPurchase {
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

    @Column(name = "created_date")
	private Timestamp createdDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
        CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "loyalty_offer_id", referencedColumnName = "id")
    private LoyaltyOffer loyaltyOffer;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "payment_card_id", referencedColumnName = "id")
    private PaymentCard paymentCard;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public LoyaltyOfferPurchase() {}

    public LoyaltyOfferPurchase(Timestamp createdDate, LoyaltyOffer loyaltyOffer, PaymentCard paymentCard, User user) {
        this.createdDate = createdDate;
        this.loyaltyOffer = loyaltyOffer;
        this.paymentCard = paymentCard;
        this.user = user;
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

    public LoyaltyOffer getLoyaltyOffer() {
        return loyaltyOffer;
    }

    public void setLoyaltyOffer(LoyaltyOffer loyaltyOffer) {
        this.loyaltyOffer = loyaltyOffer;
    }

    public PaymentCard getPaymentCard() {
        return paymentCard;
    }

    public void setPaymentCard(PaymentCard paymentCard) {
        this.paymentCard = paymentCard;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoyaltyOfferPurchase [id=" + id + ", createdDate=" + createdDate + ", loyaltyOffer=" + loyaltyOffer
                + ", paymentCard=" + paymentCard + ", user=" + user + "]";
    }
}
