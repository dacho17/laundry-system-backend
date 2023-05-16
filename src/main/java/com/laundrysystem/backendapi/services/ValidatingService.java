package com.laundrysystem.backendapi.services;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.laundrysystem.backendapi.dtos.AuthRequest;
import com.laundrysystem.backendapi.dtos.BookingRequestDto;
import com.laundrysystem.backendapi.dtos.DailyBookingRequestDto;
import com.laundrysystem.backendapi.dtos.ForgotPasswordFormDto;
import com.laundrysystem.backendapi.dtos.LaundryAssetRegForm;
import com.laundrysystem.backendapi.dtos.PasswordResetFormDto;
import com.laundrysystem.backendapi.dtos.PurchaseLoyaltyOfferDto;
import com.laundrysystem.backendapi.dtos.PurchaseRequestDto;
import com.laundrysystem.backendapi.dtos.ResidenceAdminRegForm;
import com.laundrysystem.backendapi.dtos.TenantRegForm;
import com.laundrysystem.backendapi.dtos.UpdateUserInfoForm;
import com.laundrysystem.backendapi.enums.LaundryAssetType;
import com.laundrysystem.backendapi.enums.UserRole;
import com.laundrysystem.backendapi.exceptions.ApiBadRequestException;
import com.laundrysystem.backendapi.utils.Formatting;

@Service
public class ValidatingService {
	public static final String LOGIN = "LOGIN";
	public static final String SINGUP = "SIGNUP";
	public static final String PASSWORD_RESET = "PASSWORD_RESET";
	
	private static final Logger logger = LoggerFactory.getLogger(ValidatingService.class);
	private static final String VALID_EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";	// general case
	private static final List<String> SUPPORTED_CURRENCIES = Arrays.asList("MYR", "USD", "SGD");

	public void valiadteForgotPasswordRequest(ForgotPasswordFormDto forgotPasswordForm) throws ApiBadRequestException {
		boolean isEmailValid = validateEmail(forgotPasswordForm.getEmail());
		if (!isEmailValid) {
			throwBadRequestError(String.format("Email in the ForgotPasswordFormDto is not valid. Form received=[%s]", forgotPasswordForm.toString()));
		}
	}

	public void valiadtePasswordResetRequest(PasswordResetFormDto passwordResetForm, String passwordResetToken) throws ApiBadRequestException {
		validateAuthRequest(new AuthRequest(
			passwordResetForm.getUsername(),
			passwordResetForm.getPassword(),
			(short)0), PASSWORD_RESET);

		if (passwordResetToken == null || passwordResetToken.length() == 0) {
			logger.error(String.format("Validating passwordResetToken failed - passwordResetToken=%s", passwordResetToken));
			throw new ApiBadRequestException();
		}
	}
	
	public void validateUpdateUserInfoForm(UpdateUserInfoForm userInfoForm) throws ApiBadRequestException {
		boolean isEmailValid = validateEmail(userInfoForm.getEmail());
		boolean isDialCodeValid = validateDialCode(userInfoForm.getCountryDialCode());
		boolean isMobileNumberValid = validateMobileNumber(userInfoForm.getMobileNumber());
		
		if (!isEmailValid || !isDialCodeValid || !isMobileNumberValid) {
			throwBadRequestError(String.format("The data received in the UpdateUserInfoForm is of incorrect format. Form received=[%s]", userInfoForm.toString()));
		}
	}
	
	public void validateAuthRequest(AuthRequest authRequest, String authType) throws ApiBadRequestException {
		boolean isUsernameValid = validateUsername(authRequest.getUsername());
		boolean isPasswordValid = validatePassword(authRequest.getPassword());
		short role = authRequest.getRole();
		
		if (!isUsernameValid || !isPasswordValid || (authType == SINGUP && UserRole.getRole(role) == null)) {
			throwBadRequestError(String.format("The data received in the AuthRequest is of incorrect format. Form received=[%s]", authRequest.toString()));
		}
	}
	
	public void validateIncomingBookingRequest(BookingRequestDto bookingRequest) throws ApiBadRequestException {
		validateBookingTimeslot(bookingRequest, (req) -> Formatting.isBookingTimeslot(req));
	}
	
