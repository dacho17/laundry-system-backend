package com.laundrysystem.backendapi.dtos;

import java.sql.Timestamp;

public class LoyaltyOfferDto {
    private int id;
    private String name;
    private int loyaltyPoints;
	private double price;
    private String currency;
	private Timestamp expiryDate;

    public LoyaltyOfferDto() {}

    public LoyaltyOfferDto(int id, String name, int loyaltyPoints, double price, String currency,
            Timestamp expiryDate) {
        this.id = id;
        this.name = name;
        this.loyaltyPoints = loyaltyPoints;
        this.price = price;
        this.currency = currency;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "LoyaltyOfferDto [id=" + id + ", name=" + name + ", loyaltyPoints=" + loyaltyPoints + ", price=" + price
                + ", currency=" + currency + ", expiryDate=" + expiryDate + "]";
    }
}
