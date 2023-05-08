package com.laundrysystem.backendapi.exceptions;

import javax.management.InstanceNotFoundException;

public class EntryNotFoundException extends InstanceNotFoundException {
	private static final long serialVersionUID = 1L;
	
	public EntryNotFoundException() {}
	
	public EntryNotFoundException(String message) {
		super(message);
	}
}
