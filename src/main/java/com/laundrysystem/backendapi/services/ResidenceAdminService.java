package com.laundrysystem.backendapi.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.laundrysystem.backendapi.dtos.LaundryAssetDto;
import com.laundrysystem.backendapi.dtos.LaundryAssetRegForm;
import com.laundrysystem.backendapi.dtos.ResidenceAdminDto;
import com.laundrysystem.backendapi.dtos.ResidenceAdminRegForm;
import com.laundrysystem.backendapi.dtos.TenantDto;
import com.laundrysystem.backendapi.dtos.TenantRegForm;
import com.laundrysystem.backendapi.entities.LaundryAsset;
import com.laundrysystem.backendapi.entities.Residence;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.enums.UserRole;
import com.laundrysystem.backendapi.exceptions.ApiBadRequestException;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;
import com.laundrysystem.backendapi.helpers.UserDataHelper;
import com.laundrysystem.backendapi.mappers.LaundryAssetMapper;
import com.laundrysystem.backendapi.mappers.UserMapper;
import com.laundrysystem.backendapi.repositories.interfaces.ILaundryAssetRepository;
import com.laundrysystem.backendapi.repositories.interfaces.IUserRepository;
import com.laundrysystem.backendapi.services.interfaces.IResidenceAdminService;
import com.laundrysystem.backendapi.services.interfaces.IUserService;

import jakarta.transaction.Transactional;

@Service
public class ResidenceAdminService implements IResidenceAdminService {
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private ILaundryAssetRepository laundryAssetRepository;
	@Autowired
	private IUserService userService;
	@Autowired
	private UserDataHelper userDataHelper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static final Logger logger = LoggerFactory.getLogger(ResidenceAdminService.class);
	
	@Transactional
	public TenantDto createTenant(TenantRegForm tenantRegForm) throws DbException, EntryNotFoundException {
		User residenceAdmin = userDataHelper.getActiveUser();
		Residence curResidence = userDataHelper.getUserResidence(residenceAdmin);
		
		logger.info(String.format("Residence admin with id=%d is attemtping to create a new tenant -[%s].\nIn residence id=%d",
				residenceAdmin.getId(), tenantRegForm.toString(), curResidence.getId()));
		
		User newTenant = UserMapper.tenantRegFormToUserMap(tenantRegForm, curResidence);
		newTenant.setPassword(passwordEncoder.encode(tenantRegForm.getPassword()));	// encoding password
		
		try {
			userRepository.save(newTenant);
			return UserMapper.userToTenantDto(newTenant);
		} catch (Exception exc) {
			logger.error(String.format("An error occurred while attempting to store the tenant initiated by residence admin with id=%d in residence id=%d. - [message=%s\\n%s]",
				residenceAdmin.getId(), curResidence.getId()), exc.getMessage(), exc.getStackTrace().toString());
			throw new DbException();
		}
	}
	
	@Transactional
	public TenantDto updateTenant(TenantRegForm tenantRegForm) throws DbException, EntryNotFoundException, UsernameNotFoundException {
		User residenceAdmin = userDataHelper.getActiveUser();
		Residence curResidence = userDataHelper.getUserResidence(residenceAdmin);
		
		logger.info(String.format("Residence admin with id=%d is attemtping to update the tenant with username=%s with values -[%s].\nIn residence id=%d.",
				residenceAdmin.getId(), tenantRegForm.getUsername(), tenantRegForm.toString(), curResidence.getId()));
		
		User tenantToUpdate = userService.fetchUserByUsername(tenantRegForm.getUsername());
		User updatedTenant = UserMapper.updateTenant(tenantToUpdate, tenantRegForm);
		try {
			updatedTenant = userRepository.update(updatedTenant);
			return UserMapper.userToTenantDto(updatedTenant);
		} catch (Exception exc) {
			logger.error(String.format("An error occurred while attempting to update the tenant with userId=%d initiated by residence admin with id=%d in residence id=%d.  - [message=%s\n%s]",
				updatedTenant.getId(), residenceAdmin.getId(), curResidence.getId(), exc.getMessage(), exc.getStackTrace().toString()));
			throw new DbException();
		}
	}
	
