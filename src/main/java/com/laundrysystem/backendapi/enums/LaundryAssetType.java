package com.laundrysystem.backendapi.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum LaundryAssetType {
	WASHER((short)1), DRYER((short)2);
	
	private short value;
	
	private static final Map<Short, LaundryAssetType > lookupMap
		= new HashMap<Short, LaundryAssetType >();
	
	static {
		for(LaundryAssetType  type: EnumSet.allOf(LaundryAssetType .class))
			lookupMap.put(type.getValue(), type);
	}
	
	private LaundryAssetType (final short value) {
		this.value = value;
	}
	
	public short getValue() {
		return value;
	}

	public static LaundryAssetType  getType(short val) {
		return lookupMap.get(val);
	}
}
