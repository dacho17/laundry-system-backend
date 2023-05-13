package com.laundrysystem.backendapi.dtos;

import java.sql.Timestamp;

public class DailyBookingRequestDto {
    private int assetId;
	private Timestamp timeslotFrom;
	private Timestamp timeslotTo;
	
	public DailyBookingRequestDto() {}
	
	public DailyBookingRequestDto(int assetId, Timestamp timeslotFrom, Timestamp timeslotTo) {
		super();
		this.assetId = assetId;
		this.timeslotFrom = timeslotFrom;
		this.timeslotTo = timeslotTo;
	}

	public int getAssetId() {
		return assetId;
	}

	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}

	public Timestamp getTimeslotFrom() {
		return timeslotFrom;
	}

	public void setTimeslotFrom(Timestamp timeslotFrom) {
		this.timeslotFrom = timeslotFrom;
	}

	public Timestamp getTimeslotTo() {
		return timeslotTo;
	}

	public void setTimeslotTo(Timestamp timeslotTo) {
		this.timeslotTo = timeslotTo;
	}

	@Override
	public String toString() {
		return "DailyBookingRequestDto [assetId=" + assetId + ", timeslotFrom=" + timeslotFrom + ", timeslotTo=" + timeslotTo + "]";
	}   
}
