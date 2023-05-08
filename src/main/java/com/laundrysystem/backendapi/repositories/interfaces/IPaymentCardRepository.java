package com.laundrysystem.backendapi.repositories.interfaces;

import java.util.List;

import com.laundrysystem.backendapi.entities.PaymentCard;
import com.laundrysystem.backendapi.entities.User;
public interface IPaymentCardRepository {
	List<PaymentCard> getPaymentCardsForUser(int userId);
	PaymentCard getActivePaymentCardForUser(int userId);
	PaymentCard updateActiveCardForUser(User user, PaymentCard paymentCard);
	void save(PaymentCard paymentCard);
}
