package com.laundrysystem.backendapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.laundrysystem.backendapi.dtos.BookingRequestDto;
import com.laundrysystem.backendapi.dtos.PurchaseDto;
import com.laundrysystem.backendapi.dtos.PurchaseRequestDto;
import com.laundrysystem.backendapi.dtos.ResponseObject;
import com.laundrysystem.backendapi.dtos.TimeslotAvailabilityDto;
import com.laundrysystem.backendapi.services.ValidatingService;
import com.laundrysystem.backendapi.services.interfaces.IBookingService;

import java.util.List;

@RestController
@RequestMapping(value = "/availability", produces = { "application/json" })
public class AvailabilityController {
	@Autowired
	private IBookingService bookingService;
	@Autowired
	private ValidatingService validatingService;
	
	private static final Logger logger = LoggerFactory.getLogger(AvailabilityController.class);
	private static final String SUCCESSFUL_PURCHASE = "The purchase has been made successfully";
	
//	@CrossOrigin(origins = "http://localhost:3000")
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping()
	public ResponseObject<List<TimeslotAvailabilityDto>> getLaundryAssetsEarliestAvailability() throws Exception {
		logger.info("GET /availability endpoint accessed");
		
		List<TimeslotAvailabilityDto> earliestAvailabilities = bookingService.getLaundryAssetsEarliestAvailability();
		
		logger.info("GET /availability/ returning earliest availabilities of laundry assets");
		System.out.println(String.format("Object to be sent is: %s", earliestAvailabilities));
		return new ResponseObject<List<TimeslotAvailabilityDto>>(null, earliestAvailabilities);
	}
	
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping("/purchase")
	public ResponseObject<PurchaseDto> purchaseLaundryService(@RequestBody PurchaseRequestDto purchaseRequestDto) throws Exception {
		logger.info("POST /availability/purchase endpoint accessed");
		
		validatingService.validateIncomingPurchaseRequest(purchaseRequestDto);
		
		PurchaseDto newPurchase = bookingService.purchaseLaundryService(purchaseRequestDto.getAssetId(), purchaseRequestDto.getIsPayingWithLoyaltyPoints());
		
		logger.info("POST /availability/purchase returning with the success message");
		return new ResponseObject<PurchaseDto>(SUCCESSFUL_PURCHASE, newPurchase);
	}
}
