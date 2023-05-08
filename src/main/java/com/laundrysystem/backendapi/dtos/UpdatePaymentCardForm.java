package com.laundrysystem.backendapi.dtos;

public class UpdatePaymentCardForm {
	private String cardNumber;
	private String cardHolder;
	private String expiryDate;
	private String cvv;
	
	public UpdatePaymentCardForm(String cardNumber, String cardHolder, String expiryDate, String cvv) {
		super();
		this.cardNumber = cardNumber;
		this.cardHolder = cardHolder;
		this.expiryDate = expiryDate;
		this.cvv = cvv;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	@Override
	public String toString() {
		return "UpdatePaymentCardForm [cardNumber=" + cardNumber + ", cardHolder=" + cardHolder + ", expiryDate="
				+ expiryDate + ", cvv=" + cvv + "]";
	}
}
