package by.fxg.seaside.utils;

import java.util.Calendar;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Utils {
	private static Random rand = new Random();
	private static Random rand() {
		long l = rand.nextLong();
		rand.setSeed(l);
		return rand;
	}
	
	public static NBTTagCompound getTag(ItemStack par1) {
		if (par1.hasTagCompound()) return par1.getTagCompound();
		else {
			NBTTagCompound tagCompound = new NBTTagCompound();
			par1.setTagCompound(tagCompound);
			return par1.getTagCompound();
		}
	}
	
	public static String getTimeStamp() {
		Calendar c = Calendar.getInstance();
		return "[" + c.YEAR + "." + c.MONTH + "." + c.DAY_OF_MONTH + "][" + c.HOUR_OF_DAY + ":" + c.MINUTE + ":" + c.SECOND + "][" + c.MILLISECOND + "]";
	}
	
	public static String sculptString(String[] arr) {
		String str = "";
		if (arr != null && arr.length > 0) {
			for (String str$ : arr) {
				str += str$ + " ";
			}
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
	
	public static int genKey() {
		return rand().nextInt(Integer.MAX_VALUE);
	}
	
	public static boolean getRandom(int chance) {
		rand = new Random();
		int min = 0;
		int max = 100;
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum - (max - chance) > 0;
	}
	
	public static boolean getRandom(int chance, int min, int max) {
		rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum - (max - chance) > 0;
	}

	public static boolean getRandom(double chance) {
		rand = new Random();
		double min = 0;
		double max = 100;
		double value = min + (max - min) * rand.nextDouble();
		return value - (max - chance) > 0;
	}

	public static boolean getRandom(double chance, double min, double max) {
		rand = new Random();
		double value = min + (max - min) * rand.nextDouble();
		return value - (max - chance) > 0;
	}
	
	public static int getRandom(int min, int max) {
		rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
	
	public static boolean getDeusRandom(double procent) {
		double[] randomic = new double[] { procent, 100.0D - procent };
		double r = Math.random() * 100.0D;
		int num = randomic.length;
		for (int i = 0; i < randomic.length; ++i) {
			if (r <= randomic[i]) {
				num = i;
				break;
			}
			r -= randomic[i];
		}
		return num <= 0;
	}
	
	public static int getPercentOf(int par1Percent, int par2Low, int par3High) {
		return (int)((double)par2Low / ((double)par3High / par1Percent));
	}
	
	public static double getPercentOf(double par1Percent, double par2Low, double par3High) {
		return ((double)par2Low / ((double)par3High / par1Percent));
	}
	
	public static double getPercent(double value, double percent) {
		return value / 100.0D * percent;
	}
	
	public static int getPercent(int value, int percent) {
		return value / 100 * percent;
	}
}
