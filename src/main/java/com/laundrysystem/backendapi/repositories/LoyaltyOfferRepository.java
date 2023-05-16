package com.laundrysystem.backendapi.repositories;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.laundrysystem.backendapi.entities.LoyaltyOffer;
import com.laundrysystem.backendapi.entities.LoyaltyOfferPurchase;
import com.laundrysystem.backendapi.repositories.interfaces.ILoyaltyOfferRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Repository
public class LoyaltyOfferRepository implements ILoyaltyOfferRepository {
    @Autowired
	private EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(LoyaltyOfferRepository.class);
	
	public LoyaltyOffer findById(int id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<LoyaltyOffer> query = cb.createQuery(LoyaltyOffer.class);
	    Root<LoyaltyOffer> root = query.from(LoyaltyOffer.class);
	    query = query.select(root)
			.where(cb.and(
					cb.equal(root.get("id"), id)
			));
	    
	    try {
			return entityManager.createQuery(query).getSingleResult();			
		} catch (NoResultException exc) {	// exception occurs because getSingleResult throws it if not entries have been found
			logger.warn(String.format("Loyalty offer with id=%d has not been found.", id));
			return null;
		}
	}

	public void savePurchase(LoyaltyOfferPurchase loyaltyOfferPurchase) {
		logger.info(String.format("About to save the loyaltyOfferPurchase connected to the loyaltyOffer=[%d]", loyaltyOfferPurchase.getLoyaltyOffer().getId()));
		
		entityManager.persist(loyaltyOfferPurchase);
		
		logger.info(String.format("Successfully saved the purchase connected to the loyaltyOffer=[%d]", loyaltyOfferPurchase.getLoyaltyOffer().getId()));
	}

	public List<LoyaltyOffer> getLoyaltyOffers() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<LoyaltyOffer> query = cb.createQuery(LoyaltyOffer.class);
	    Root<LoyaltyOffer> root = query.from(LoyaltyOffer.class);
	    query = query.select(root);

		try {
			return entityManager.createQuery(query).getResultList();			
		} catch (NoResultException exc) {	// exception occurs because getSingleResult throws it if not entries have been found
			logger.error(String.format("Loyalty offers could not be retrieved from the database. - [exc=%s]", exc.getStackTrace().toString()));
			return null;
		}
	}
}
