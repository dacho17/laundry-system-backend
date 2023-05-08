package com.laundrysystem.backendapi.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.laundrysystem.backendapi.entities.LaundryAsset;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.repositories.interfaces.ILaundryAssetRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Repository
public class LaundryAssetRepository implements ILaundryAssetRepository {
	@Autowired
	private EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(LaundryAssetRepository.class);
	
	public void save(LaundryAsset asset) {
		logger.info(String.format("About to store a new laundry asset for the residence residenceId=%d.", asset.getResidence().getId()));
		
		entityManager.persist(asset);
		
		logger.info(String.format("Successfully stored the new laundry asset id=%d for the residence residenceId=%d.", asset.getId() ,asset.getResidence().getId()));
	}
	
	public LaundryAsset update(LaundryAsset asset) {
		logger.info(String.format("About to update the laundry asset with id=%d for the residence with residenceId=%d.", asset.getId(), asset.getResidence().getId()));
		
		entityManager.merge(asset);
		
		logger.info(String.format("Successfully updated the laundry asset id=%d for the residence with residenceId=%d.", asset.getId(), asset.getResidence().getId()));

		return asset;
	}
	
	public LaundryAsset findById(int id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<LaundryAsset> query = cb.createQuery(LaundryAsset.class);
	    Root<LaundryAsset> root = query.from(LaundryAsset.class);
	    query = query.select(root)
			.where(cb.and(
					cb.equal(root.get("id"), id)
			));
	    
	    try {
			return entityManager.createQuery(query).getSingleResult();			
		} catch (NoResultException exc) {	// exception occurs because getSingleResult throws it if not entries have been found
			logger.warn(String.format("Laundry Asset with id=%d has not been found.", id));
			return null;
		}
	}
}
