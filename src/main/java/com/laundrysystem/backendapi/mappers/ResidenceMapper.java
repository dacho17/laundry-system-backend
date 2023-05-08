package com.laundrysystem.backendapi.mappers;

import com.laundrysystem.backendapi.dtos.ResidenceDto;
import com.laundrysystem.backendapi.entities.Address;
import com.laundrysystem.backendapi.entities.Residence;

public class ResidenceMapper {

	public static ResidenceDto toDto(Residence residence) {
		Address address = residence.getAddress();
		
		return new ResidenceDto(
			residence.getName(),
			address.getStreetName(),
			address.getStreetNumber(),
			address.getPostalCode(),
			address.getCity(),
			address.getCountry()
		);
	}
}
