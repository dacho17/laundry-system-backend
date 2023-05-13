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
		String postedEmail = userInfoForm.getEmail();
		String postedMobileNumber = userInfoForm.getMobileNumber();
		if (postedEmail == null || postedMobileNumber == null) {
			throwBadRequestError(String.format("Some required data in the UpdateUserInfoForm has not been received. Form received=[%s]", userInfoForm.toString()));
		}
		
		boolean isEmailInCorrectFormat = validateEmail(postedEmail);
		boolean isMobileNumberInCorrectFormat = postedMobileNumber.length() == 8;	// TODO: this needs to be fine grained based on the country
		
		if (!isEmailInCorrectFormat || !isMobileNumberInCorrectFormat) {
			throwBadRequestError(String.format("The data received in the UpdateUserInfoForm is of incorrect format. Form received=[%s]", userInfoForm.toString()));
		}
	}
	
	// NOTE: trivial validation for the time being
	public void validateAuthRequest(AuthRequest authRequest, String authType) throws ApiBadRequestException {
		String username = authRequest.getUsername();
		String password = authRequest.getPassword();
		short role = authRequest.getRole();
		
		if (username == null || password == null || (authType == SINGUP && UserRole.getRole(role) == null)) {
			throwBadRequestError(String.format("Some required data in the AuthRequest has not been received. Form received=[%s]", authRequest.toString()));
		}
		
		boolean isUsernameInValidFormat = username.length() > 5;
		boolean isPasswordInValidFormat = password.length() > 5;
		
		if (!isUsernameInValidFormat || !isPasswordInValidFormat) {
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
	
	public void validateIncomingPurchaseRequest(BookingRequestDto purchaseRequest) throws ApiBadRequestException {
		boolean isAssetIdValid = purchaseRequest.getAssetId() > 0;
		boolean isTimestampToday = Formatting.isTimestampToday(purchaseRequest.getTimeslot());
		
		if (!isAssetIdValid || !isTimestampToday) {
			throwBadRequestError(String.format("The data received in the BookingRequestDto is of incorrect format. Form received=[%s]", purchaseRequest.toString()));
		}
	}
	
	public void validateTenantRegForm(TenantRegForm tenantRegForm, boolean isNewTenantForm) throws ApiBadRequestException {
		boolean isNameValid = tenantRegForm.getName().length() >= 2;
		boolean isSurnameValid = tenantRegForm.getSurname().length() >= 2;
		boolean isUsernameValid = tenantRegForm.getUsername().length() > 5;
		
		// password is not being validated for the update user form
		boolean isPasswordValid = !isNewTenantForm || tenantRegForm.getPassword().length() > 5;
		boolean isEmailInCorrectFormat = validateEmail(tenantRegForm.getEmail());
		boolean isMobileNumberInCorrectFormat = tenantRegForm.getMobileNumber().length() == 8;	// TODO: this needs to be fine grained based on the country
		
		Timestamp curTimeTs = new Timestamp(System.currentTimeMillis());
		Timestamp aMonthFromCurTimeTs = new Timestamp(curTimeTs.getTime() + 30 * 24 * 60 * 60 * 1000);
		boolean isTenancyFromValid = !isNewTenantForm || tenantRegForm.getTenancyFrom().after(curTimeTs);
		boolean isTenancyUntilValid = tenantRegForm.getTenancyTo().after(aMonthFromCurTimeTs);
		
		if (!isNameValid || !isSurnameValid || !isPasswordValid || !isUsernameValid || !isEmailInCorrectFormat  || !isMobileNumberInCorrectFormat 
				|| !isTenancyFromValid ||  !isTenancyUntilValid) {
			throwBadRequestError(String.format("The data received in the TenantRegForm is of incorrect format. Form received=[%s]", tenantRegForm.toString()));
		}
	}
	
	public void validateResidenceAdminRegForm(ResidenceAdminRegForm residenceAdminRegForm, boolean isNewResidenceAdminForm) throws ApiBadRequestException {
		boolean isNameValid = residenceAdminRegForm.getName().length() >= 2;
		boolean isSurnameValid = residenceAdminRegForm.getSurname().length() >= 2;
		boolean isUsernameValid = residenceAdminRegForm.getUsername().length() > 5;
		
		// password is not being validated for the update residence admin form
		boolean isPasswordValid = !isNewResidenceAdminForm|| residenceAdminRegForm.getPassword().length() > 5;
		boolean isEmailInCorrectFormat = validateEmail(residenceAdminRegForm.getEmail());
		boolean isMobileNumberInCorrectFormat = residenceAdminRegForm.getMobileNumber().length() == 8;	// TODO: this needs to be fine grained based on the country		
	
		if (!isNameValid || !isSurnameValid || !isPasswordValid || !isUsernameValid || !isEmailInCorrectFormat  || !isMobileNumberInCorrectFormat) {
			throwBadRequestError(String.format("The data received in the ResidenceAdminRegForm is of incorrect format. Form received=[%s]", residenceAdminRegForm.toString()));
		}
	}
	
	public void validateLaundryAssetRegForm(LaundryAssetRegForm laundryAssetRegForm, boolean isNewLaundryAssetForm) throws ApiBadRequestException{
		boolean isIdPresent = isNewLaundryAssetForm || laundryAssetRegForm.getId() > 0;
		boolean isNameValid = laundryAssetRegForm.getName().length() >= 2;
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
		
		System.out.println(isAssetIdValid);
		System.out.println(Formatting.isTimeSlotHourRelevant(timeslot));
		System.out.println(validFn.apply(timeslot));
		if (!isAssetIdValid || !isTimeslotValid) {
			throwBadRequestError(String.format("The data received in the BookingRequestDto is faulty. Form=[%s]",
					bookingRequest.toString()));
		}
	}
	
	private boolean validateEmail(String email) {
		boolean isEmailInCorrectFormat = Pattern.compile(VALID_EMAIL_PATTERN)
	      .matcher(email)
	      .matches();
		return isEmailInCorrectFormat;
	}

	private void throwBadRequestError(String msgToLog) throws ApiBadRequestException {
		logger.error(msgToLog);
		throw new ApiBadRequestException();
	}
	
	
}
