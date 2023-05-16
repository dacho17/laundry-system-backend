package com.laundrysystem.backendapi.mappers;

import com.laundrysystem.backendapi.utils.Formatting;

import java.sql.Timestamp;

import com.laundrysystem.backendapi.dtos.ActivityHistoryEntryDto;
import com.laundrysystem.backendapi.dtos.PurchaseDto;
import com.laundrysystem.backendapi.entities.LaundryAsset;
import com.laundrysystem.backendapi.entities.Purchase;
import com.laundrysystem.backendapi.enums.ActivityHistoryEntryType;
import com.laundrysystem.backendapi.enums.LaundryAssetType;

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

	public static ActivityHistoryEntryDto toActivityHistoryEntry(Purchase purchase) {
		if (purchase.getPaymentCard() != null) {	// purchase made using cash
			return new ActivityHistoryEntryDto(
				Formatting.timestampToDateStr(purchase.getCreatedDate()),
				ActivityHistoryEntryType.ASSET_PURCHASE,
				purchase.getAmountPaid().doubleValue(),
				purchase.getPaidInCurrency(),
				LaundryAssetType.getType(purchase.getLaundryAsset().getAssetType()),
				purchase.getBooking().getTimeslot(),
				purchase.getLaundryAsset().getName()
			);
		}

		return new ActivityHistoryEntryDto(
			Formatting.timestampToDateStr(purchase.getCreatedDate()),
			ActivityHistoryEntryType.ASSET_PURCHASE,
			purchase.getLoyaltyPointsUsed(),
			LaundryAssetType.getType(purchase.getLaundryAsset().getAssetType()),
			purchase.getBooking().getTimeslot(),
			purchase.getLaundryAsset().getName()
		);
	}
}
