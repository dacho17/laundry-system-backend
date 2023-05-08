package com.laundrysystem.backendapi.dtos;

import java.util.List;

public class ActiveBookingsDto {
	
	private List<ReservedBookingDto> bookingsToPurchase;
	private List<ReservedBookingDto> purchasedBookings;
//	private List<BookingDto> expiredBookings;
	
	public ActiveBookingsDto(List<ReservedBookingDto> bookingsToPurchase, List<ReservedBookingDto> purchasedBookings) {	// List<BookingDto> expiredBookings
		super();
		this.bookingsToPurchase = bookingsToPurchase;
		this.purchasedBookings = purchasedBookings;
//		this.expiredBookings = expiredBookings;
	}

	public List<ReservedBookingDto> getBookingsToPurchase() {
		return bookingsToPurchase;
	}

	public void setBookingsToPurchase(List<ReservedBookingDto> bookingsToPurchase) {
		this.bookingsToPurchase = bookingsToPurchase;
	}

	public List<ReservedBookingDto> getPurchasedBookings() {
		return purchasedBookings;
	}

	public void setPurchasedBookings(List<ReservedBookingDto> purchasedBookings) {
		this.purchasedBookings = purchasedBookings;
	}

//	public List<BookingDto> getExpiredBookings() {
//		return expiredBookings;
//	}
//
//	public void setExpiredBookings(List<BookingDto> expiredBookings) {
//		this.expiredBookings = expiredBookings;
//	}

	@Override
	public String toString() {
		return "ActiveBookingsDto [bookingsToPurchase=" + bookingsToPurchase + ", purchasedBookings="
				+ purchasedBookings + "]";
	}
}
