package com.laundrysystem.backendapi.dtos;

public class AccountInformationDto {
	private UpdateUserInfoForm userInfo;
	private ResidenceDto residenceInfo;
	
	public AccountInformationDto(UpdateUserInfoForm userInfo, ResidenceDto residenceInfo) {
		super();
		this.userInfo = userInfo;
		this.residenceInfo = residenceInfo;
	}

	public UpdateUserInfoForm getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UpdateUserInfoForm userInfo) {
		this.userInfo = userInfo;
	}

	public ResidenceDto getResidenceInfo() {
		return residenceInfo;
	}

	public void setResidenceInfo(ResidenceDto residenceInfo) {
		this.residenceInfo = residenceInfo;
	}

	@Override
	public String toString() {
		return "AccountInformationDto [userInfo=" + userInfo + ", residenceInfo=" + residenceInfo + "]";
	}
}
