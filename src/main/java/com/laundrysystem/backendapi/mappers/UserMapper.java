package com.laundrysystem.backendapi.mappers;

import java.util.Optional;

import com.laundrysystem.backendapi.dtos.ResidenceAdminDto;
import com.laundrysystem.backendapi.dtos.ResidenceAdminRegForm;
import com.laundrysystem.backendapi.dtos.TenantDto;
import com.laundrysystem.backendapi.dtos.TenantRegForm;
import com.laundrysystem.backendapi.dtos.UpdateUserInfoForm;
import com.laundrysystem.backendapi.dtos.UserDto;
import com.laundrysystem.backendapi.entities.Residence;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.entities.Tenancy;
import com.laundrysystem.backendapi.enums.UserRole;
import com.laundrysystem.backendapi.utils.Formatting;

public class UserMapper {

	public static UpdateUserInfoForm toUserInfoForm(User user) {
		return new UpdateUserInfoForm(
			user.getUsername(),
			user.getEmail(),
			user.getCountryDialCode(),
			user.getMobileNumber()
		);
	}
	
	public static UserDto toDto(User user) {
		return new UserDto(
			user.getUsername(),
			UserRole.getRole(user.getRole()),
			user.getJwt()
		);
	}
	
	public static TenantDto userToTenantDto(User user) {
		Tenancy tenancy = getCurrentTenancy(user);
		long curTs = Formatting.getCurTimestamp().getTime();
		return new TenantDto(
			user.getName(),
			user.getSurname(),
			user.getUsername(),
			tenancy.getTenancyStart(),
			tenancy.getTenancyEnd(),
			tenancy.getTenancyStart().getTime() <= curTs && curTs <= tenancy.getTenancyEnd().getTime(),
			user.getEmail(),
			user.getCountryDialCode(),
			user.getMobileNumber()
		);
	}
	
	public static ResidenceAdminDto userToResidenceAdminDto(User user) {
		return new ResidenceAdminDto(
			user.getUsername(),
			user.getName(),
			user.getSurname(),
			user.getEmail(),
			user.getCountryDialCode(),
			user.getMobileNumber()
		);		
	}

	public static User tenantRegFormToUserMap(TenantRegForm tenantRegForm, Residence residence) {
		User newTenant = new User(
			Formatting.getCurTimestamp(),
			tenantRegForm.getUsername(),
			tenantRegForm.getPassword(),
			UserRole.ROLE_TENANT.getValue(),
			tenantRegForm.getName(),
			tenantRegForm.getSurname(),
			tenantRegForm.getEmail(),
			tenantRegForm.getCountryDialCode(),
			tenantRegForm.getMobileNumber()
		);
		
		Tenancy tenancy = new Tenancy(
			Formatting.getCurTimestamp(),
			tenantRegForm.getTenancyFrom(),
			tenantRegForm.getTenancyTo(),
			newTenant,
			residence
		);
		
		newTenant.addTenancy(tenancy);
		return newTenant;
	}
	
	public static User updateTenant(User user, TenantRegForm tenantRegForm) {
		user.setName(tenantRegForm.getName());
		user.setSurname(tenantRegForm.getSurname());
		user.setUsername(tenantRegForm.getUsername());
		Tenancy curResidence = getCurrentTenancy(user);
		curResidence.setTenancyStart(tenantRegForm.getTenancyFrom());
		curResidence.setTenancyEnd(tenantRegForm.getTenancyTo());
		user.setEmail(tenantRegForm.getEmail());
		user.setCountryDialCode(tenantRegForm.getCountryDialCode());
		user.setMobileNumber(tenantRegForm.getMobileNumber());
		
		return user;
	}
	
	public static User updateResidenceAdmin(User user, ResidenceAdminRegForm regForm) {
		user.setName(regForm.getName());
		user.setSurname(regForm.getSurname());
		user.setUsername(regForm.getUsername());
		user.setEmail(regForm.getEmail());
		user.setCountryDialCode(regForm.getCountryDialCode());
		user.setMobileNumber(regForm.getMobileNumber());
		
		return user;
	}
	
	public static User residenceAdminFormToUserMap(ResidenceAdminRegForm regForm, Residence residence) {
		User newResidenceAdmin = new User(
				Formatting.getCurTimestamp(),
				regForm.getUsername(),
				regForm.getPassword(),
				UserRole.ROLE_RESIDENCE_ADMIN.getValue(),
				regForm.getName(),
				regForm.getSurname(),
				regForm.getEmail(),
				regForm.getCountryDialCode(),
				regForm.getMobileNumber()
			);
			
			Tenancy adminsEmployer = new Tenancy(
				Formatting.getCurTimestamp(),
				newResidenceAdmin,
				residence
			);
			
			newResidenceAdmin.addTenancy(adminsEmployer);
			return newResidenceAdmin;
	}
	
	private static Tenancy getCurrentTenancy(User user) {
		long curTs = Formatting.getCurTimestamp().getTime();
		Optional<Tenancy> usrResidence = user.getTenancies().stream()
			.filter(residence -> (residence.getTenancyStart().getTime() <= curTs && curTs <= residence.getTenancyEnd().getTime())
					|| (residence.getTenancyStart().getTime() >= curTs))
			.findFirst();
		
		if (usrResidence.isEmpty()) return null;
		return usrResidence.get();
	}
}
