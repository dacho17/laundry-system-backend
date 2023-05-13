package com.laundrysystem.backendapi.services;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundrysystem.backendapi.dtos.AuthRequest;
import com.laundrysystem.backendapi.dtos.EmailDto;
import com.laundrysystem.backendapi.dtos.PasswordResetFormDto;
import com.laundrysystem.backendapi.dtos.UserDto;
import com.laundrysystem.backendapi.entities.User;
import com.laundrysystem.backendapi.enums.UserRole;
import com.laundrysystem.backendapi.exceptions.ApiBadRequestException;
import com.laundrysystem.backendapi.exceptions.ApiRuntimeException;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.helpers.UserDataHelper;
import com.laundrysystem.backendapi.mappers.UserMapper;
import com.laundrysystem.backendapi.repositories.interfaces.IUserRepository;
import com.laundrysystem.backendapi.services.interfaces.IEmailService;
import com.laundrysystem.backendapi.services.interfaces.IUserService;
import com.laundrysystem.backendapi.utils.JwtUtils;
import com.laundrysystem.backendapi.utils.UserDetailsHelper;

@Service
public class UserService implements IUserService  {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	private static final String PASSWORD_SUCC_UPDATED = "Password has been successfully updated";
	private static final String PASSWORD_EMAIL_SENT = "The email has been sent successfully";
	
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private UserDataHelper userDataHelper;
	@Autowired
	private IEmailService emailService;
	@Autowired
	private JwtUtils jwtUtils;

//	@Transactional
//	public UserDto createUser(AuthRequest signupRequest) throws DbException {
//		User user = new User(
//			new Timestamp(System.currentTimeMillis()),
//			signupRequest.getUsername(), 
//			passwordEncoder.encode(signupRequest.getPassword()),
//			signupRequest.getRole()
//		);
//		
//		try {
//			logger.info(String.format("Storing the user with [username=%s] to the database.", signupRequest.getUsername()));
//			userRepository.save(user);
//			logger.info(String.format("The user with [username=%s] successfully stored to the DB.", signupRequest.getUsername()));
//		} catch (Exception exc) {
//			logger.error(String.format("An error occurred while creating the user with [username=%s]",
//					signupRequest.getUsername(), exc.getStackTrace().toString()));
//			throw new DbException();
//		}
//
//		return UserMapper.toDto(user);
//	}
	
	@Transactional
	public UserDto loginUser(AuthRequest loginRequest) throws DbException {
		logger.info(String.format("Creating login response for the user with [username=%s].", loginRequest.getUsername()));
		
		Authentication authentication = userDataHelper.setAuthenticationForUser(loginRequest.getUsername(), loginRequest.getPassword());
		
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsHelper userDetails = (UserDetailsHelper) authentication.getPrincipal();
		
		try {
			logger.info(String.format("Logging in with the user [username=%s].", loginRequest.getUsername()));
			userRepository.setUserJwt(userDetails.getId(), jwt);
		} catch (Exception exc) {
			logger.error(String.format("An error occurred while logging in with the user [username=%s]",
					loginRequest.getUsername(), exc.getStackTrace().toString()));
			throw new DbException();
		}
		
		logger.info(String.format("Returning the login response carrying JWT token for the user with [username=%s].", userDetails.getUsername()));
		return new UserDto(
			userDetails.getUsername(),
			UserRole.getRole(userDetails.getRole()),
			jwt
		);
	}

	@Transactional
	public UserDto logoutUser() throws DbException {
		String activeUsername = userDataHelper.getActiveUsername();
		
		logger.info(String.format("Logging out user on demand. Removing jwt from the user with username=%s", activeUsername));
		UserDto loggedOutUser = removeJwtFromUser(null, activeUsername);
		
		logger.info(String.format("Successfully logged out with the user (username=%s).", activeUsername));		
		return loggedOutUser;
	}
	
	@Transactional
	public UserDto removeJwtFromUser(String jwt, String username) throws UsernameNotFoundException, DbException {
		User user = fetchUserByUsername(username);
		
		try {
			user = userRepository.setUserJwt(user.getId(), null);
			return UserMapper.toDto(user);
		} catch (Exception exc) {
			logger.error(String.format("Error occurred while removing jwt from the user with [userId=%d]. - [err=%s]", user.getId(), exc.getMessage()));
			throw new DbException();
		}
	}

	public User fetchUserByUsername(String username) throws UsernameNotFoundException, DbException {
		logger.info(String.format("Attempting to find the user with [username=%s].", username));
		
		User user;
		try {
			user = userRepository.findByUsername(username);			
		} catch (Exception exc) {
			logger.error(String.format("Error occurred while removing fetching the user with [username=%s]. - [err=%s]", username, exc.getMessage()));
			throw new DbException();
		}
		
		if (user == null) {
			logger.warn(String.format("User not found with [username=%s].", username));
			throw new UsernameNotFoundException("Incorrect credentials");
		}
		
		return user;
	}

	@Transactional
	public String requestPasswordResetForUser(String email) throws ApiBadRequestException, DbException, ApiRuntimeException {
		logger.info(String.format("Attempting to find the user with [email=%s].", email));
	
		User user;
		try {
			user = userRepository.findByEmail(email);
		} catch (Exception exc) {
			logger.error(String.format("Error occurred while removing fetching the user with [email=%s]. - [err=%s]", email, exc.getMessage()));
			throw new DbException();
		}
		
		if (user == null) {
			logger.warn(String.format("User not found with [email=%s].", email));
			throw new ApiBadRequestException();
		}

		try {
			String passwordResetToken = userDataHelper.generateSecureToken();
			Timestamp passwordResetValidUntil = userDataHelper.generateTimeToResetPassword();	
			user = userRepository.generateResetPasswordData(user, passwordResetToken, passwordResetValidUntil);
		} catch (Exception exc) {
			logger.error(String.format("Error occurred while generating the reset password data for the user with [id=%d]. - [err=%s]", user.getId(), exc.getMessage()));
			throw new DbException();
		}

		EmailDto emailDto = emailService.generatePasswordResetEmail(email, user.getPasswordResetToken());
		emailService.sendSimpleMail(emailDto);
		
		return PASSWORD_EMAIL_SENT;
	}

	@Transactional
	public String resetPasswordForUser(PasswordResetFormDto passwordResetForm, String passwordResetToken) throws UsernameNotFoundException, ApiBadRequestException, DbException {
		User user = fetchUserByUsername(passwordResetForm.getUsername());		
		userDataHelper.validateResetPasswordData(user, passwordResetToken);

		try {
			String encrPassword = userDataHelper.generatePassword(passwordResetForm.getPassword());
			user = userRepository.resetPasswordForUser(user, encrPassword);
		} catch (Exception exc) {
			logger.error(String.format("An error occurred while attempting to reset password for the user with userId=%d", user.getId()));
			throw new DbException();
		}
		
		return PASSWORD_SUCC_UPDATED;
	}
}
