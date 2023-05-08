package com.laundrysystem.backendapi.services.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.laundrysystem.backendapi.dtos.LaundryAssetDto;
import com.laundrysystem.backendapi.dtos.LaundryAssetRegForm;
import com.laundrysystem.backendapi.dtos.ResidenceAdminDto;
import com.laundrysystem.backendapi.dtos.ResidenceAdminRegForm;
import com.laundrysystem.backendapi.dtos.TenantDto;
import com.laundrysystem.backendapi.dtos.TenantRegForm;
import com.laundrysystem.backendapi.exceptions.ApiBadRequestException;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;

public interface IResidenceAdminService {
	TenantDto createTenant(TenantRegForm tenantRegForm) throws DbException, EntryNotFoundException;
	TenantDto updateTenant(TenantRegForm tenantRegForm) throws DbException, EntryNotFoundException, UsernameNotFoundException;
	List<TenantDto> getTenants() throws DbException, EntryNotFoundException;
	ResidenceAdminDto createResidenceAdmin(ResidenceAdminRegForm residenceAdminRegForm) throws DbException, EntryNotFoundException;
	ResidenceAdminDto updateResidenceAdmin(ResidenceAdminRegForm residenceAdminRegForm) throws DbException, EntryNotFoundException, UsernameNotFoundException;
	List<ResidenceAdminDto> getResidenceAdmins() throws DbException, EntryNotFoundException;
	LaundryAssetDto createResidenceLaundryAsset(LaundryAssetRegForm laundryAssetRegForm) throws DbException, EntryNotFoundException;
	LaundryAssetDto updateResidenceLaundryAsset(LaundryAssetRegForm laundryAssetRegForm) throws DbException, EntryNotFoundException, ApiBadRequestException;
	List<LaundryAssetDto> getResidenceLaundryAssets() throws DbException, EntryNotFoundException;
}
