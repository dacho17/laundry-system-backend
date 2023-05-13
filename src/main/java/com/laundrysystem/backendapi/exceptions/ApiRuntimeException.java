package com.laundrysystem.backendapi.exceptions;

public class ApiRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
	
	public ApiRuntimeException() {}
	
	public ApiRuntimeException(String message) {
		super(message);
	}
}
