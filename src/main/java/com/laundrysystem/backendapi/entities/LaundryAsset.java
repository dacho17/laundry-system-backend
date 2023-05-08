package com.laundrysystem.backendapi.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "laundry_assets")
public class LaundryAsset {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "asset_type")
	private short assetType;
	
	@Column(name = "running_time")
	private short runningTime;
	
	@Column(name = "service_price")
	private double servicePrice;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "is_operational")
	private boolean isOperational;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "residence_id", referencedColumnName = "id")
	private Residence residence;
	
	@OneToMany(mappedBy = "laundryAsset", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private List<Booking> bookings;
	
	@OneToMany(mappedBy = "laundryAsset", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private List<Purchase> purchases;
	
	@OneToMany(mappedBy = "laundryAsset", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private List<LaundryAssetUse> laundryAssetUses;

	public LaundryAsset() {}
	
	public LaundryAsset(Timestamp createdDate, String name, short assetType, short runningTime,
			double servicePrice, String currency, boolean isOperational, Residence residence) {
		this.createdDate = createdDate;
		this.name = name;
		this.assetType = assetType;
		this.runningTime = runningTime;
		this.servicePrice = servicePrice;
		this.currency = currency;
		this.isOperational = isOperational;
		this.residence = residence;
	}

	public LaundryAsset(int id, Timestamp createdDate, String name, short assetType, short runningTime,
			double servicePrice, String currency, boolean isOperational, Residence residence, List<Booking> bookings,
			List<Purchase> purchases) {
		super();
		this.id = id;
		this.createdDate = createdDate;
		this.name = name;
		this.assetType = assetType;
		this.runningTime = runningTime;
		this.servicePrice = servicePrice;
		this.currency = currency;
		this.isOperational = isOperational;
		this.residence = residence;
		this.bookings = bookings;
		this.purchases = purchases;
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

	public Residence getResidence() {
		return residence;
	}

	public void setResidence(Residence residence) {
		this.residence = residence;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}
	
	public List<LaundryAssetUse> getLaundryAssetUses() {
		return this.laundryAssetUses;
	}

	@Override
	public String toString() {
		return "LaundryAsset [id=" + id + ", createdDate=" + createdDate + ", name=" + name + ", assetType=" + assetType
				+ ", runningTime=" + runningTime + ", servicePrice=" + servicePrice + ", currency=" + currency
				+ ", isOperational=" + isOperational + ", residence=" + residence + ", bookings=" + bookings
				+ ", purchases=" + purchases + "]";
	}
}
