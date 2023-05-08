package com.laundrysystem.backendapi.dtos;

public class PaymentCardDto {
	private String cardHolderName;
	private String expiryDate;
	private String lastFourDigits;
	
	public PaymentCardDto(String cardHolderName, String expiryDate, String lastFourDigits) {
		super();
		this.cardHolderName = cardHolderName;
		this.expiryDate = expiryDate;
		this.lastFourDigits = lastFourDigits;
	}
	
	public String getCardHolderName() {
		return cardHolderName;
	}
	
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	
	public String getExpiryDate() {
		return expiryDate;
	}
	
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public String getLastFourDigits() {
		return lastFourDigits;
	}
	
	public void setLastFourDigits(String lastFourDigits) {
		this.lastFourDigits = lastFourDigits;
	}
	
	@Override
	public String toString() {
		return "PaymentCardDto [cardHolderName=" + cardHolderName + ", expiryDate=" + expiryDate + ", lastFourDigits="
				+ lastFourDigits + "]";
	}
}
