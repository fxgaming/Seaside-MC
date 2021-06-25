package by.fxg.sampui.client;

import net.minecraft.client.gui.FontRenderer;

public class SAMPUtils {
	public static int setIfMaximal(int maximal, String text, int ext, FontRenderer fr) {
		return text == null || fr == null || maximal > fr.getStringWidth(text) ? maximal : fr.getStringWidth(text) + ext;
	}
	
	public static boolean isInArea(int x, int y, int bx, int by, int bx1, int by1) {
		return bx <= x && bx1 >= x && by <= y && by1 >= y;
	}
	
	public static boolean isInAreaSized(int x, int y, int bx, int by, int bw, int bh) {
		return bx <= x && bx + bw >= x && by <= y && by + bh >= y;
	}
}
