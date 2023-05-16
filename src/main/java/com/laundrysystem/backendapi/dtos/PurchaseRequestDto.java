package com.laundrysystem.backendapi.dtos;

public class PurchaseRequestDto {
    private int assetId;
    private boolean isPayingWithLoyaltyPoints;

    public PurchaseRequestDto() {}

    public PurchaseRequestDto(int assetId, boolean isPayingWithLoyaltyPoints) {
        this.assetId = assetId;
        this.isPayingWithLoyaltyPoints = isPayingWithLoyaltyPoints;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public boolean getIsPayingWithLoyaltyPoints() {
        return isPayingWithLoyaltyPoints;
    }

    public void setIsPayingWithLoyaltyPoints(boolean isPayingWithLoyaltyPoints) {
        this.isPayingWithLoyaltyPoints = isPayingWithLoyaltyPoints;
    }

    @Override
    public String toString() {
        return "PurchaseRequestDto [assetId=" + assetId + ", isPayingWithLoyaltyPoints=" + isPayingWithLoyaltyPoints
                + "]";
    }
}
