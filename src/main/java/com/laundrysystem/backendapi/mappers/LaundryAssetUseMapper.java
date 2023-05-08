package com.laundrysystem.backendapi.mappers;

import java.sql.Timestamp;

import com.laundrysystem.backendapi.dtos.ReservedBookingDto;
import com.laundrysystem.backendapi.entities.Booking;
import com.laundrysystem.backendapi.entities.LaundryAsset;
import com.laundrysystem.backendapi.entities.LaundryAssetUse;
import com.laundrysystem.backendapi.utils.Formatting;

public class LaundryAssetUseMapper {
	private static final int MINUTE_MILISECONDS_MULTIPLIER = 60000;
	
	public static ReservedBookingDto MAPtoReservedBooking(LaundryAssetUse laundryAssetUse) {
		return new ReservedBookingDto(laundryAssetUse.getLaundryAsset().getId(),
			laundryAssetUse.getLaundryAsset().getName(),
			laundryAssetUse.getStartTime(),
			laundryAssetUse.getEndTime(),
			laundryAssetUse.getLaundryAsset().getServicePrice(),
			laundryAssetUse.getLaundryAsset().getCurrency()
		);
	}
	
	public static LaundryAssetUse toMAP(LaundryAsset laundryAsset, Booking booking) {
		Timestamp curTimestamp = Formatting.getCurTimestamp();
		Timestamp endOfRunTimestamp = new Timestamp(curTimestamp.getTime() + laundryAsset.getRunningTime() * MINUTE_MILISECONDS_MULTIPLIER);
		return new LaundryAssetUse(
			curTimestamp,
			curTimestamp,	// TODO/NOTE: in demo the asset is being immediately run after purchase
			endOfRunTimestamp,
			booking,
			laundryAsset
		);
	}
}
