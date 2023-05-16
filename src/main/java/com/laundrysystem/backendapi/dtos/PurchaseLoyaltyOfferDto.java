package com.laundrysystem.backendapi.dtos;

public class PurchaseLoyaltyOfferDto {
    private int loyaltyOfferId;

    public PurchaseLoyaltyOfferDto() {}

    public PurchaseLoyaltyOfferDto(int loyaltyOfferId) {
        this.loyaltyOfferId = loyaltyOfferId;
    }

    public int getLoyaltyOfferId() {
        return loyaltyOfferId;
    }

    public void setLoyaltyOfferId(int loyaltyOfferId) {
        this.loyaltyOfferId = loyaltyOfferId;
    }

    @Override
    public String toString() {
        return "PurchaseLoyaltyOfferDto [loyaltyOfferId=" + loyaltyOfferId + "]";
    }
}
