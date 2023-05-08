package com.laundrysystem.backendapi.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.laundrysystem.backendapi.entities.LaundryAssetUse;
import com.laundrysystem.backendapi.repositories.interfaces.ILaundryAssetUseRepository;

import jakarta.persistence.EntityManager;

@Repository
public class LaundryAssetUseRepository implements ILaundryAssetUseRepository {
	@Autowired
	private EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(LaundryAssetUseRepository.class);
	
	public void save(LaundryAssetUse laundryAssetUse) {
		logger.info(String.format("About to store a new laundry asset use {%s} connected to laundryAsset with id=%d.", laundryAssetUse.toString(), laundryAssetUse.getLaundryAsset().getId()));
		
		entityManager.persist(laundryAssetUse);
		
		logger.info(String.format("Successfully stored the new laundry asset use {%s} connected to laundryAsset with id=%d.", laundryAssetUse.toString(), laundryAssetUse.getLaundryAsset().getId()));
	}
}
