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
@Table(name = "user_residence")
public class UserResidence {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "tenancy_start")
	private Timestamp tenancyStart;
	
	@Column(name = "tenancy_end")
	private Timestamp tenancyEnd;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "residence_id", referencedColumnName = "id")
	private Residence residence;

	public UserResidence() {}

	public UserResidence(Timestamp createdDate, User user, Residence residence) {
		this.createdDate = createdDate;
		this.user = user;
		this.residence = residence;
	}
	
	public UserResidence(Timestamp createdDate, Timestamp tenancyStart, Timestamp tenancyEnd, User user, Residence residence) {
		this.createdDate = createdDate;
		this.tenancyStart = tenancyStart;
		this.tenancyEnd = tenancyEnd;
		this.user = user;
		this.residence = residence;
	}
	
	public UserResidence(int id, Timestamp createdDate, Timestamp tenancyStart, User user, Residence residence) {
		super();
		this.id = id;
		this.createdDate = createdDate;
		this.tenancyStart = tenancyStart;
		this.user = user;
		this.residence = residence;
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

	public Timestamp getTenancyStart() {
		return tenancyStart;
	}
	
	public void setTenancyStart(Timestamp tenancyStart) {
		this.tenancyStart = tenancyStart;
	}
	
	public Timestamp getTenancyEnd() {
		return tenancyEnd;
	}
	
	public void setTenancyEnd(Timestamp tenancyEnd) {
		this.tenancyEnd = tenancyEnd;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Residence getResidence() {
		return residence;
	}

	public void setResidence(Residence residence) {
		this.residence = residence;
	}

	@Override
	public String toString() {
		return "UserResidence [id=" + id + ", createdDate=" + createdDate + ", tenancyStart=" + tenancyStart
				+ ", tenancyEnd=" + tenancyEnd + ", user=" + user
				+ ", residence=" + residence + "]";
	}
}
