package com.laundrysystem.backendapi.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.laundrysystem.backendapi.entities.Purchase;
import com.laundrysystem.backendapi.repositories.interfaces.IPurchaseRepository;

import jakarta.persistence.EntityManager;

@Repository
public class PurchaseRepository implements IPurchaseRepository {
	@Autowired
	EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(PurchaseRepository.class);
	
	public void save(Purchase purchase) {
		logger.info(String.format("About to save the purchase connected to the bookingId=[%d]", purchase.getBooking().getId()));
		
		entityManager.persist(purchase);
		
		logger.info(String.format("Successfully saved the purchase connected to the bookingId=[%d]", purchase.getBooking().getId()));
	}
}
