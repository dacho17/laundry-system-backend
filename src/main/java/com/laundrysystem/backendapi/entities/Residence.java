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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "residences")
public class Residence {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "name")
	private String name;
	
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;
	
	@OneToMany(mappedBy = "residence", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private List<UserResidence> userResidences;
	
	@OneToMany(mappedBy = "residence", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	private List<LaundryAsset> laundryAssets;
	
	public Residence() {}

	public Residence(int id, Timestamp createdDate, String name, Address address, List<UserResidence> userResidences,
			List<LaundryAsset> laundryAssets) {
		super();
		this.id = id;
		this.createdDate = createdDate;
		this.name = name;
		this.address = address;
		this.userResidences = userResidences;
		this.laundryAssets = laundryAssets;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<UserResidence> getUserResidences() {
		return userResidences;
	}

	public void setUserResidences(List<UserResidence> userResidences) {
		this.userResidences = userResidences;
	}

	public List<LaundryAsset> getLaundryAssets() {
		return laundryAssets;
	}

	public void setLaundryAssets(List<LaundryAsset> laundryAssets) {
		this.laundryAssets = laundryAssets;
	}

	@Override
	public String toString() {
		return "Residence [id=" + id + ", createdDate=" + createdDate + ", name=" + name 
				+ ", address=" + address + ", userResidences="
				+ userResidences + ", laundryAssets=" + laundryAssets + "]";
	}
}
