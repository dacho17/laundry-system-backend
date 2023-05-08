package com.laundrysystem.backendapi.repositories.interfaces;

import com.laundrysystem.backendapi.entities.LaundryAsset;

public interface ILaundryAssetRepository {
	void save(LaundryAsset laundryAsset);
	LaundryAsset update(LaundryAsset laundryAsset);
	LaundryAsset findById(int id);
}
