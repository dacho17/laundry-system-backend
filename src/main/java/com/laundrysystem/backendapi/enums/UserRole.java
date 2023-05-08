package com.laundrysystem.backendapi.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum UserRole {
	// NOTE: using ROLE_ prefix here since spring-security requires it
	ROLE_TENANT ((short)1), ROLE_RESIDENCE_ADMIN((short)2), ROLE_SYSTEM_ADMIN((short)3);

	private short value;

	private static final Map<Short, UserRole> lookupMap
		= new HashMap<Short, UserRole>();

	static {
		for(UserRole type: EnumSet.allOf(UserRole.class))
			lookupMap.put(type.getValue(), type);
	}
	
	private UserRole(final short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static UserRole getRole(short val) {
		return lookupMap.get(val);
	}
}
