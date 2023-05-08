package com.laundrysystem.backendapi.dtos;

import java.sql.Timestamp;

public class BookingRequestDto {
	private int assetId;
	private Timestamp timeslot;
	
	public BookingRequestDto() {}
	
	public BookingRequestDto(int assetId, Timestamp timeslot) {
		super();
		this.assetId = assetId;
		this.timeslot = timeslot;
	}

	public int getAssetId() {
		return assetId;
	}

	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}

	public Timestamp getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(Timestamp timeslot) {
		this.timeslot = timeslot;
	}

	@Override
	public String toString() {
		return "BookingRequestDto [assetId=" + assetId + ", timeslot=" + timeslot + "]";
	}
}