	public void validateIncomingDailyBookingRequest(DailyBookingRequestDto bookingRequest) throws ApiBadRequestException {
		Timestamp curTimeslot = bookingRequest.getTimeslotFrom();
		boolean isTimestampToday = Formatting.isTimestampToday(curTimeslot);
		System.out.println(String.format("The received timestamp %s is for today %b", curTimeslot, isTimestampToday));
		
		BookingRequestDto bookingReqHelper = new BookingRequestDto(bookingRequest.getAssetId(), curTimeslot);
		if (isTimestampToday) {
			validateBookingTimeslot(bookingReqHelper, (req) -> Formatting.isBookingTimeslot(req));
		} else {
			validateBookingTimeslot(bookingReqHelper, (req) -> Formatting.isDailyBookingTimeslot(req));			
		}
	}
	
	public void validateIncomingPurchaseRequest(PurchaseRequestDto purchaseRequest) throws ApiBadRequestException {
		boolean paymentFlagVal = purchaseRequest.getIsPayingWithLoyaltyPoints();
		
		boolean isAssetIdValid = purchaseRequest.getAssetId() > 0;
		boolean isPaymentFlagValid = paymentFlagVal == true || paymentFlagVal == false;
		
		if (!isAssetIdValid || !isPaymentFlagValid) {
			throwBadRequestError(String.format("The data received in the BookingRequestDto is of incorrect format. Form received=[%s]", purchaseRequest.toString()));
		}
	}

	public void validatePurchaseLoyaltyOfferDto(PurchaseLoyaltyOfferDto purchaseLoyaltyOfferDto) throws ApiBadRequestException {
		if (purchaseLoyaltyOfferDto.getLoyaltyOfferId() <= 0) {
			throwBadRequestError(String.format("The data received in the PurchaseLoyaltyOfferDto is of incorrect format. Form received=[%s]", purchaseLoyaltyOfferDto.toString()));
		}
	}
	
	public void validateTenantRegForm(TenantRegForm tenantRegForm, boolean isNewTenantForm) throws ApiBadRequestException {
		boolean isNameValid = validateName(tenantRegForm.getName());
		boolean isSurnameValid = validateName(tenantRegForm.getSurname());
		boolean isUsernameValid = validateUsername(tenantRegForm.getUsername());
		boolean isPasswordValid = !isNewTenantForm || validatePassword(tenantRegForm.getPassword());
		boolean isEmailInCorrectFormat = validateEmail(tenantRegForm.getEmail());
		boolean isDialCodeInCorrectFormat = validateDialCode(tenantRegForm.getCountryDialCode());
		boolean isMobileNumberInCorrectFormat = validateMobileNumber(tenantRegForm.getMobileNumber());

		Timestamp curTimeTs = new Timestamp(System.currentTimeMillis());
		Timestamp aMonthFromCurTimeTs = new Timestamp(curTimeTs.getTime() + 30 * 24 * 60 * 60 * 1000);
		boolean isTenancyFromValid = !isNewTenantForm || tenantRegForm.getTenancyFrom().after(curTimeTs);
		boolean isTenancyUntilValid = tenantRegForm.getTenancyTo().after(aMonthFromCurTimeTs);
		
		if (!isNameValid || !isSurnameValid || !isPasswordValid || !isUsernameValid || !isEmailInCorrectFormat
				||!isDialCodeInCorrectFormat || !isMobileNumberInCorrectFormat
				|| !isTenancyFromValid ||  !isTenancyUntilValid) {
			throwBadRequestError(String.format("The data received in the TenantRegForm is of incorrect format. Form received=[%s]", tenantRegForm.toString()));
		}
	}
	
