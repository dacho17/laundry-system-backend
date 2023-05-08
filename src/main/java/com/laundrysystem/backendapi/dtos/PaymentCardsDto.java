package com.laundrysystem.backendapi.dtos;

import java.util.List;

public class PaymentCardsDto {
	private PaymentCardDto currentCard;
	private List<PaymentCardDto> inactiveCards;
	
	public PaymentCardsDto(PaymentCardDto currentCard, List<PaymentCardDto> inactiveCards) {
		super();
		this.currentCard = currentCard;
		this.inactiveCards = inactiveCards;
	}

	public PaymentCardDto getCurrentCard() {
		return currentCard;
	}

	public void setCurrentCard(PaymentCardDto currentCard) {
		this.currentCard = currentCard;
	}

	public List<PaymentCardDto> getInactiveCards() {
		return inactiveCards;
	}

	public void setInactiveCards(List<PaymentCardDto> inactiveCards) {
		this.inactiveCards = inactiveCards;
	}

	@Override
	public String toString() {
		return "PaymentCardsDto [currentCard=" + currentCard + ", inactiveCards=" + inactiveCards + "]";
	}
}
