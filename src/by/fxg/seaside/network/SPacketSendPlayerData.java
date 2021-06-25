package by.fxg.seaside.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import by.fxg.seaside.managers.PlayerManager;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;

public class SPacketSendPlayerData extends SSPacket {
	private NBTTagCompound compound;
	private int entityID;
	
	public SPacketSendPlayerData(PlayerManager pm) {
		if (pm != null) {
			pm.saveNBTData(this.compound = new NBTTagCompound());
			this.entityID = pm.player.entityId;
		}
	}
	
	public void write(ByteArrayDataOutput bado) {
		this.writeNBTTagCompound(this.compound, bado);
		bado.writeInt(this.entityID);
	}

	public void read(ByteArrayDataInput badi) {
		this.compound = this.readNBTTagCompound(badi);
		this.entityID = badi.readInt();
	}

	public void exec(INetworkManager iman, EntityPlayer player, Side side) {
		if (player.worldObj.getEntityByID(this.entityID) != null && player.worldObj.getEntityByID(this.entityID) instanceof EntityPlayer) {
			PlayerManager.get((EntityPlayer)player.worldObj.getEntityByID(this.entityID)).loadNBTData(this.compound);
		}
	}

	public int getID() {
		return 3;
	}
}
