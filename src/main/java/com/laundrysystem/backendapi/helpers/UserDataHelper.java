package com.laundrysystem.backendapi.helpers;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.laundrysystem.backendapi.entities.LaundryAsset;
import com.laundrysystem.backendapi.entities.PaymentCard;
import com.laundrysystem.backendapi.entities.Residence;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.entities.UserResidence;
import com.laundrysystem.backendapi.exceptions.ApiBadRequestException;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;
import com.laundrysystem.backendapi.repositories.interfaces.IPaymentCardRepository;
import com.laundrysystem.backendapi.repositories.interfaces.IUserRepository;
import com.laundrysystem.backendapi.utils.Formatting;

@Service
public class UserDataHelper {
	private static final Logger logger = LoggerFactory.getLogger(UserDataHelper.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IPaymentCardRepository paymentCardRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final int MINUTES_TO_RESET_PASSWORD = 30;

	public Residence getUserResidence(User user) throws EntryNotFoundException, DbException {
		logger.info(String.format("Finding the current residence for user with userId=%d.", user.getId()));
		long curTs = Formatting.getCurTimestamp().getTime();
		
		Optional<UserResidence> activeTenancyCandidate;
		try {
			activeTenancyCandidate = user.getUserResidences().stream()
					.filter((residence) -> (residence.getTenancyStart().getTime() <= curTs && curTs <= residence.getTenancyEnd().getTime())
					|| (residence.getTenancyStart().getTime() >= curTs)).findFirst();			
		} catch (Exception exc) {
			logger.warn(String.format("For now this is an indication of residence admin employee"));
			return user.getUserResidences().get(0).getResidence();
		}


		if (activeTenancyCandidate.isEmpty()) {
			logger.warn(String.format("User with userId=%d has no active tenancies, laundry assets cannot be fetched.", user.getId()));
			return null;
		}
		
		Residence curResidence = activeTenancyCandidate.get().getResidence();
		logger.info(String.format("Current residence (residenceId=%d) found for the user with userId=%d", curResidence.getId(), user.getId()));
		
		return curResidence;
	}

	public List<LaundryAsset> getAccessibleLaundryAssets(User user) throws EntryNotFoundException, DbException {
		Residence curResidence = getUserResidence(user);
		List<LaundryAsset> laundryAssets = curResidence.getLaundryAssets();
		logger.info(String.format("%d laundry assets have been fetched for the residency with id=%d",
			laundryAssets.size(), curResidence.getId()));
		
		// NOTE: in case only the operational assets are to be returned!
		List<LaundryAsset> operationalAssets = laundryAssets.stream().filter(asset -> asset.getIsOperational()).toList();
		logger.info(String.format("Out of %d fetched laundry assets, %d laundry assets are operational",
			laundryAssets.size(), operationalAssets.size()));
		
		return operationalAssets;
	}
	
	public PaymentCard getActivePaymentCardFor(User user) throws EntryNotFoundException{
		PaymentCard activePaymentCard = paymentCardRepository.getActivePaymentCardForUser(user.getId());
		
		if (activePaymentCard == null) {
			throw new EntryNotFoundException("Please register a payment method to proceed.");
		}
		
		return activePaymentCard;
	}
	
	
	public User getActiveUser() throws DbException, EntryNotFoundException {
		logger.info("About to fetch data for the currently active user.");

		User user;
		String activeUsername;
		try {
			activeUsername = getActiveUsername();
			logger.info(String.format("Fetching the user with username=%s", activeUsername));
			user = userRepository.findByUsername(activeUsername);
		} catch (Exception exc) {
			logger.error(String.format("An error occurred while fetching the currently active user. - [%s]", exc.getStackTrace().toString()));
			throw new DbException();
		}

		if (user == null) {
			logger.error(String.format("The active user with username=%s has not been found.", activeUsername));
			throw new EntryNotFoundException();
		}
		
		return user;
	}
	
	public String getActiveUsername() {
		logger.info(String.format("Getting the active user."));

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails)principal;
			System.out.println(userDetails.toString());
			return userDetails.getUsername();
		} else {
			System.out.println(String.format("Principal not instance of userDetails converted to string=%s", principal.toString()));
			return principal.toString();
		}
	}

	public Authentication setAuthenticationForUser(String username, String password) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, password));

		System.out.println(String.format("Authentication=[%s]\n\n", authentication.toString()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return authentication;
	}

	public String generatePassword(String password) {
		return passwordEncoder.encode(password);
	}

	public String generateSecureToken() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 16;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
		.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
		.limit(targetStringLength)
		.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		.toString();
		
		return generatedString;
	}

	public Timestamp generateTimeToResetPassword() {
		long curTs = Formatting.getCurTimestamp().getTime();
		long resetPasswordUntilTs = curTs + Formatting.convertMinuteToMs(MINUTES_TO_RESET_PASSWORD);

		return new Timestamp(resetPasswordUntilTs);
	}

	public void validateResetPasswordData(User user, String passwordResetToken) throws ApiBadRequestException {
		logger.info(String.format("Checking resetPasswordData validity for the user with id=%d, with passwordResetToken=%s", user.getId(), passwordResetToken));
	
		if (!passwordResetToken.equals(user.getPasswordResetToken())) {
			logger.error(String.format("User's passwordResetToken=%s did not match the received passwordResetToken=%s", user.getPasswordResetToken(), passwordResetToken));
			throw new ApiBadRequestException();
		}

		long curTs = Formatting.getCurTimestamp().getTime();
		long passwordTokenExpirationTs = user.getPasswordResetValidUntil().getTime();
		if (curTs > passwordTokenExpirationTs) {
			logger.error(String.format("User's passwordResetToken=%s has expired on=%s", passwordResetToken, user.getPasswordResetValidUntil()));
			throw new ApiBadRequestException();
		}
	}
}
