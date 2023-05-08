package com.laundrysystem.backendapi.exceptions;

import javax.management.InvalidAttributeValueException;

public class ApiBadRequestException extends InvalidAttributeValueException {
	private static final long serialVersionUID = 1L;
	
	public ApiBadRequestException() {}
	
	public ApiBadRequestException(String message) {
		super(message);
	}
}
