package com.laundrysystem.backendapi.dtos;

import java.sql.Timestamp;

import com.laundrysystem.backendapi.enums.ActivityHistoryEntryType;
import com.laundrysystem.backendapi.enums.LaundryAssetType;

public class ActivityHistoryEntryDto {
    private String timeOfActivity;
    private ActivityHistoryEntryType activityType;
    private double paidAmount;
    private String currency;
    private int loyaltyPointsUsed;
    private LaundryAssetType assetType;
    private Timestamp chosenTimeslot;
    private String assetName;
    private String offerName;
    private int loyaltyPointsPurchased;

    public ActivityHistoryEntryDto() {}

    // constructor for a booking activity
    public ActivityHistoryEntryDto(String timeOfActivity, ActivityHistoryEntryType activityType, LaundryAssetType assetType,
        Timestamp chosenTimeslot, String assetName) {
            this.timeOfActivity = timeOfActivity;
            this.activityType = activityType;
            this.assetType = assetType;
            this.chosenTimeslot = chosenTimeslot;
            this.assetName = assetName;
    }

    // constructor for a purchase of machine in currency
    public ActivityHistoryEntryDto(String timeOfActivity, ActivityHistoryEntryType activityType, double paidAmount, String currency,
        LaundryAssetType assetType, Timestamp chosenTimeslot, String assetName) {
            this.timeOfActivity = timeOfActivity;
            this.activityType = activityType;
            this.paidAmount = paidAmount;
            this.currency = currency;
            this.assetType = assetType;
            this.chosenTimeslot = chosenTimeslot;
            this.assetName = assetName;
    }

    // constuctor for a purchase of machine using loyalty points
    public ActivityHistoryEntryDto(String timeOfActivity, ActivityHistoryEntryType activityType, int loyaltyPointsUsed,
        LaundryAssetType assetType, Timestamp chosenTimeslot, String assetName) {
            this.timeOfActivity = timeOfActivity;
            this.activityType = activityType;
            this.loyaltyPointsUsed = loyaltyPointsUsed;
            this.assetType = assetType;
            this.chosenTimeslot = chosenTimeslot;
            this.assetName = assetName;
    }

    // constructor for a purchase of loyalty offer
    public ActivityHistoryEntryDto(String timeOfActivity, ActivityHistoryEntryType activityType, double paidAmount, String currency,
        String offerName, int loyaltyPointsPurchased) {
            this.timeOfActivity = timeOfActivity;
            this.activityType = activityType;
            this.paidAmount = paidAmount;
            this.currency = currency;
            this.offerName = offerName;
            this.loyaltyPointsPurchased = loyaltyPointsPurchased;
    }

    public String getTimeOfActivity() {
        return timeOfActivity;
    }

    public void setTimeOfActivity(String timeOfActivity) {
        this.timeOfActivity = timeOfActivity;
    }

    public ActivityHistoryEntryType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityHistoryEntryType activityType) {
        this.activityType = activityType;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getLoyaltyPointsUsed() {
        return loyaltyPointsUsed;
    }

    public void setLoyaltyPointsUsed(int loyaltyPointsUsed) {
        this.loyaltyPointsUsed = loyaltyPointsUsed;
    }

    public LaundryAssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(LaundryAssetType assetType) {
        this.assetType = assetType;
    }

    public Timestamp getChosenTimeslot() {
        return chosenTimeslot;
    }

    public void setChosenTimeslot(Timestamp chosenTimeslot) {
        this.chosenTimeslot = chosenTimeslot;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public int getLoyaltyPointsPurchased() {
        return loyaltyPointsPurchased;
    }

    public void setLoyaltyPointsPurchased(int loyaltyPointsPurchased) {
        this.loyaltyPointsPurchased = loyaltyPointsPurchased;
    }

    @Override
    public String toString() {
        return "ActivityHistoryEntryDto [timeOfActivity=" + timeOfActivity + ", activityType=" + activityType
                + ", paidAmount=" + paidAmount + ", currency=" + currency + ", loyaltyPointsUsed=" + loyaltyPointsUsed 
                + ", assetType=" + assetType+ ", chosenTimeslot=" + chosenTimeslot + ", assetName=" + assetName + 
                ", offerName=" + offerName + ", loyaltyPointsPurchased=" + loyaltyPointsPurchased + "]";
    }
}
