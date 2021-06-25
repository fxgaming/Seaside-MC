package by.fxg.seaside.utils;

import by.fxg.seaside.systems.businesses.EnumBusiness;

public enum ALevel {
	NONE,
	LOW_HELPER,
	HELPER,
	LEAD_HELPER,
	
	LOW_MODERATOR,
	MODERATOR,
	LEAD_MODERATOR,
	
	LOW_ADMINISTRATOR,
	ADMINISTRATOR_1,
	ADMINISTRATOR_2,
	ADMINISTRATOR_3,
	ADMINISTRATOR_4,
	ADMINISTRATOR_5,
	LEAD_ADMINISTRATOR,
	EXEC_ADMINISTRATOR,
	SERVER;
	
	public static ALevel getByID(int id) {
		for (ALevel value : values()) {
			if (value.ordinal() == id) {
				return value;
			}
		}
		return null;
	}
}
