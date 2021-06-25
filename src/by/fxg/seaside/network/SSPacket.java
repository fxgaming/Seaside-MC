package by.fxg.seaside.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;

public abstract class SSPacket {
	abstract public void write(ByteArrayDataOutput bado);
	abstract public void read(ByteArrayDataInput badi);
	abstract public void exec(INetworkManager iman, EntityPlayer player, Side side);
	abstract public int getID();
	
	public Packet get() {
		return SeasidePackets.preparePacket(this);
	}
	
	protected NBTTagCompound readNBTTagCompound(ByteArrayDataInput badi) {
		short short1 = badi.readShort();
		if (short1 > 0) {
			try {
				byte[] abyte = new byte[short1];
				badi.readFully(abyte);
				return CompressedStreamTools.decompress(abyte);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	protected void writeNBTTagCompound(NBTTagCompound tag, ByteArrayDataOutput bado) {
		if (tag == null) {
			bado.writeShort(-1);
		} else {
			byte[] abyte;
			try {
				abyte = CompressedStreamTools.compress(tag);
				bado.writeShort(abyte.length);
				bado.write(abyte);
			} catch (Exception e) {
				bado.writeShort(-1);
			}
		}
	}
}
