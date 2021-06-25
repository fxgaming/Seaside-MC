package by.fxg.seaside.amain;

import java.util.EnumSet;

import by.fxg.seaside.network.SPacketInteract;
import by.fxg.seaside.network.minimap.ReiMinimap;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.Minecraft;

public class ClientTickHandler implements ITickHandler {
	public static ClientTickHandler instance;
	public ClientTickHandler() {
		instance = this;
	}
	
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.PLAYER)) {

		} else if (type.contains(TickType.CLIENT)) {
			if (KeyLoader.keyInteract.isPressed()) {
				PacketDispatcher.sendPacketToServer(new SPacketInteract(true).get());
			}
			if (KeyLoader.keyInteract2.isPressed()) {
				PacketDispatcher.sendPacketToServer(new SPacketInteract(false).get());
			}
		} else if (type.contains(TickType.RENDER)) {
			
		}
	}
	
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.RENDER, TickType.PLAYER);
	}

	public String getLabel() {
		return "seasideclient";
	}
}
