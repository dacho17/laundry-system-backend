package com.laundrysystem.backendapi.dtos;

public class LaundryAssetRegForm {
	private int id;
	private String name;
	private short assetType;
	private short runningTime;
	private double servicePrice;
	private String currency;
	private boolean isOperational;
	
	public LaundryAssetRegForm() {}

	public LaundryAssetRegForm(int id, String name, short assetType, short runningTime, double servicePrice,
			String currency, boolean isOperational) {
		super();
		this.id = id;
		this.name = name;
		this.assetType = assetType;
		this.runningTime = runningTime;
		this.servicePrice = servicePrice;
		this.currency = currency;
		this.isOperational = isOperational;
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

	public short getAssetType() {
		return assetType;
	}

	public void setAssetType(short assetType) {
		this.assetType = assetType;
	}

	public short getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(short runningTime) {
		this.runningTime = runningTime;
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

	public boolean getIsOperational() {
		return isOperational;
	}

	public void setIsOperational(boolean isOperational) {
		this.isOperational = isOperational;
	}

	@Override
	public String toString() {
		return "LaundryAssetRegForm [id=" + id + ", name=" + name + ", assetType=" + assetType + ", runningTime=" + runningTime
				+ ", servicePrice=" + servicePrice + ", currency=" + currency + ", isOperational=" + isOperational
				+ "]";
	}
}
