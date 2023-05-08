package com.laundrysystem.backendapi.dtos;

import java.sql.Timestamp;

import com.laundrysystem.backendapi.enums.TimeslotAvailabilityStatus;

public class TimeslotAvailabilityDto {
    private ActivityDto activity;
    private TimeslotAvailabilityStatus status;
    private Timestamp runningTimeEnd;
    private Timestamp bookingSlotEnd;
    
    public TimeslotAvailabilityDto() {}

	public TimeslotAvailabilityDto(ActivityDto activity, TimeslotAvailabilityStatus status, Timestamp runningTimeEnd, Timestamp bookingSlotEnd) {
		super();
		this.activity = activity;
		this.status = status;
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
		return "TimeslotAvailabilityDto [activity=" + activity + ", status=" + status + ", runningTimeEnd="
				+ runningTimeEnd + ", bookingSlotEnd=" + bookingSlotEnd + "]";
	}
}
