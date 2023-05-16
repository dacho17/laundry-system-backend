package com.laundrysystem.backendapi.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "loyalty_offers")
public class LoyaltyOffer {
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

    @Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "name")
	private String name;

    @Column(name = "loyalty_points")
    private int loyaltyPoints;

    @Column(name = "price")
	private double price;

    @Column(name = "currency")
    private String currency;

    @Column(name = "expiry_date")
	private Timestamp expiryDate;

    @OneToMany(mappedBy = "loyaltyOffer", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
		CascadeType.DETACH, CascadeType.REFRESH})
	private List<LoyaltyOfferPurchase> loyaltyOfferPurchases;

    public LoyaltyOffer() {}

    public LoyaltyOffer(Timestamp createdDate, String name, int loyaltyPoints, double price, String currency, Timestamp expiryDate) {
        this.createdDate = createdDate;
        this.name = name;
        this.loyaltyPoints = loyaltyPoints;
        this.price = price;
        this.currency = currency;
        this.expiryDate = expiryDate;
    }

    public LoyaltyOffer(Timestamp createdDate, String name, int loyaltyPoints, double price, String currency, Timestamp expiryDate,
        List<LoyaltyOfferPurchase> loyaltyOfferPurchases) {
        this.createdDate = createdDate;
        this.name = name;
        this.loyaltyPoints = loyaltyPoints;
        this.price = price;
        this.currency = currency;
        this.expiryDate = expiryDate;
        this.loyaltyOfferPurchases = loyaltyOfferPurchases;
    }

    public int getId() {
        return id;
    }

    public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public int getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(int loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

    public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

    public Timestamp getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

    public List<LoyaltyOfferPurchase> getLoyaltyOfferPurchases() {
		return loyaltyOfferPurchases;
	}

	public void setLoyaltyOfferPurchases(List<LoyaltyOfferPurchase> loyaltyOfferPurchases) {
		this.loyaltyOfferPurchases = loyaltyOfferPurchases;
	}

    @Override
    public String toString() {
        return "LoyaltyOffer [id=" + id + ", createdDate=" + createdDate + ", name=" + name + ", loyaltyPoints="
                + loyaltyPoints + ", price=" + price + ", currency=" + currency + ", expiryDate=" + expiryDate + "]";
    }
}
