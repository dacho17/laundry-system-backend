package com.laundrysystem.backendapi.mappers;

import com.laundrysystem.backendapi.utils.Formatting;

import java.sql.Timestamp;

import com.laundrysystem.backendapi.dtos.PurchaseDto;
import com.laundrysystem.backendapi.entities.LaundryAsset;
import com.laundrysystem.backendapi.entities.Purchase;

public class PurchaseMapper {
	
	public static PurchaseDto toDTO(Purchase purchase) {
		LaundryAsset laundryAsset = purchase.getLaundryAsset();
		String timeOfActivity = Formatting.timestampToDateStr(purchase.getCreatedDate());
		Timestamp chosenTimeslot = purchase.getBooking().getTimeslot();
		
		return new PurchaseDto(
			timeOfActivity,
			chosenTimeslot,
			laundryAsset.getName(),
			laundryAsset.getServicePrice(),
			laundryAsset.getCurrency(),
			laundryAsset.getId()
		);
	}
}