	public List<TenantDto> getTenants() throws DbException, EntryNotFoundException {
		User residenceAdmin = userDataHelper.getActiveUser();
		Residence curResidence = userDataHelper.getUserResidence(residenceAdmin);
		
		logger.info(String.format("Residence admin with id=%d is attemtping to fetch the residence tenants where residenceId=%d",
			residenceAdmin.getId(),curResidence.getId()));
		
		try {
			List<User> residenceTenants = userRepository.getResidenceUsersOfType(curResidence.getId(), UserRole.ROLE_TENANT);
			return residenceTenants.stream().map(tenant -> UserMapper.userToTenantDto(tenant)).toList();
		} catch(Exception exc) {
			logger.error(String.format("An error occurred while attempting to fetch the tenants initiated by residence admin with id=%d in residence id=%d.  - [message=%s %s\n]",
				residenceAdmin.getId(), curResidence.getId(), exc.getMessage(), exc.getStackTrace().toString()));
			throw new DbException();
		}
	}
	
	@Transactional
	public ResidenceAdminDto createResidenceAdmin(ResidenceAdminRegForm residenceAdminRegForm) throws DbException, EntryNotFoundException {
		User residenceAdmin = userDataHelper.getActiveUser();
		Residence curResidence = userDataHelper.getUserResidence(residenceAdmin);
		
		logger.info(String.format("Residence admin with id=%d is attemtping to create a new residence admin -[%s].\nIn residence id=%d",
				residenceAdmin.getId(), residenceAdminRegForm.toString(), curResidence.getId()));
		
		User newResidenceAdmin = UserMapper.residenceAdminFormToUserMap(residenceAdminRegForm, curResidence);
		newResidenceAdmin.setPassword(passwordEncoder.encode(residenceAdminRegForm.getPassword()));	// encoding password
		
		try {
			userRepository.save(newResidenceAdmin);
			return UserMapper.userToResidenceAdminDto(newResidenceAdmin);
		} catch (Exception exc) {
			logger.error(String.format("An error occurred while attempting to store the residence admin initiated by the residence admin with id=%d in the residence id=%d. - [message=%s\\n%s]",
				residenceAdmin.getId(), curResidence.getId(), exc.getMessage(), exc.getStackTrace().toString()));
			throw new DbException();
		}
	}
	
	@Transactional
	public ResidenceAdminDto updateResidenceAdmin(ResidenceAdminRegForm residenceAdminRegForm) throws DbException, EntryNotFoundException, UsernameNotFoundException {
		User residenceAdmin = userDataHelper.getActiveUser();
		Residence curResidence = userDataHelper.getUserResidence(residenceAdmin);
		
		logger.info(String.format("Residence admin with id=%d is attemtping to update the residence admin with username=%s with values -[%s].\nIn residence id=%d.",
				residenceAdmin.getId(), residenceAdminRegForm.getUsername(), residenceAdminRegForm.toString(), curResidence.getId()));
		
		User residenceAdminToUpdate = userService.fetchUserByUsername(residenceAdminRegForm.getUsername());
		User updatedResidenceAdmin = UserMapper.updateResidenceAdmin(residenceAdminToUpdate, residenceAdminRegForm);
		try {
			updatedResidenceAdmin = userRepository.update(updatedResidenceAdmin);
			return UserMapper.userToResidenceAdminDto(updatedResidenceAdmin);
		} catch (Exception exc) {
			logger.error(String.format("An error occurred while attempting to update the residence admin with userId=%d initiated by residence admin with id=%d in residence id=%d.  - [message=%s\n%s]",
				updatedResidenceAdmin.getId(), residenceAdmin.getId(), curResidence.getId(), exc.getMessage(), exc.getStackTrace().toString()));
			throw new DbException();
		}
	}
	
