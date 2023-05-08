package com.laundrysystem.backendapi.exceptions;

import org.springframework.security.core.AuthenticationException;

public class ApiAuthException extends AuthenticationException {
	private static final long serialVersionUID = 1L;
	
	public ApiAuthException(String message) {
		super(message);
	}
}
