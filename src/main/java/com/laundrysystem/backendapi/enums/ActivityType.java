package com.laundrysystem.backendapi.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ActivityType {
	BOOKING((short)1), PURCHASE((short)2);
	
	private short value;
	
	private static final Map<Short, ActivityType> lookupMap
		= new HashMap<Short, ActivityType>();
	
	static {
		for(ActivityType type: EnumSet.allOf(ActivityType.class))
			lookupMap.put(type.getValue(), type);
	}
	
	private ActivityType(final short value) {
		this.value = value;
	}
	
	public short getValue() {
		return value;
	}

	public static ActivityType getType(short val) {
		return lookupMap.get(val);
	}
}