	public List<ResidenceAdminDto> getResidenceAdmins() throws DbException, EntryNotFoundException {
		User residenceAdmin = userDataHelper.getActiveUser();
		Residence curResidence = userDataHelper.getUserResidence(residenceAdmin);
		
		logger.info(String.format("Residence admin with id=%d is attemtping to fetch the residence admins where residenceId=%d",
			residenceAdmin.getId(),curResidence.getId()));
		
		try {
			List<User> residenceAdmins = userRepository.getResidenceUsersOfType(curResidence.getId(), UserRole.ROLE_RESIDENCE_ADMIN);
			return residenceAdmins.stream().map(resAdmin -> UserMapper.userToResidenceAdminDto(resAdmin)).toList();
		} catch(Exception exc) {
			logger.error(String.format("An error occurred while attempting to fetch the residence admins initiated by residence admin with id=%d in residence id=%d.  - [message=%s %s\n]",
				residenceAdmin.getId(), curResidence.getId(), exc.getMessage(), exc.getStackTrace().toString()));
			throw new DbException();
		}
	}
	
	@Transactional
	public LaundryAssetDto createResidenceLaundryAsset(LaundryAssetRegForm laundryAssetRegForm) throws DbException, EntryNotFoundException {
		User residenceAdmin = userDataHelper.getActiveUser();
		Residence curResidence = userDataHelper.getUserResidence(residenceAdmin);
		
		logger.info(String.format("Residence admin with id=%d is attemtping to create a laundry asset for residenceId=%d",
			residenceAdmin.getId(),curResidence.getId()));
		
		LaundryAsset newLaundryAsset = LaundryAssetMapper.regFormToMap(laundryAssetRegForm, curResidence);
		
		try {
			laundryAssetRepository.save(newLaundryAsset);
			return LaundryAssetMapper.toDto(newLaundryAsset);
		} catch (Exception exc) {
			logger.error(String.format("An error occurred while attempting to store the new laundry asset by residence admin with id=%d in residence id=%d.  - [message=%s %s\n",
					residenceAdmin.getId(), curResidence.getId(), exc.getMessage(), exc.getStackTrace().toString()));
			throw new DbException();
		}
	}

	@Transactional
	public LaundryAssetDto updateResidenceLaundryAsset(LaundryAssetRegForm laundryAssetRegForm) throws DbException, EntryNotFoundException, ApiBadRequestException {
		User residenceAdmin = userDataHelper.getActiveUser();
		Residence curResidence = userDataHelper.getUserResidence(residenceAdmin);
		
		logger.info(String.format("Residence admin with id=%d is attemtping to fetch the residence admins where residenceId=%d",
			residenceAdmin.getId(),curResidence.getId()));
		
		LaundryAsset laundryAssetToUpdate = laundryAssetRepository.findById(laundryAssetRegForm.getId());
		if (laundryAssetToUpdate == null) {
			throw new ApiBadRequestException();
		}
		
		try {
						
			laundryAssetToUpdate = LaundryAssetMapper.updateLaundryAsset(laundryAssetToUpdate, laundryAssetRegForm);
			LaundryAsset storedLaundryAsset = laundryAssetRepository.update(laundryAssetToUpdate);
			
			return LaundryAssetMapper.toDto(storedLaundryAsset);
		} catch (Exception exc) {
			logger.error(String.format("An error occurred while attempting to update the laundry asset with id=%d by residence admin with id=%d in residence id=%d.  - [message=%s %s\n",
				laundryAssetRegForm.getId(), residenceAdmin.getId(), curResidence.getId(), exc.getMessage(), exc.getStackTrace().toString()));
			throw new DbException();
		}
	}
	
	public List<LaundryAssetDto> getResidenceLaundryAssets() throws DbException, EntryNotFoundException {
		User residenceAdmin = userDataHelper.getActiveUser();
		Residence curResidence = userDataHelper.getUserResidence(residenceAdmin);
		
		logger.info(String.format("Residence admin with id=%d is attemtping to fetch the laundry assets for residence with residenceId=%d",
			residenceAdmin.getId(),curResidence.getId()));

		List<LaundryAsset> laundryAssets = curResidence.getLaundryAssets();
		
		return laundryAssets.stream().map(asset -> LaundryAssetMapper.toDto(asset)).toList();
	}
}
