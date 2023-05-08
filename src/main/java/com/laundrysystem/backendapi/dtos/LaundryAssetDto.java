package com.laundrysystem.backendapi.dtos;

import com.laundrysystem.backendapi.enums.LaundryAssetType;

public class LaundryAssetDto {
	private int id;
	private String name;
	private LaundryAssetType assetType;
	private short runningTime;
	private double servicePrice;
	private String currency;
	private boolean isOperational;
	
	public LaundryAssetDto() {}
	
	public LaundryAssetDto(int id, String name, LaundryAssetType assetType, short runningTime,
		boolean isOperational, double servicePrice, String currency) {
		super();
		this.id = id;
		this.name = name;
		this.assetType = assetType;
		this.runningTime = runningTime;
		this.isOperational = isOperational;
		this.servicePrice = servicePrice;
		this.currency = currency;
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

	public LaundryAssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(LaundryAssetType assetType) {
		this.assetType = assetType;
	}

	public short getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(short runningTime) {
		this.runningTime = runningTime;
	}

	public double getServicePrice() {
		return this.servicePrice;
	}

	public void setServicePrice(double servicePrice) {
		this.servicePrice = servicePrice;
	}
	
	public String getCurrency() {
		return this.currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean getIsOperational() {
		return isOperational;
	}

	public void setIsOperational(boolean isOperational) {
		this.isOperational = isOperational;
	}

	@Override
	public String toString() {
		return "LaundryAssetDto [id=" + id + ", name=" + name + ", assetType=" + assetType + ", runningTime="
				+ runningTime + ", servicePrice=" + servicePrice + ", currency=" + currency + ", isOperational="
				+ isOperational + "]";
	}
}
