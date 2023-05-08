package com.laundrysystem.backendapi.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.laundrysystem.backendapi.dtos.LaundryAssetDto;
import com.laundrysystem.backendapi.dtos.LaundryAssetRegForm;
import com.laundrysystem.backendapi.dtos.ResidenceAdminDto;
import com.laundrysystem.backendapi.dtos.ResidenceAdminRegForm;
import com.laundrysystem.backendapi.dtos.ResponseObject;
import com.laundrysystem.backendapi.dtos.TenantDto;
import com.laundrysystem.backendapi.dtos.TenantRegForm;
import com.laundrysystem.backendapi.services.ValidatingService;
import com.laundrysystem.backendapi.services.interfaces.IResidenceAdminService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/residence-admin", produces = { "application/json" })
public class ResidenceAdminController {
	@Autowired
	private ValidatingService validatingService;
	
	@Autowired
	private IResidenceAdminService residenceAdminService;
	
	private static final Logger logger = LoggerFactory.getLogger(ResidenceAdminController.class);
	private static final String UPDATE_TENANT_SUCCESS_MESSAGE = "Tenant successfully updated";
	private static final String CREATE_TENANT_SUCCESS_MESSAGE = "Tenant successfully created";
	private static final String UPDATE_RESIDENCE_ADMIN_SUCCESS_MESSAGE = "Residence admin successfully updated";
	private static final String CREATE_RESIDENCE_ADMIN_SUCCESS_MESSAGE = "Residence admin successfully created";
	private static final String UPDATE_LAUNDRY_ASSET_SUCCESS_MESSAGE = "Laundry asset successfully updated";
	private static final String CREATE_LAUNDRY_ASSET_SUCCESS_MESSAGE = "Laundry asset successfully created";
	
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/tenants")
	public ResponseObject<List<TenantDto>> getResidenceTenants() throws Exception {
		logger.info("GET /residence-admin/tenants enpoint accessed.");
		
		List<TenantDto> residenceTenants = residenceAdminService.getTenants();
		
		logger.info("GET /residence-admin/tenants returning residence tenants.");
		return new ResponseObject<List<TenantDto>>(null, residenceTenants);
	}
	
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping("/tenant")
	public ResponseObject<TenantDto> createResidenceTenant(@RequestBody TenantRegForm tenantRegForm) throws Exception {
		logger.info("POST /residence-admin/tenant endpoint accessed");
		
		validatingService.validateTenantRegForm(tenantRegForm, true);
		
		TenantDto createdTenant = residenceAdminService.createTenant(tenantRegForm);
		
		logger.info("POST /residence-admin/tenant returning newly created tenant.");
		return new ResponseObject<TenantDto>(CREATE_TENANT_SUCCESS_MESSAGE, createdTenant);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping("/update-tenant")
	public ResponseObject<TenantDto> updateResidenceTenant(@RequestBody TenantRegForm tenantRegForm) throws Exception {
		logger.info("POST /residence-admin/update-tenant endpoint accessed");
		
		validatingService.validateTenantRegForm(tenantRegForm, false);
		TenantDto updatedTenant = residenceAdminService.updateTenant(tenantRegForm);
		
		logger.info("POST /residence-admin/update-tenant.");
		return new ResponseObject<TenantDto>(UPDATE_TENANT_SUCCESS_MESSAGE, updatedTenant);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping()
	public ResponseObject<List<ResidenceAdminDto>> getResidenceAdmins() throws Exception {
		logger.info("GET /residence-admin enpoint accessed.");
		
		List<ResidenceAdminDto> residenceAdmins = residenceAdminService.getResidenceAdmins();
		
		logger.info("GET /residence-admin returning residence admins.");
		return new ResponseObject<List<ResidenceAdminDto>>(null, residenceAdmins);
	}
	
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping()
	public ResponseObject<ResidenceAdminDto> createResidenceAdmin(@RequestBody ResidenceAdminRegForm residenceAdminRegForm) throws Exception {
		logger.info("POST /residence-admin endpoint accessed");
		
		validatingService.validateResidenceAdminRegForm(residenceAdminRegForm, true);
		ResidenceAdminDto createdResidenceAdmin = residenceAdminService.createResidenceAdmin(residenceAdminRegForm);
		
		logger.info("POST /residence-admin returning newly created residence admin.");
		return new ResponseObject<ResidenceAdminDto>(CREATE_RESIDENCE_ADMIN_SUCCESS_MESSAGE, createdResidenceAdmin);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping("/update-residence-admin")
	public ResponseObject<ResidenceAdminDto> updateResidenceAdmin(@RequestBody ResidenceAdminRegForm residenceAdminRegForm) throws Exception {
		logger.info("POST /residence-admin/update-residence-admin endpoint accessed");
		
		validatingService.validateResidenceAdminRegForm(residenceAdminRegForm, false);
		ResidenceAdminDto updatedResidenceAdmin = residenceAdminService.updateResidenceAdmin(residenceAdminRegForm);
		
		logger.info("POST /residence-admin/update-residence-admin returning updated residence admin.");
		return new ResponseObject<ResidenceAdminDto>(UPDATE_RESIDENCE_ADMIN_SUCCESS_MESSAGE, updatedResidenceAdmin);
	}
	
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping("/create-asset")
	public ResponseObject<LaundryAssetDto> createLaundryAsset(@RequestBody LaundryAssetRegForm laundryAssetRegForm) throws Exception {
		logger.info("POST /residence-admin/create-asset endpoint accessed");
		
		validatingService.validateLaundryAssetRegForm(laundryAssetRegForm, true);
		LaundryAssetDto createdLaundryAsset = residenceAdminService.createResidenceLaundryAsset(laundryAssetRegForm);
		
		logger.info("POST /residence-admin/create-asset returning newly created laundry asset.");
		return new ResponseObject<>(CREATE_LAUNDRY_ASSET_SUCCESS_MESSAGE, createdLaundryAsset);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping("/update-asset")
	public ResponseObject<LaundryAssetDto> updateLaundryAsset(@RequestBody LaundryAssetRegForm laundryAssetRegForm) throws Exception {
		logger.info("POST /residence-admin/update-asset endpoint accessed");
		
		validatingService.validateLaundryAssetRegForm(laundryAssetRegForm, false);
		LaundryAssetDto updatedLaundryAsset = residenceAdminService.updateResidenceLaundryAsset(laundryAssetRegForm);
		
		logger.info("POST /residence-admin/update-asset returning updated laundry asset.");
		return new ResponseObject<LaundryAssetDto>(UPDATE_LAUNDRY_ASSET_SUCCESS_MESSAGE, updatedLaundryAsset);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/assets")
	public ResponseObject<List<LaundryAssetDto>> getLaundryAssets() throws Exception {
		logger.info("GET /residence-admin/assets enpoint accessed.");
		
		List<LaundryAssetDto> laundryAssets = residenceAdminService.getResidenceLaundryAssets();
		
		laundryAssets.forEach(asset -> System.out.println(String.format("String isOperational=%b", asset.getIsOperational())));
		
		logger.info("GET /residence-admin/assets returning laundry assets.");
		return new ResponseObject<List<LaundryAssetDto>>(null, laundryAssets);
	}
}
