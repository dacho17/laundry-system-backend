package com.laundrysystem.backendapi.mappers;

import com.laundrysystem.backendapi.dtos.ActivityHistoryEntryDto;
import com.laundrysystem.backendapi.dtos.LoyaltyOfferDto;
import com.laundrysystem.backendapi.entities.LoyaltyOffer;
import com.laundrysystem.backendapi.entities.LoyaltyOfferPurchase;
import com.laundrysystem.backendapi.enums.ActivityHistoryEntryType;
import com.laundrysystem.backendapi.utils.Formatting;

public class LoyaltyOfferMapper {
    public static LoyaltyOfferDto toDTO(LoyaltyOffer loyaltyOffer) {
		return new LoyaltyOfferDto(loyaltyOffer.getId(), loyaltyOffer.getName(), loyaltyOffer.getLoyaltyPoints(),
            loyaltyOffer.getPrice(), loyaltyOffer.getCurrency(), loyaltyOffer.getExpiryDate());
	}

    public static ActivityHistoryEntryDto toActivityHistoryEntry(LoyaltyOfferPurchase loyaltyOfferPurchase) {
      LoyaltyOffer loyaltyOffer = loyaltyOfferPurchase.getLoyaltyOffer();
      return new ActivityHistoryEntryDto(
        Formatting.timestampToDateStr(loyaltyOfferPurchase.getCreatedDate()),
        ActivityHistoryEntryType.OFFER_PURCHASE,
        loyaltyOffer.getPrice(),
        loyaltyOffer.getCurrency(),
        loyaltyOffer.getName(),
        loyaltyOffer.getLoyaltyPoints()
        );
    }
}
