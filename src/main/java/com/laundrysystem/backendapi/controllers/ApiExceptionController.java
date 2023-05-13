package com.laundrysystem.backendapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.laundrysystem.backendapi.dtos.ResponseObject;
import com.laundrysystem.backendapi.exceptions.ApiBadRequestException;
import com.laundrysystem.backendapi.exceptions.DbException;
import com.laundrysystem.backendapi.exceptions.EntryNotFoundException;
import com.laundrysystem.backendapi.exceptions.ForbiddenActionException;

@RestControllerAdvice
public class ApiExceptionController {

	private static final String errorInRequest = "Please check that the information you provided has been entered correctly.";
	private static final String databaseConflictError = "This action can not be performed.";
	private static final String errorReadingReq ="Exception occurred while reading the request data.";
	private static final String dataNotFound = "Data received from request was not found.";
	private static final String errorReadingDB ="Exception occurred while trying to retrieve data from the database.";
	private static final String internalServerError = "Internal Server Error occurred.";
	
	private static final Logger logger = LoggerFactory.getLogger(ApiExceptionController.class);
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(ForbiddenActionException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.CONFLICT));
		return new ResponseEntity<>(new ResponseObject<>(databaseConflictError, exc.getMessage()), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(ApiBadRequestException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.BAD_REQUEST));
		return new ResponseEntity<>(new ResponseObject<>(errorInRequest, exc.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(HttpMessageNotReadableException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.BAD_REQUEST));
		return new ResponseEntity<>(new ResponseObject<>(errorInRequest, errorReadingReq), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(EntryNotFoundException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.NOT_FOUND));
		return new ResponseEntity<>(new ResponseObject<>(exc.getMessage() != null ? exc.getMessage() : dataNotFound, exc.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(DbException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.INTERNAL_SERVER_ERROR));
		return new ResponseEntity<>(new ResponseObject<>(internalServerError, exc.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseObject<?>> handleException(InvalidDataAccessResourceUsageException exc) {
		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.INTERNAL_SERVER_ERROR));
		return new ResponseEntity<>(new ResponseObject<>(internalServerError, errorReadingDB), HttpStatus.INTERNAL_SERVER_ERROR);
	}
//	
//	@ExceptionHandler
//	public ResponseEntity<ResponseObject<?>> handleException(ServerApiException exc) {
//		logger.warn(String.format("Exception of %s occured, API is returning %s response.", exc.getClass().toString(), HttpStatus.INTERNAL_SERVER_ERROR));
//		return new ResponseEntity<>(new ResponseObject<>(internalServerError, exc.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//	}
}
