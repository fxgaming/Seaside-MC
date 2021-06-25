package by.fxg.seaside.network.minimap;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;

public class WaypointEntity extends Entity {
	private final Minecraft mc;
	private ArrayList unloadedEntity;

	public WaypointEntity(Minecraft mc) {
		super(mc.theWorld);
		this.mc = mc;
		this.setSize(0.0F, 0.0F);
		super.ignoreFrustumCheck = true;
		this.onUpdate();
	}

	public void onUpdate() {
		this.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
	}

	protected void entityInit() {
	}

	public boolean isInRangeToRenderVec3D(Vec3 vec3d) {
		return true;
	}

	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}
}
