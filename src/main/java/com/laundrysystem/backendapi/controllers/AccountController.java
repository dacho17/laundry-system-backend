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

import com.laundrysystem.backendapi.dtos.AccountInformationDto;
import com.laundrysystem.backendapi.dtos.ActivityDto;
import com.laundrysystem.backendapi.dtos.PaymentCardsDto;
import com.laundrysystem.backendapi.dtos.ResponseObject;
import com.laundrysystem.backendapi.dtos.UpdatePaymentCardForm;
import com.laundrysystem.backendapi.dtos.UpdateUserInfoForm;
import com.laundrysystem.backendapi.services.ValidatingService;
import com.laundrysystem.backendapi.services.interfaces.IAccountService;
import com.laundrysystem.backendapi.services.interfaces.IBookingService;
import com.laundrysystem.backendapi.services.interfaces.IPaymentCardService;

import java.util.List;

@RestController
@RequestMapping(value = "/account", produces = { "application/json" })
public class AccountController {
	@Autowired
	private IPaymentCardService paymentCardService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IBookingService bookingService;
	@Autowired
	private ValidatingService validatingService;

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	private static final String NO_REGISTERED_CARDS = "No cards have been registered";
	private static final String NEW_CARD_SUCCESSFULLY_REGISTERED = "New card successfully registered";
	private static final String ACCOUNT_INFO_SUCCESSFULLY_UPDATED = "Account information successfully updated";

	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/activity-history")
	public ResponseObject<List<ActivityDto>> getActivityHistory() throws Exception {
		logger.info("GET /account/activity-history enpoint accessed.");

		List<ActivityDto> activities = bookingService.getUserActivities();

		logger.info("GET /account/activity-history returning result.");
		return new ResponseObject<List<ActivityDto>>(null, activities);
	}

	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/payment-cards")
	public ResponseObject<PaymentCardsDto> getPaymentCards() throws Exception {
		logger.info("GET /account/payment-cards endpoint accessed.");

		PaymentCardsDto paymentCards = paymentCardService.getUserPaymentCards();

		String responseMessage = null;
		if (paymentCards == null) {
			responseMessage = NO_REGISTERED_CARDS;
		}

		logger.info("GET /account/payment-cards returning result.");
		return new ResponseObject<PaymentCardsDto>(responseMessage, paymentCards);
	}

	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping("/payment-card")
	public ResponseObject<PaymentCardsDto> updatePaymentCard(@RequestBody UpdatePaymentCardForm updatePaymentCardForm)
			throws Exception {
		logger.info("POST /account/payment-card endpoint accessed");

		// TODO: form validation. This form will never arrive to the application in the
		// current format.
		PaymentCardsDto updatedPaymentCards = paymentCardService.updateUserPaymentCard(updatePaymentCardForm);

		logger.info("POST /account/payment-card returning payment cards with the newly created payment card.");
		return new ResponseObject<PaymentCardsDto>(NEW_CARD_SUCCESSFULLY_REGISTERED, updatedPaymentCards);
	}

	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/account-information")
	public ResponseObject<AccountInformationDto> getAccountInformation() throws Exception {
		logger.info("GET /account/account-information endpoint accessed");

		AccountInformationDto accountInfo = accountService.getAccountInformation();

		logger.info("GET /account/account-information returning account information.");
		return new ResponseObject<AccountInformationDto>(null, accountInfo);
	}

	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping("/user-information")
	public ResponseObject<UpdateUserInfoForm> updateUserInformation(@RequestBody UpdateUserInfoForm userInfoForm)
			throws Exception {
		logger.info("POST /account/user-information endpoint accessed");

		validatingService.validateUpdateUserInfoForm(userInfoForm);

		UpdateUserInfoForm updatedForm = accountService.updateUserInfo(userInfoForm);

		logger.info("POST /account/user-information returning updated user form");
		return new ResponseObject<UpdateUserInfoForm>(ACCOUNT_INFO_SUCCESSFULLY_UPDATED, updatedForm);
	}
}
