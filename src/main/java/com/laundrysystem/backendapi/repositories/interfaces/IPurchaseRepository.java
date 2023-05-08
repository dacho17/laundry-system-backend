package com.laundrysystem.backendapi.repositories.interfaces;

import com.laundrysystem.backendapi.entities.Purchase;

public interface IPurchaseRepository {
	void save(Purchase purchase);
}
