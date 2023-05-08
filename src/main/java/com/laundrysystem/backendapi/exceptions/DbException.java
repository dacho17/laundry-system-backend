package com.laundrysystem.backendapi.exceptions;

public class DbException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public DbException() {}
	
	public DbException(String message) {
		super(message);
	}
}
