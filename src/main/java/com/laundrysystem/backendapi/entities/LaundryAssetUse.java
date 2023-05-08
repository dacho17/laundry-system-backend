package com.laundrysystem.backendapi.entities;

import java.sql.Timestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "laundry_asset_uses")
public class LaundryAssetUse {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "start_time")
	private Timestamp startTime;
	
	@Column(name = "end_time")
	private Timestamp endTime;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "booking_id", referencedColumnName = "id")
	private Booking booking;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "laundry_asset_id", referencedColumnName = "id")
	private LaundryAsset laundryAsset;

	public LaundryAssetUse() {}

	public LaundryAssetUse(Timestamp createdDate, Timestamp startTime, Timestamp endTime, Booking booking,
			LaundryAsset laundryAsset) {
		super();
		this.createdDate = createdDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.booking = booking;
		this.laundryAsset = laundryAsset;
	}
	
	public LaundryAssetUse(int id, Timestamp createdDate, Timestamp startTime, Timestamp endTime, Booking booking,
			LaundryAsset laundryAsset) {
		super();
		this.id = id;
		this.createdDate = createdDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.booking = booking;
		this.laundryAsset = laundryAsset;
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

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public LaundryAsset getLaundryAsset() {
		return laundryAsset;
	}

	public void setLaundryAsset(LaundryAsset laundryAsset) {
		this.laundryAsset = laundryAsset;
	}

	@Override
	public String toString() {
		return "LaundryAssetUse [id=" + id + ", createdDate=" + createdDate + ", startTime=" + startTime + ", endTime="
				+ endTime + "]";
	}
}
