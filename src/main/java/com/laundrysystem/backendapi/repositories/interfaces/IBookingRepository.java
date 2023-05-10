package com.laundrysystem.backendapi.repositories.interfaces;

import com.laundrysystem.backendapi.entities.Booking;

public interface IBookingRepository {
	void save(Booking booking);
	Booking update (Booking booking);
}
