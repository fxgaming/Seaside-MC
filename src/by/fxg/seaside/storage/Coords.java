package by.fxg.seaside.storage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import net.minecraft.entity.Entity;

public class Coords {
	public int x, y, z;
	
	public Coords() {}
	public Coords(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Coords(int[] args) {
		this(args[0], args[1], args[2]);
	}
	public Coords(String[] args) {
		this(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]));
	}
	
	public boolean compare(int x, int y, int z) {
		return this.x == x && this.y == y && this.z == z;
	}
	
	public boolean compare(Coords c) {
		return this.x == c.x && this.y == c.y && this.z == c.z;
	}
	
	public static Coords getFromEntity(Entity e) {
		if (e == null) return null;
		return new Coords((int)Math.floor(e.posX), (int)Math.floor(e.posY), (int)Math.floor(e.posZ));
	}
	
	public void writeBytes(ByteArrayDataOutput bado) {
		bado.writeInt(this.x);
		bado.writeInt(this.y);
		bado.writeInt(this.z);
	}
	
	public Coords readBytes(ByteArrayDataInput badi) {
		this.x = badi.readInt();
		this.y = badi.readInt();
		this.z = badi.readInt();
		return this;
	}
	
	public String toString() {
		return "[" + this.x + ", " + this.y + ", " + this.z + "]";
	}
}
