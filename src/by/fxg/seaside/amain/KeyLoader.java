package by.fxg.seaside.amain;

import java.util.ArrayList;
import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class KeyLoader extends KeyHandler {
	//public static Key keyBottle = new Key("Пузырек с энергией", Keyboard.KEY_Z);
	public static Key keyInteract = new Key("Взаимодействие", Keyboard.KEY_LMENU);
	public static Key keyInteract2 = new Key("Доп. действие 2", Keyboard.KEY_N);
	
	public KeyLoader() {
		super(new KeyBinding[]{keyInteract, keyInteract2});
	}
	
	public String getLabel() {
		return "keyLoader";
	}

	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if (kb.pressed) {
			kb.pressTime++;
		} else {
			kb.pressed = true;
		}
		if (isRepeat && kb.pressTime > 0) {
			kb.pressTime = 0;
		}
	}

	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		if (kb.pressed) {
			kb.pressed = false;
			kb.pressTime = 0;
		} else if (kb.pressTime > 0) {
			kb.pressTime = 0;
		}
	}

	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}
	
	public static class Key extends KeyBinding {
		public Key(String par1Str, int par2) {
			super(par1Str, par2);
		}
		
		public String getKeyButton() {
			try {
				return Keyboard.getKeyName(this.keyCode);
			} catch (Exception e) {
				return "@";
			}
		}
	};
}
