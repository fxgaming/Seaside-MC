package by.fxg.seaside.systems.houses;

import java.util.ArrayList;
import java.util.List;

import by.fxg.seaside.SVars;
import by.fxg.seaside.Seaside;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class House {
	public int ID;
	public int classID;
	public int interiorID;
	public String owner;
	public boolean isOpen;
	public boolean isOnRent;
	public int rentPrice;
	public int govPrice;
	public List<String> living;
	public long dropTime;
	
	public House(int ID, int classID, int interiorID) {
		this.ID = ID;
		this.classID = classID;
		this.interiorID = interiorID;
		this.owner = "none";
		this.isOpen = true;
		this.govPrice = SVars.cfg.getCompoundTag("houseSettings").getIntArray("houseClassPrices")[classID];
		this.isOnRent = false;
		this.rentPrice = 500;
		this.living = new ArrayList<String>();
		this.dropTime = 0L;
	}
	
	public House(int ID, int classID, int interiorID, String owner, boolean isOpen, boolean isOnRent, int rentPrice, List<String> living, long dropTime) {
		this.ID = ID;
		this.classID = classID;
		this.interiorID = interiorID;
		this.owner = owner;
		this.isOpen = isOpen;
		this.isOnRent = isOnRent;
		this.rentPrice = rentPrice;
		this.living = living;
		this.dropTime = dropTime;
	}
	
	public NBTTagCompound save() {
		if (Seaside.SERVER) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("id", this.ID);
			tag.setInteger("class", this.classID);
			tag.setInteger("interior", this.interiorID);
			tag.setString("owner", this.owner);
			tag.setBoolean("isOpen", this.isOpen);
			tag.setBoolean("isOnRent", this.isOnRent);
			tag.setInteger("rentPrice", this.rentPrice);
			tag.setLong("dropTime", this.dropTime);
			NBTTagList list = new NBTTagList("living");
			for (String str : this.living) {
				list.appendTag(new NBTTagString(str, str));
			}
			tag.setTag("living", list);
			return tag;
		}
		return null;
	}
	
	public static House get(NBTTagCompound tag) {
		if (Seaside.SERVER) {
			House house = new House(tag.getInteger("id"), tag.getInteger("class"), tag.getInteger("interior"));
			house.owner = tag.getString("owner");
			house.isOpen = tag.getBoolean("isOpen");
			house.isOnRent = tag.getBoolean("isOnRent");
			house.rentPrice = tag.getInteger("rentPrice");
			house.govPrice = SVars.cfg.getCompoundTag("houseSettings").getIntArray("houseClassPrices")[house.classID];
			house.dropTime = tag.getLong("dropTime");
			house.living = new ArrayList<String>();
			NBTTagList living = tag.getTagList("living");
			for (int i = 0; i != living.tagCount(); i++) {
				if (living.tagAt(i) != null) {
					house.living.add(((NBTTagString)living.tagAt(i)).data);
				}
			}
			return house;
		}
		return null;
	}
}
