package by.fxg.seaside.systems.punishment;

import net.minecraft.entity.player.EntityPlayer;

public class Punishments {
	public static Punishments instance;
	
	public Punishments() {
		instance = this;
	}
	
	public boolean hasMute(EntityPlayer var1) {
		return false;
	}
	
	public boolean hasBan(EntityPlayer var1) {
		return false;
	}
}
