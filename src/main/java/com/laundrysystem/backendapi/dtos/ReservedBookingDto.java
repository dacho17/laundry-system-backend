package com.laundrysystem.backendapi.dtos;

import java.sql.Timestamp;

public class ReservedBookingDto {
	private int assetId;
	private String assetName;
	private Timestamp fromTimeslot;
	private Timestamp toTimeslot;
	private double servicePrice;
	private String currency;
	
	public ReservedBookingDto(int assetId, String assetName, Timestamp fromTimeslot, Timestamp toTimeslot,
			double servicePrice, String currency) {
		super();
		this.assetId = assetId;
		this.assetName = assetName;
		this.fromTimeslot = fromTimeslot;
		this.toTimeslot = toTimeslot;
		this.servicePrice = servicePrice;
		this.currency = currency;
	}

	public int getAssetId() {
		return assetId;
	}

	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public Timestamp getFromTimeslot() {
		return fromTimeslot;
	}

	public void setFromTimeslot(Timestamp fromTimeslot) {
		this.fromTimeslot = fromTimeslot;
	}

	public Timestamp getToTimeslot() {
		return toTimeslot;
	}

	public void setToTimeslot(Timestamp toTimeslot) {
		this.toTimeslot = toTimeslot;
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
		return "ReservedBookingDto [assetId=" + assetId + ", assetName=" + assetName + ", fromTimeslot=" + fromTimeslot
				+ ", toTimeslot=" + toTimeslot + ", servicePrice=" + servicePrice + ", currency=" + currency + "]";
	}
}
