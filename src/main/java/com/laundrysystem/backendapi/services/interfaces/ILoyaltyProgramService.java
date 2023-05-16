package com.laundrysystem.backendapi.services.interfaces;

import java.util.List;

import com.laundrysystem.backendapi.dtos.LoyaltyOfferDto;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;

public interface ILoyaltyProgramService {
    void purchaseLoyaltyOffer(int loyaltyOfferId) throws DbException, EntryNotFoundException;
    List<LoyaltyOfferDto> getLoyaltyOffers() throws DbException;
}
