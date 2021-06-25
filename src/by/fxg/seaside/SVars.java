package by.fxg.seaside;

import java.io.File;

import by.fxg.seaside.storage.Coords;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class SVars {
	private static final File cfgFile = new File("conf/config.dat");
	public static NBTTagCompound cfg;
	
	public static void load() {
		if (Seaside.SERVER) {
			try {
				if (cfgFile.exists()) {
					cfg = CompressedStreamTools.read(cfgFile);
				} else {
					cfgFile.createNewFile();
					setDefaults();
				}
			} catch (Exception e) {
				System.err.println("[Configuration] SVars#load errored");
				e.printStackTrace();
			}
		}
	}
	
	private static void setDefaults() {
		if (Seaside.SERVER) {
			cfg = new NBTTagCompound();
			cfg.setDouble("speakRadius", 6.0D);
			cfg.setDouble("screamRadius", 10.0D);
			NBTTagCompound var1 = new NBTTagCompound();
			var1 = new NBTTagCompound();
			var1.setInteger("houseClasses", 7);
			var1.setIntArray("houseClassPrices", new int[]{150000, 200000, 250000, 400000, 600000, 1200000, 3000000});
			var1.setInteger("houseInteriors", 7);
			var1.setIntArray("houseIntPrices", new int[]{100000, 250000, 300000, 500000, 750000, 1500000, 5000000});
			cfg.setCompoundTag("houseSettings", var1);
			
			save();
		}
	}
	
	private static boolean save() {
		if (Seaside.SERVER) {
			try {
				if (cfgFile.exists()) {
					CompressedStreamTools.write(cfg, cfgFile);
				} else {
					cfgFile.createNewFile();
					CompressedStreamTools.write(cfg, cfgFile);
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
}
