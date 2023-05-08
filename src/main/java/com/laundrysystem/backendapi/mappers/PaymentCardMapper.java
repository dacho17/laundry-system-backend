package com.laundrysystem.backendapi.mappers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.laundrysystem.backendapi.dtos.PaymentCardDto;
import com.laundrysystem.backendapi.dtos.PaymentCardsDto;
import com.laundrysystem.backendapi.dtos.UpdatePaymentCardForm;
import com.laundrysystem.backendapi.entities.PaymentCard;

public class PaymentCardMapper {

	public static PaymentCardsDto getCurrentAndPastPaymentCards(List<PaymentCard> paymentCards) {
		List<PaymentCardDto> inactiveCards = new ArrayList<>();
		PaymentCardDto activeCard = null;
		
		for (PaymentCard card: paymentCards) {
			if (card.isBeingUsed()) {
				activeCard = toDto(card);
			} else {
				inactiveCards.add(toDto(card));
			}
		}
		
		return new PaymentCardsDto(activeCard, inactiveCards);
	}
	
	public static PaymentCardsDto getCurrentAndPastPaymentCards(PaymentCard newPaymentCard, List<PaymentCard> oldPaymentCards) {
		PaymentCardsDto userPaymentCards = getCurrentAndPastPaymentCards(oldPaymentCards);
		userPaymentCards.setCurrentCard(toDto(newPaymentCard));
		
		return userPaymentCards;
	}
	
	public static PaymentCardDto toDto(PaymentCard paymentCard) {
		return new PaymentCardDto(
			paymentCard.getCardHolderName(),
			paymentCard.getExpiryDate(),
			paymentCard.getLastFourDigits()
		);
	}
	
	public static PaymentCard cardFormtoMap(UpdatePaymentCardForm updatePaymentCardForm) {
		// NOTE: CVV is present in the form at the moment, but this value will never hit the server. Used for presentation purposes.
		
		return new PaymentCard(
			new Timestamp(System.currentTimeMillis()),
			true,
			"tempTokenAssignedByTheService",	// NOTE: not assigned by the BE
			updatePaymentCardForm.getCardHolder(),
			updatePaymentCardForm.getExpiryDate(),
			updatePaymentCardForm.getCardNumber().substring(updatePaymentCardForm.getCardNumber().length() - 4)
		);
	}
}
