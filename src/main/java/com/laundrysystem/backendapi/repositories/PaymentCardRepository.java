package com.laundrysystem.backendapi.repositories;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.laundrysystem.backendapi.entities.PaymentCard;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.repositories.interfaces.IPaymentCardRepository;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Repository
public class PaymentCardRepository implements IPaymentCardRepository {
	@Autowired
	private EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentCardRepository.class);
	
	public List<PaymentCard> getPaymentCardsForUser(int userId) {
		logger.info(String.format("Fetching payment cards for the user with userId=%d.", userId));
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<User> query = cb.createQuery(User.class);
	    Root<User> root = query.from(User.class);
	    query = query.select(root)
			.where(cb.and(
					cb.equal(root.get("id"), userId)
			));
	    
		User user = entityManager.createQuery(query).getSingleResult();
		List<PaymentCard> paymentCards = user.getPaymentCards();
		logger.info(String.format("%d payment cards fetched for the user with userId=%d.", paymentCards.size(), userId));
		
		return paymentCards;
	}
	
	public PaymentCard getActivePaymentCardForUser(int userId) {
		PaymentCard candidateActiveCard = getActiveCard(userId);
		
		return candidateActiveCard;
	}
	
	public PaymentCard updateActiveCardForUser(User user, PaymentCard newPaymentCard) {
		int userId = user.getId();

		PaymentCard activeCard = getActiveCard(userId);
		if (activeCard != null) {
			logger.info(String.format("Marking the currently active card with cardId=%d of user with userId=%d, as unused.", activeCard.getId(), userId));

			activeCard.setBeingUsed(false);
			update(activeCard);
			
			logger.info(String.format("Card with id=%d successfully marked as unused.", activeCard.getId()));
		} else {
			logger.info(String.format("No active cards found for the user with userId=%d.", userId));
			// if (newPaymentCard == null)
			// 	return null;
		}
		
		newPaymentCard.setUser(user);
		save(newPaymentCard);
		
		return newPaymentCard;
	}
	
	public void save(PaymentCard paymentCard) {
		logger.info(String.format("About to store a new payment card for the user userId=%d.", paymentCard.getUser().getId()));
		
		entityManager.persist(paymentCard);
		
		logger.info(String.format("Successfully stored the new payment card id=%d for the user userId=%d.", paymentCard.getId() ,paymentCard.getUser().getId()));
	}
	
	public PaymentCard update(PaymentCard paymentCard) {
		logger.info(String.format("About to update the payment card with id=%d for the user with userId=%d.", paymentCard.getId(), paymentCard.getUser().getId()));
		
		entityManager.merge(paymentCard);
		
		logger.info(String.format("Successfully updated the payment card id=%d for the user with userId=%d.", paymentCard.getId() ,paymentCard.getUser().getId()));

		return paymentCard;
	}
	
	private PaymentCard getActiveCard(int userId) {
		List<PaymentCard> paymentCards = getPaymentCardsForUser(userId);
		logger.info(String.format("Fetching active payment card for user with userId=%d.", userId));
		Optional<PaymentCard> candidateActiveCard = paymentCards.stream().filter((card) -> card.isBeingUsed()).findFirst();
		
		if (candidateActiveCard.isEmpty()) {
			logger.info(String.format("No active payment card found for the user with userId=%d.", userId));
			return null;
		}

		PaymentCard activePaymentCard = candidateActiveCard.get();
		logger.info(String.format("Returning active payment card id=%d for user with userId=%d.", userId, activePaymentCard.getId()));
		return activePaymentCard;
	}
}
