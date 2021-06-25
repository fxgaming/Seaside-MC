package by.fxg.seaside.amain;

import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ServerTickHandler implements ITickHandler {
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		
	}

	public void tickEnd(EnumSet<TickType> type, Object... tickData) {

	}
	
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

	public String getLabel() {
		return "seasideclient";
	}
}
