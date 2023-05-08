package com.laundrysystem.backendapi.dtos;

import java.sql.Timestamp;

import com.laundrysystem.backendapi.enums.ActivityType;

public class PurchaseDto extends ActivityDto {
	private double servicePrice;
	private String currency;
	

	public PurchaseDto(String timeOfActivity, Timestamp chosenTimeslot, String assetName, double servicePrice, String currency, int assetId) {
		super(timeOfActivity, ActivityType.PURCHASE, chosenTimeslot, assetName, assetId);
		this.servicePrice = servicePrice;
		this.currency = currency;
	}


	public double getServicePrice() {
		return servicePrice;
	}


	public void setServicePrice(double servicePrice) {
		this.servicePrice = servicePrice;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	@Override
	public String toString() {
		return "PurchaseDto [servicePrice=" + servicePrice + ", currency=" + currency + ", timeOfActivity="
				+ timeOfActivity + ", activityType=" + activityType + ", chosenTimeslot=" + chosenTimeslot
				+ ", assetName=" + assetName + ", assetId=" + assetId + "]";
	}
}
