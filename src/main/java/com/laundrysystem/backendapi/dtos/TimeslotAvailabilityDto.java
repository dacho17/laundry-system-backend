package com.laundrysystem.backendapi.dtos;

import java.sql.Timestamp;

import com.laundrysystem.backendapi.enums.TimeslotAvailabilityStatus;

public class TimeslotAvailabilityDto {
    private ActivityDto activity;
    private TimeslotAvailabilityStatus status;
	private boolean isAssetOperational;
    private Timestamp runningTimeEnd;
    private Timestamp bookingSlotEnd;
    
    public TimeslotAvailabilityDto() {}

	public TimeslotAvailabilityDto(ActivityDto activity, TimeslotAvailabilityStatus status, boolean isAssetOperational,
		Timestamp runningTimeEnd, Timestamp bookingSlotEnd) {
		super();
		this.activity = activity;
		this.status = status;
		this.isAssetOperational = isAssetOperational;
		this.runningTimeEnd = runningTimeEnd;
		this.bookingSlotEnd = bookingSlotEnd;
	}

	public ActivityDto getActivity() {
		return activity;
	}

	public void setActivity(ActivityDto activity) {
		this.activity = activity;
	}

	public TimeslotAvailabilityStatus getStatus() {
		return status;
	}

	public void setStatus(TimeslotAvailabilityStatus status) {
		this.status = status;
	}

	public boolean getIsAssetOperational() {
		return isAssetOperational;
	}

	public void setIsAssetOperational(boolean isAssetOperational) {
		this.isAssetOperational = isAssetOperational;
	}

	public Timestamp getRunningTimeEnd() {
		return runningTimeEnd;
	}

	public void setRunningTimeEnd(Timestamp runningTimeEnd) {
		this.runningTimeEnd = runningTimeEnd;
	}
	
	public Timestamp getBookingSlotEnd() {
		return bookingSlotEnd;
	}

	public void setbookingSlotEnd(Timestamp bookingSlotEnd) {
		this.bookingSlotEnd = bookingSlotEnd;
	}

	@Override
	public String toString() {
		return "TimeslotAvailabilityDto [activity=" + activity + ", status=" 
			+ status + ", isAssetOperational=" + isAssetOperational + ", runningTimeEnd="
			+ runningTimeEnd + ", bookingSlotEnd=" + bookingSlotEnd + "]";
	}
}
