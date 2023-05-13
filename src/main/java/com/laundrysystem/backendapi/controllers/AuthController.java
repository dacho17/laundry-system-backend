package com.laundrysystem.backendapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.laundrysystem.backendapi.dtos.AuthRequest;
import com.laundrysystem.backendapi.dtos.ForgotPasswordFormDto;
import com.laundrysystem.backendapi.dtos.PasswordResetFormDto;
import com.laundrysystem.backendapi.dtos.ResponseObject;
import com.laundrysystem.backendapi.dtos.UserDto;
import com.laundrysystem.backendapi.services.ValidatingService;
import com.laundrysystem.backendapi.services.interfaces.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/auth", produces = { "application/json" })
public class AuthController {

	private static final String loginSummary = "Log in to the system. If the operation is successful, the endpoint returns the JWT token used to authenticate the user in the subsequent requests.";
	private static final String signupSummary = "Create the account. If the operation is successful, the endpoint returns the username of the created user in string format.";
	
	private static final String loginSuccess = "Login successful and JWT generated.";
	private static final String signupSuccess = "User registered successfully.";
	private static final String logoutSuccess = "User logged out successfully";
	
	private static final String errorInRequest = "The information you provided could not be processed. Please check the information you provided.";
	private static final String unauthenticatedUser = "The user failed to authenticate.";
	private static final String userAlreadyExists = "The provided username already exists.";
	private static final String internalServerError = "Internal Server Error occurred.";
	
	private static final String httpCreated = "201";
	private static final String httpBadReq = "400";
	private static final String httpNotAuthenicated = "401";
	private static final String httpConflict = "409";
	private static final String httpServerError = "500";
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private IUserService userService;
	@Autowired
	private ValidatingService validatingService;
	
	@Operation(summary = loginSummary)
	@ApiResponses(value = {
		@ApiResponse(responseCode = httpCreated, description = loginSuccess, content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class)),}),
		@ApiResponse(responseCode = httpBadReq, description = errorInRequest, content = @Content),
		@ApiResponse(responseCode = httpNotAuthenicated, description = unauthenticatedUser, content = @Content),
		@ApiResponse(responseCode = httpServerError, description = internalServerError, content = @Content) })
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping("/login")
	public ResponseObject<UserDto> login(@RequestBody AuthRequest loginRequest) throws Exception {
		logger.info("POST /login endpoint accessed.");
		
		validatingService.validateAuthRequest(loginRequest, ValidatingService.LOGIN);
		UserDto loggedInUser = userService.loginUser(loginRequest);
		
		logger.info(String.format("The user (username=%s) has successfully been logged in.", loggedInUser.getUsername()));
		return new ResponseObject<UserDto>(loginSuccess, loggedInUser);
	} 
	
	// not being used atm
//	@Operation(summary = signupSummary)
//	@ApiResponses(value = {
//		@ApiResponse(responseCode = httpCreated, description = signupSuccess, content = {
//			@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseObject.class)),}),
//		@ApiResponse(responseCode = httpBadReq, description = errorInRequest, content = @Content),
//		@ApiResponse(responseCode = httpConflict, description = userAlreadyExists, content = @Content),
//		@ApiResponse(responseCode = httpServerError, description = internalServerError, content = @Content) })
//	@ResponseStatus(code = HttpStatus.CREATED)
//	@PostMapping("/signup")
//	public ResponseObject<UserDto> signup(@RequestBody AuthRequest signupRequest) throws Exception {
//		logger.info("POST /signup endpoint accessed.");
//		
//		validatingService.validateAuthRequest(signupRequest, ValidatingService.signupFlag);
//		
//		if (userService.fetchUserByUsername(signupRequest.getUsername()) != null) {
//			logger.error(String.format("User with [username=%s] already exists in the system.", signupRequest.getUsername()));
//			throw new ApiBadRequestException();
//		}
//		
//		try {
//			logger.info(String.format("User with username=%s not found in the database. Proceeding with user creation.", signupRequest.getUsername()));
//			UserDto newUser = userService.createUser(signupRequest);
//			newUser = userService.loginUser(signupRequest);
//			
//			logger.info(String.format("The user (username=%s) has successfully been signed up and logged in.", newUser.getUsername()));
//			return new ResponseObject<UserDto>(signupSuccess, newUser);
//		} catch (DbException exc) {
//			logger.error(exc.getMessage());
//			throw new DbException(exc.getMessage());
//		} catch (Exception exc) {
//			logger.error(exc.getMessage());
//			throw new ApiBadRequestException(errorInRequest);
//		}
//	}

	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/logout")
	public ResponseObject<UserDto> logout() throws Exception {
		logger.info("GET /logout endpoint accessed.");
		
		UserDto loggedOutUser = userService.logoutUser();
		
		logger.info(String.format("The user username=%s has successfully been signed out.", loggedOutUser.getUsername()));
		return new ResponseObject<UserDto>(logoutSuccess, loggedOutUser);
	}

	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping("/forgot-password")
	public ResponseObject<String> forgotPasswordRequest(@RequestBody ForgotPasswordFormDto forgotPasswordRequest) throws Exception {
		logger.info("POST /auth/forgot-password endpoint accessed");
		
		validatingService.valiadteForgotPasswordRequest(forgotPasswordRequest);
		
		String resMessage = userService.requestPasswordResetForUser(forgotPasswordRequest.getEmail());
		
		logger.info("POST /auth/forgot-password returning with the success message");
		return new ResponseObject<String>(resMessage, resMessage);
	}

	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping("/reset-password")
	public ResponseObject<String> passwordResetRequest(@RequestParam String passwordResetToken, @RequestBody PasswordResetFormDto passwordResetForm) throws Exception {
		logger.info("POST /auth/reset-password endpoint accessed");
		
		validatingService.valiadtePasswordResetRequest(passwordResetForm, passwordResetToken);
		
		String resMessage = userService.resetPasswordForUser(passwordResetForm, passwordResetToken);
		
		logger.info("POST /auth/reset-password returning with the success message");
		return new ResponseObject<String>(resMessage, resMessage);
	}
}
