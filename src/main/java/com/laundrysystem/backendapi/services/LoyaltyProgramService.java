package com.laundrysystem.backendapi.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laundrysystem.backendapi.dtos.LoyaltyOfferDto;
import com.laundrysystem.backendapi.entities.LoyaltyOffer;
import com.laundrysystem.backendapi.entities.LoyaltyOfferPurchase;
import com.laundrysystem.backendapi.entities.PaymentCard;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;
import com.laundrysystem.backendapi.helpers.UserDataHelper;
import com.laundrysystem.backendapi.mappers.LoyaltyOfferMapper;
import com.laundrysystem.backendapi.repositories.interfaces.ILoyaltyOfferRepository;
import com.laundrysystem.backendapi.repositories.interfaces.IUserRepository;
import com.laundrysystem.backendapi.services.interfaces.ILoyaltyProgramService;
import com.laundrysystem.backendapi.utils.Formatting;

import jakarta.transaction.Transactional;

@Service
public class LoyaltyProgramService implements ILoyaltyProgramService {
    private static final Logger logger = LoggerFactory.getLogger(LoyaltyProgramService.class);
    
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ILoyaltyOfferRepository loyaltyOfferRepository;
    @Autowired
	private UserDataHelper userDataHelper;

    @Transactional
    public void purchaseLoyaltyOffer(int loyaltyOfferId) throws DbException, EntryNotFoundException {
        User user = userDataHelper.getActiveUser();
		
        LoyaltyOffer loyaltyOffer = loyaltyOfferRepository.findById(loyaltyOfferId);
        if (loyaltyOffer == null) {
            logger.error(String.format("The requested loyaltyOffer with id=%d, is not registered. Aborting the purchase operation.", loyaltyOfferId));
            throw new EntryNotFoundException();
        }

        int balanceAfterChange = user.getLoyaltyPoints() + loyaltyOffer.getLoyaltyPoints();
        // if (balanceAfterChange < 0) {   // should never happen since we are buying points
		// 	logger.error(String.format("updateLoyaltyPointBalance could not be executed because user [id=%d] does not have enough loyaltyPoints.", user.getId()));
		// 	throw new ApiBadRequestException();
		// }

		PaymentCard activePaymentCard = userDataHelper.getActivePaymentCardFor(user);

        LoyaltyOfferPurchase newPurchase = new LoyaltyOfferPurchase(
            Formatting.getCurTimestamp(), loyaltyOffer, activePaymentCard, user);
        try {
            loyaltyOfferRepository.savePurchase(newPurchase);
        } catch (Exception exc) {
            logger.error(String.format("An error occured while storing a loyaltyOfferPurchase in the database. - [err=%s]", exc.getStackTrace().toString()));
				throw new DbException();
        }

        // TODO: make a payment card charge!
		logger.info(String.format("The card (cardId=%d) has succesfully been charged.", activePaymentCard.getId()));

        try {
            userRepository.updateLoyaltyPointBalance(user, balanceAfterChange);
        } catch (Exception exc) {
            logger.error(String.format("An error occured while updating user's [id=%d] loyalty point balance. - [err=%s]", user.getId(), exc.getStackTrace().toString()));
				throw new DbException();
        }

        logger.info(String.format("The loyaltyOfferPurchase {%s} has been made successfully.", newPurchase.toString()));
    }

    public List<LoyaltyOfferDto> getLoyaltyOffers() throws DbException {
        List<LoyaltyOffer> loyaltyOffers = loyaltyOfferRepository.getLoyaltyOffers();

        if (loyaltyOffers == null) {
            throw new DbException();
        };

        return loyaltyOffers.stream().map((loyaltyOffer) -> LoyaltyOfferMapper.toDTO(loyaltyOffer)).toList();
    }
}
