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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "timeslot")
	private Timestamp timeslot;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "laundry_asset_id", referencedColumnName = "id")
	private LaundryAsset laundryAsset;
	
	@OneToMany(mappedBy = "booking", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private List<LaundryAssetUse> laundryAssetUses;
	
	@OneToOne(mappedBy = "booking")
	private Purchase purchase;

	public Booking() {}
	
	public Booking(Timestamp createdDate, Timestamp timeslot, User user, LaundryAsset laundryAsset) {
		super();
		this.createdDate = createdDate;
		this.timeslot = timeslot;
		this.user = user;
		this.laundryAsset = laundryAsset;
	}
	
	public Booking(int id, Timestamp createdDate, Timestamp timeslot, User user, LaundryAsset laundryAsset) {
		super();
		this.id = id;
		this.createdDate = createdDate;
		this.timeslot = timeslot;
		this.user = user;
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

	public Timestamp getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(Timestamp timeslot) {
		this.timeslot = timeslot;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LaundryAsset getLaundryAsset() {
		return laundryAsset;
	}

	public void setLaundryAsset(LaundryAsset laundryAsset) {
		this.laundryAsset = laundryAsset;
	}
	
	public Purchase getPurchase() {
		return purchase;
	}
	
	public List<LaundryAssetUse> getLaundryAssetUses() {
		return this.laundryAssetUses;
	}

	@Override
	public String toString() {
		return "Booking [id=" + id + ", createdDate=" + createdDate + ", timeslot=" + timeslot + "]";
	}
}
