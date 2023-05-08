package com.laundrysystem.backendapi.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laundrysystem.backendapi.dtos.PaymentCardsDto;
import com.laundrysystem.backendapi.dtos.UpdatePaymentCardForm;
import com.laundrysystem.backendapi.entities.PaymentCard;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;
import com.laundrysystem.backendapi.helpers.UserDataHelper;
import com.laundrysystem.backendapi.mappers.PaymentCardMapper;
import com.laundrysystem.backendapi.repositories.interfaces.IPaymentCardRepository;
import com.laundrysystem.backendapi.services.interfaces.IPaymentCardService;

import jakarta.transaction.Transactional;

@Service
public class PaymentCardService implements IPaymentCardService {
	@Autowired
	private IPaymentCardRepository paymentCardRepository;

	@Autowired
	private UserDataHelper userDataHelper;
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentCardService.class);
	
	@Transactional
	public PaymentCardsDto getUserPaymentCards() throws EntryNotFoundException, DbException {
		User user = userDataHelper.getActiveUser();
		
		try {
			List<PaymentCard> paymentCards = paymentCardRepository.getPaymentCardsForUser(user.getId());
			if (paymentCards != null) {
				logger.info(String.format("%d cards have been fetched for the user with userId=%d.", paymentCards.size(), user.getId()));
				return PaymentCardMapper.getCurrentAndPastPaymentCards(paymentCards);
			}

			logger.info(String.format("No cards have been fetched for the user with userId=%d.", user.getId()));
			return new PaymentCardsDto(null, new ArrayList<>());
		} catch(Exception exc) {
			logger.error(String.format("An exception occurred while fetching the user's payment cards (userId=%d). - err=[%s]", user.getId(), exc.getStackTrace().toString()));
			throw new DbException();
		}
	}
	
	@Transactional
	public PaymentCardsDto updateUserPaymentCard(UpdatePaymentCardForm updatePaymentCardForm) throws DbException, EntryNotFoundException {
		User user = userDataHelper.getActiveUser();

		PaymentCard newPaymentCard = PaymentCardMapper.cardFormtoMap(updatePaymentCardForm);
		
		try {
			PaymentCard createdPaymentCard = paymentCardRepository.updateActiveCardForUser(user, newPaymentCard);
			List<PaymentCard> oldPaymentCards = user.getPaymentCards();
			PaymentCardsDto userPaymentCards = PaymentCardMapper.getCurrentAndPastPaymentCards(createdPaymentCard, oldPaymentCards);
			
			logger.info(String.format("Returning %d unused cards for the user (userId=%d), and a newly created payment card with id=%d.",
					oldPaymentCards.size(), user.getId(), createdPaymentCard.getId()));

			return userPaymentCards;
		} catch(Exception exc) {
			logger.error(String.format("An exception occurred while updating the user's payment card (userId=%d). - err=[%s]", user.getId(), exc.getStackTrace().toString()));
			throw new DbException();
		}
	}
}
