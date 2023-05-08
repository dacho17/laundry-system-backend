package com.laundrysystem.backendapi.mappers;

import com.laundrysystem.backendapi.dtos.LaundryAssetDto;
import com.laundrysystem.backendapi.dtos.LaundryAssetRegForm;
import com.laundrysystem.backendapi.entities.LaundryAsset;
import com.laundrysystem.backendapi.entities.Residence;
import com.laundrysystem.backendapi.enums.LaundryAssetType;
import com.laundrysystem.backendapi.utils.Formatting;

public class LaundryAssetMapper {

	public static LaundryAssetDto toDto(LaundryAsset laundryAsset) {
		return new LaundryAssetDto(
			laundryAsset.getId(),
			laundryAsset.getName(),
			LaundryAssetType.getType(laundryAsset.getAssetType()),
			laundryAsset.getRunningTime(),
			laundryAsset.getIsOperational(),
			laundryAsset.getServicePrice(),
			laundryAsset.getCurrency()
		);
	}
	
	public static LaundryAsset regFormToMap(LaundryAssetRegForm regForm, Residence residence) {		
		return new LaundryAsset(
			Formatting.getCurTimestamp(),
			regForm.getName(),
			regForm.getAssetType(),
			regForm.getRunningTime(),
			regForm.getServicePrice(),
			regForm.getCurrency(),
			regForm.getIsOperational(),
			residence
		);
	}
	
	public static LaundryAsset updateLaundryAsset(LaundryAsset laundryAssetToUpdate, LaundryAssetRegForm laundryAssetRegForm) {
		laundryAssetToUpdate.setName(laundryAssetRegForm.getName());
		laundryAssetToUpdate.setRunningTime(laundryAssetRegForm.getRunningTime());
		laundryAssetToUpdate.setIsOperational(laundryAssetRegForm.getIsOperational());
		laundryAssetRegForm.setServicePrice(laundryAssetRegForm.getServicePrice());
		laundryAssetRegForm.setCurrency(laundryAssetRegForm.getCurrency());
		
		return laundryAssetToUpdate;
	}
}
