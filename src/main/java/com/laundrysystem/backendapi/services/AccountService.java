package com.laundrysystem.backendapi.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laundrysystem.backendapi.dtos.AccountInformationDto;
import com.laundrysystem.backendapi.dtos.UpdateUserInfoForm;
import com.laundrysystem.backendapi.entities.Residence;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;
import com.laundrysystem.backendapi.helpers.UserDataHelper;
import com.laundrysystem.backendapi.mappers.ResidenceMapper;
import com.laundrysystem.backendapi.mappers.UserMapper;
import com.laundrysystem.backendapi.repositories.interfaces.IUserRepository;
import com.laundrysystem.backendapi.services.interfaces.IAccountService;

import jakarta.transaction.Transactional;

@Service
public class AccountService implements IAccountService {
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private UserDataHelper userDataHelper;
	
	private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
	
	public AccountInformationDto getAccountInformation() throws EntryNotFoundException, DbException {
		User user = userDataHelper.getActiveUser();
		
		logger.info(String.format("Assembling the AccountInformationDto for the user with userId=%d", user.getId()));
		Residence curResidence = userDataHelper.getUserResidence(user);
		
		UpdateUserInfoForm userData = UserMapper.toUserInfoForm(user);
		
		AccountInformationDto accountInfo = new AccountInformationDto(userData, ResidenceMapper.toDto(curResidence));
		return accountInfo;	
	}

	@Transactional
	public UpdateUserInfoForm updateUserInfo(UpdateUserInfoForm userInfoForm) throws EntryNotFoundException, DbException {
		User user = userDataHelper.getActiveUser();
		logger.info(String.format("About to update user information (userId=%d) with [%s]", user.getId(), userInfoForm.toString()));
		
		try {
			User updatedUser = userRepository.setUserEmailAndMobileNumber(user,
				userInfoForm.getEmail(), userInfoForm.getCountryDialCode(), userInfoForm.getMobileNumber());
			return UserMapper.toUserInfoForm(updatedUser);
		} catch (Exception exc) {
			logger.error("An error occurred while updatingUserInfo for userId=%d. Err - [%s]", user.getId(), exc.getStackTrace().toString());
			throw new DbException();
		}
	}
}
