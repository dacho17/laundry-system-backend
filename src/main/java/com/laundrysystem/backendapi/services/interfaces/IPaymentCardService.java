package com.laundrysystem.backendapi.services.interfaces;

import com.laundrysystem.backendapi.dtos.PaymentCardsDto;
import com.laundrysystem.backendapi.dtos.UpdatePaymentCardForm;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;

public interface IPaymentCardService {
	PaymentCardsDto getUserPaymentCards() throws EntryNotFoundException, DbException;
	PaymentCardsDto updateUserPaymentCard(UpdatePaymentCardForm updatePaymentCardForm) throws EntryNotFoundException, DbException;
}
