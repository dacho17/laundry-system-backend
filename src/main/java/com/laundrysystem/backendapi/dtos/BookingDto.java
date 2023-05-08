package com.laundrysystem.backendapi.dtos;

import java.sql.Timestamp;

import com.laundrysystem.backendapi.enums.ActivityType;

public class BookingDto extends ActivityDto {
	
	public BookingDto(String timeOfActivity, Timestamp chosenTimeslot, String assetName, int assetId) {
		super(timeOfActivity, ActivityType.BOOKING, chosenTimeslot, assetName, assetId);
	}

	@Override
	public String toString() {
		return "BookingDto [timeOfActivity=" + timeOfActivity + ", activityType=" + activityType + ", chosenTimeslot="
				+ chosenTimeslot + ", assetName=" + assetName + ", assetId=" + assetId + "]";
	}
}
