package com.laundrysystem.backendapi.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ActivityHistoryEntryType {
    ASSET_BOOKING((short)1), ASSET_PURCHASE((short)2), OFFER_PURCHASE((short)3);
	
	private short value;
	
	private static final Map<Short, ActivityHistoryEntryType> lookupMap
		= new HashMap<Short, ActivityHistoryEntryType>();
	
	static {
		for(ActivityHistoryEntryType type: EnumSet.allOf(ActivityHistoryEntryType.class))
			lookupMap.put(type.getValue(), type);
	}
	
	private ActivityHistoryEntryType(final short value) {
		this.value = value;
	}
	
	public short getValue() {
		return value;
	}

	public static ActivityHistoryEntryType getType(short val) {
		return lookupMap.get(val);
	}
}
