package by.fxg.seaside.systems.markers;

import by.fxg.seaside.systems.pickup.EnumPickup;

public enum MarkerColor {
	RED,
	GREEN,
	BLUE,
	YELLOW,
	DARKAQUA
	
	;
	
	public static MarkerColor getByID(int id) {
		for (MarkerColor value : values()) {
			if (value.ordinal() == id) {
				return value;
			}
		}
		return null;
	}
}
