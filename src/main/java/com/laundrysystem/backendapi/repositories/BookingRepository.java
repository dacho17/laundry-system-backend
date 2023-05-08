package com.laundrysystem.backendapi.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.laundrysystem.backendapi.entities.Booking;
import com.laundrysystem.backendapi.repositories.interfaces.IBookingRepository;

import jakarta.persistence.EntityManager;

@Repository
public class BookingRepository implements IBookingRepository {
	@Autowired
	EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(BookingRepository.class);
	
	public void save(Booking booking) {
		logger.info(String.format("About to store the newly creted booking connected to the user with Id=[%d]", booking.getUser().getId()));
		
		entityManager.persist(booking);
		
		logger.info(String.format("Successfully stored the booking connected to the user with Id=[%d]", booking.getUser().getId()));
	}
}