package com.laundrysystem.backendapi.services.interfaces;

import com.laundrysystem.backendapi.dtos.AccountInformationDto;
import com.laundrysystem.backendapi.dtos.UpdateUserInfoForm;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;

public interface IAccountService {
	AccountInformationDto getAccountInformation() throws EntryNotFoundException, DbException;
	UpdateUserInfoForm updateUserInfo(UpdateUserInfoForm userInfoForm) throws EntryNotFoundException, DbException;
}
