package by.fxg.seaside.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class SeasidePackets implements IPacketHandler {
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (packet.channel.equals("ss")) {
			ByteArrayDataInput badi = ByteStreams.newDataInput(packet.data);
			SSPacket pack = this.getPacket(badi.readInt());
			if (packet != null) {
				pack.read(badi);
				pack.exec(manager, ((EntityPlayer)player),  ((EntityPlayer)player).worldObj.isRemote ? Side.CLIENT : Side.SERVER);
			}
		}
	}

	public SSPacket getPacket(int id) {
		switch (id) {
			case 0:	return new SPacketInteract(false);
			case 1: return new SPacketMarker.Add(null);
			case 2: return new SPacketMarker.Remove("");
			case 3: return new SPacketSendPlayerData(null);
			case 4: return new SPacketGuiData(null);
			case 5: return new SPacketHouseInfo(null, null);
			case 6: return new SPacketServerDialog(0, 0, 0, 0, 0, "");
			case 7: 
			default: return null;
		}
	}
	
	protected static Packet250CustomPayload preparePacket(SSPacket ss) {
		ByteArrayDataOutput bado = ByteStreams.newDataOutput();
		bado.writeInt(ss.getID());
		ss.write(bado);
		Packet250CustomPayload pak = new Packet250CustomPayload();
		pak.channel = "ss";
		pak.data = bado.toByteArray();
		pak.length = pak.data.length;
		return pak;
	}
}
