package com.laundrysystem.backendapi.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum TimeslotAvailabilityStatus {
    RUNNING_BY_USER ((short)1),
    FREE_TO_USE((short)2),
    FREE_TO_USE_BOOKED((short)3),
    BOOKED_BY_USER((short)4),
    AVAILABLE_FROM((short)5);

	private short value;

	private static final Map<Short, TimeslotAvailabilityStatus> lookupMap
		= new HashMap<Short, TimeslotAvailabilityStatus>();

	static {
		for(TimeslotAvailabilityStatus type: EnumSet.allOf(TimeslotAvailabilityStatus.class))
			lookupMap.put(type.getValue(), type);
	}
	
	private TimeslotAvailabilityStatus(final short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	public static TimeslotAvailabilityStatus getRole(short val) {
		return lookupMap.get(val);
	}
}