	public void validateResidenceAdminRegForm(ResidenceAdminRegForm residenceAdminRegForm, boolean isNewResidenceAdminForm) throws ApiBadRequestException {
		boolean isNameValid = validateName(residenceAdminRegForm.getName());
		boolean isSurnameValid = validateName(residenceAdminRegForm.getSurname());
		boolean isUsernameValid = validateUsername(residenceAdminRegForm.getUsername());
		boolean isPasswordValid = !isNewResidenceAdminForm || validatePassword(residenceAdminRegForm.getPassword());
		boolean isEmailInCorrectFormat = validateEmail(residenceAdminRegForm.getEmail());
		boolean isDialCodeInCorrectFormat = validateDialCode(residenceAdminRegForm.getCountryDialCode());
		boolean isMobileNumberInCorrectFormat = validateMobileNumber(residenceAdminRegForm.getMobileNumber());
				
		if (!isNameValid || !isSurnameValid || !isPasswordValid || !isUsernameValid
			|| !isEmailInCorrectFormat || !isDialCodeInCorrectFormat || !isMobileNumberInCorrectFormat) {
			throwBadRequestError(String.format("The data received in the ResidenceAdminRegForm is of incorrect format. Form received=[%s]", residenceAdminRegForm.toString()));
		}
	}
	
	public void validateLaundryAssetRegForm(LaundryAssetRegForm laundryAssetRegForm, boolean isNewLaundryAssetForm) throws ApiBadRequestException{
		boolean isIdPresent = isNewLaundryAssetForm || laundryAssetRegForm.getId() > 0;
		boolean isNameValid = validateName(laundryAssetRegForm.getName());
		boolean isTypeValid = isNewLaundryAssetForm || LaundryAssetType.getType(laundryAssetRegForm.getAssetType()) != null;
		boolean isRunningTimeValid = laundryAssetRegForm.getRunningTime() > 0;
		boolean isPriceValid = laundryAssetRegForm.getServicePrice() > 0;
		boolean isCurrencyValid = SUPPORTED_CURRENCIES.contains(laundryAssetRegForm.getCurrency());
		boolean isIsOperationalValid = laundryAssetRegForm.getIsOperational() == false || laundryAssetRegForm.getIsOperational() == true;
		
		System.out.println(String.format("isNameValid=%b, isTypeValid=%b, isRunningTimeValid=%b, isPriceValid=%b"
				+ "isCurrencyValid=%b, isOpeationalValid=%b", isNameValid, isTypeValid, isRunningTimeValid, isPriceValid, isCurrencyValid, isIsOperationalValid));
		
		if (!isIdPresent || !isNameValid || !isTypeValid || !isRunningTimeValid || !isPriceValid || !isCurrencyValid || !isIsOperationalValid) {
			throwBadRequestError(String.format("The data received in the LaundryAssetRegForm is of incorrect format. Form received=[%s]", laundryAssetRegForm.toString()));
		}
	}
	
	private void validateBookingTimeslot(BookingRequestDto bookingRequest, Function<Timestamp, Boolean> validFn) throws ApiBadRequestException  {
		int assetId = bookingRequest.getAssetId();
		Timestamp timeslot = bookingRequest.getTimeslot();
		
		boolean isAssetIdValid = assetId > 0;
		boolean isTimeslotValid = Formatting.isTimeSlotHourRelevant(timeslot) && validFn.apply(timeslot);
		
		if (!isAssetIdValid || !isTimeslotValid) {
			throwBadRequestError(String.format("The data received in the BookingRequestDto is faulty. Form=[%s]",
					bookingRequest.toString()));
		}
	}

	private boolean validateUsername(String username) {
		return username != null && username.length() > 5;
	}

	private boolean validatePassword(String password) {
		return password != null && password.length() > 5;
	}

	private boolean validateName(String name) {
		return name != null && name.length() >= 2;
	}

	private boolean validateDialCode(String dialCode) {
		return dialCode != null && dialCode.length() == 3 || dialCode.length() == 4;	// +xy(z)
	}

	private boolean validateMobileNumber(String mobileNumber) {
		return mobileNumber != null && mobileNumber.length() >= 6;	// TODO: this needs to be fine grained based on the country
	}
	
	private boolean validateEmail(String email) {
		boolean isEmailInCorrectFormat = email != null && Pattern.compile(VALID_EMAIL_PATTERN)
	      .matcher(email)
	      .matches();
		return isEmailInCorrectFormat;
	}

	private void throwBadRequestError(String msgToLog) throws ApiBadRequestException {
		logger.error(msgToLog);
		throw new ApiBadRequestException();
	}
}
