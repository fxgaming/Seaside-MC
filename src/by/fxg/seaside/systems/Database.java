package by.fxg.seaside.systems;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.storage.Coords;
import by.fxg.seaside.systems.businesses.Business;
import by.fxg.seaside.systems.houses.House;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;

public class Database {//СДЕЛАТЬ ДБ ИГРОКОВ ЧЕРЕЗ ОТДЕЛЬНЫЕ ФАЙЛЫ
	private static final File sdbFile = new File("conf/sdb.dat");
	public static NBTTagCompound sdb;
	
	public static Map<Integer, Coords> interiors;
	public static Map<Integer, House> houses;
	public static Map<Integer, Business> businesses;
	
	public static void init() {
		interiors = new HashMap<Integer, Coords>();
		houses = new HashMap<Integer, House>();
		businesses = new HashMap<Integer, Business>();
		load();
	}

	public static void load() {
		if (Seaside.SERVER) {
			try {
				if (sdbFile.exists()) {
					sdb = CompressedStreamTools.read(sdbFile);
				} else {
					sdbFile.createNewFile();
					setDefaults();
				}
				read(0);
			} catch (Exception e) {
				System.err.println("[Configuration] Database#load errored");
				e.printStackTrace();
			}
		}
	}
	
	public static void read(int type) {
		if (Seaside.SERVER) {
			if (type == 0 || type == 1) {
				NBTTagList var1 = sdb.getTagList("interiors");
				interiors.clear();
				for (int i = 0; i != var1.tagCount(); i++) {
					if (var1.tagAt(i) != null && var1.tagAt(i) instanceof NBTTagIntArray) {
						int[] arr = ((NBTTagIntArray)var1.tagAt(i)).intArray;
						interiors.put(arr[0], new Coords(arr[1], arr[2], arr[3]));
					}
				}
			}
			if (type == 0 || type == 2) {
				NBTTagList var1 = sdb.getTagList("houses");
				houses.clear();
				for (int i = 0; i != var1.tagCount(); i++) {
					if (var1.tagAt(i) != null && var1.tagAt(i) instanceof NBTTagCompound) {
						House h = House.get((NBTTagCompound)var1.tagAt(i));
						houses.put(h.ID, h);
					}
				}
			}
		}
	}
	
	public static void mergeData(int type) {
		if (Seaside.SERVER) {
			if (type == 0 || type == 1) {
				NBTTagList var1 = new NBTTagList();
				for (int key : interiors.keySet()) {
					var1.appendTag(new NBTTagIntArray("interior", new int[]{key, interiors.get(key).x, interiors.get(key).y, interiors.get(key).z}));
				}
				sdb.setTag("interiors", var1);
			}
			if (type == 0 || type == 2) {
				NBTTagList var1 = new NBTTagList();
				for (House h : houses.values()) {
					var1.appendTag(h.save());
				}
				sdb.setTag("houses", var1);
			}
		}
	}
	
	public static boolean save() {
		try {
			if (Seaside.SERVER) {
				if (sdbFile.exists()) {
					CompressedStreamTools.write(sdb, sdbFile);
				} else {
					sdbFile.createNewFile();
					CompressedStreamTools.write(sdb, sdbFile);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static void setDefaults() {
		if (Seaside.SERVER) {
			sdb = new NBTTagCompound();
			sdb.setTag("houses", new NBTTagList());
			
			save();
		}
	}
}
