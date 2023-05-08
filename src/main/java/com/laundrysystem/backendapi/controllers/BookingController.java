package com.laundrysystem.backendapi.controllers;

import java.util.List;
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

import com.laundrysystem.backendapi.dtos.ActiveBookingsDto;
import com.laundrysystem.backendapi.dtos.BookingDto;
import com.laundrysystem.backendapi.dtos.BookingRequestDto;
import com.laundrysystem.backendapi.dtos.LaundryAssetDto;
import com.laundrysystem.backendapi.dtos.ResponseObject;
import com.laundrysystem.backendapi.helpers.UserDataHelper;
import com.laundrysystem.backendapi.services.ValidatingService;
import com.laundrysystem.backendapi.services.interfaces.IBookingService;

@RestController
@RequestMapping(value = "/booking", produces = { "application/json" })
public class BookingController {

	@Autowired
	private IBookingService bookingService;
	@Autowired
	private ValidatingService validatingService;
	
	@Autowired
	UserDataHelper userDataHelper;
	
	private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
	private static final String SUCCESSFUL_BOOKING = "The booking has been made successfully";

	
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping()
	public ResponseObject<List<BookingDto>> bookLaundryAsset(@RequestBody BookingRequestDto bookingRequest) throws Exception {
		logger.info("POST /booking endpoint accessed");
		
		validatingService.validateIncomingBookingRequest(bookingRequest);
		List<BookingDto> necessaryBookingSlots = bookingService.bookLaundryAsset(bookingRequest);
				
		logger.info("POST /booking returning the success message");
		return new ResponseObject<List<BookingDto>>(SUCCESSFUL_BOOKING, necessaryBookingSlots);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping("/daily-bookings")
	public ResponseObject<List<BookingDto>> getLaundryAssetDailyBookings(@RequestBody BookingRequestDto dailyBookingsRequest) throws Exception {
		logger.info("GET /booking/daily-bookings endpoint accessed");
		
		validatingService.validateIncomingDailyBookingRequest(dailyBookingsRequest);
		List<BookingDto> dailyAssetBookings = bookingService.getDailyAssetBookings(dailyBookingsRequest);
				
		logger.info("GET /booking/daily-bookings returning the daily bookings");
		return new ResponseObject<List<BookingDto>>(null, dailyAssetBookings);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/my-bookings")
	public ResponseObject<List<BookingDto>> getMyFutureBookings() throws Exception {
		logger.info("GET /booking/my-bookings endpoint accessed");
		
		List<BookingDto> myBookings = bookingService.getUsersFutureBookings();
		
		logger.info("GET /booking/my-bookings returning future bookings");
		return new ResponseObject<List<BookingDto>>(null, myBookings);
	}

	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/my-active-bookings")
	public ResponseObject<ActiveBookingsDto> getMyActiveBookings() throws Exception {
		logger.info("GET /booking/my-active-bookings endpoint accessed");
		
		ActiveBookingsDto myActiveBookings = bookingService.getActiveBookings();
		
		logger.info("GET /booking/my-active-bookings returning ActiveBookingsDto");
		
		System.out.println("My active bookings are:\n\n");
		System.out.println(myActiveBookings);
		return new ResponseObject<ActiveBookingsDto>(null, myActiveBookings);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/laundry-assets")
	public ResponseObject<List<LaundryAssetDto>> getAccessibleLaundryAssets() throws Exception {
		logger.info("GET /booking/laundry-assets endpoint accessed");
		
		List<LaundryAssetDto> accessibleLaundryAssets = bookingService.getAccessibleLaundryAssets();
		
		logger.info("GET /booking/laundry-assets returning List<LaundryAssetDto>");
		return new ResponseObject<List<LaundryAssetDto>>(null, accessibleLaundryAssets);
	}
}
