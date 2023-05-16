package com.laundrysystem.backendapi.repositories.interfaces;

import java.util.List;

import com.laundrysystem.backendapi.entities.LoyaltyOffer;
import com.laundrysystem.backendapi.entities.LoyaltyOfferPurchase;

public interface ILoyaltyOfferRepository {
    LoyaltyOffer findById(int id);
    void savePurchase(LoyaltyOfferPurchase loyaltyOfferPurchase);
    List<LoyaltyOffer> getLoyaltyOffers();
}
