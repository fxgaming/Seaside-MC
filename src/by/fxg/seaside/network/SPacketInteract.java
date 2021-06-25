package by.fxg.seaside.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.ServerProxy;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;

public class SPacketInteract extends SSPacket {
	private boolean isMainKey;
	public SPacketInteract(boolean isMainKey) {
		this.isMainKey = isMainKey;
	}
	
	public void write(ByteArrayDataOutput bado) {
		bado.writeBoolean(this.isMainKey);
	}
	
	public void read(ByteArrayDataInput badi) {
		this.isMainKey = badi.readBoolean();
	}

	public void exec(INetworkManager iman, EntityPlayer player, Side side) {
		if (side.isServer()) {
			Seaside.instance.eventHandlerSystem.onClientPlayerInteractionUsed(player, iman, this.isMainKey);
		}
	}

	public int getID() {
		return 0;
	}
}
