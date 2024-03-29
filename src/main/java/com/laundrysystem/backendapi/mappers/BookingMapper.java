package com.laundrysystem.backendapi.mappers;

import java.sql.Timestamp;

import com.laundrysystem.backendapi.dtos.BookingDto;
import com.laundrysystem.backendapi.entities.Booking;
import com.laundrysystem.backendapi.entities.LaundryAsset;
import com.laundrysystem.backendapi.utils.Formatting;

public class BookingMapper {
	public static BookingDto toDTO(Booking booking) {
		LaundryAsset laundryAsset = booking.getLaundryAsset();
		String timeOfActivity = Formatting.timestampToDateStr(booking.getCreatedDate());
		Timestamp chosenTimeslot = booking.getTimeslot();
		
		return new BookingDto(timeOfActivity, chosenTimeslot, laundryAsset.getName(), laundryAsset.getId());
	}
}
