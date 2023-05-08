package com.laundrysystem.backendapi.dtos;

import java.sql.Timestamp;

import com.laundrysystem.backendapi.enums.ActivityType;

public class ActivityDto {
	protected String timeOfActivity;
	protected ActivityType activityType;
	protected Timestamp chosenTimeslot;
	protected String assetName;
	protected int assetId;
	
	public ActivityDto(String timeOfActivity, ActivityType activityType, Timestamp chosenTimeslot, String assetName, int assetId) {
		super();
		this.timeOfActivity = timeOfActivity;
		this.activityType = activityType;
		this.chosenTimeslot = chosenTimeslot;
		this.assetName = assetName;
		this.assetId = assetId;
	}

	public String getTimeOfActivity() {
		return timeOfActivity;
	}

	public void setTimeOfActivity(String timeOfActivity) {
		this.timeOfActivity = timeOfActivity;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public Timestamp getChosenTimeslot() {
		return chosenTimeslot;
	}

	public void setChosenTimeslot(Timestamp chosenTimeslot) {
		this.chosenTimeslot = chosenTimeslot;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	
	public int getAssetId() {
		return assetId;
	}

	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}


	@Override
	public String toString() {
		return "ActivityDto [timeOfActivity=" + timeOfActivity + ", activityType=" + activityType + ", chosenTimeslot="
				+ chosenTimeslot + ", assetName=" + assetName + ", assetId=" + assetId + "]";
	}
}
