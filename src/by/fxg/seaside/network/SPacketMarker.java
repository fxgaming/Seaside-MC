package by.fxg.seaside.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import by.fxg.seaside.ClientProxy;
import by.fxg.seaside.amain.EventHandlerUI;
import by.fxg.seaside.storage.Coords;
import by.fxg.seaside.systems.markers.Marker;
import by.fxg.seaside.systems.markers.MarkerColor;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;

public class SPacketMarker {
	public static class Add extends SSPacket {
		public Marker marker;
		public Add(Marker marker) {
			this.marker = marker;
		}
		
		public void write(ByteArrayDataOutput bado) {
			bado.writeUTF(this.marker.name);
			bado.writeInt(this.marker.color.ordinal());
			this.marker.pos.writeBytes(bado);
		}
		
		public void read(ByteArrayDataInput badi) {
			this.marker = new Marker(badi.readUTF(), MarkerColor.getByID(badi.readInt()), new Coords().readBytes(badi));
		}

		public void exec(INetworkManager iman, EntityPlayer player, Side side) {
			EventHandlerUI.instance.addSetMarker(this.marker);
		}

		public int getID() {
			return 1;
		}
	}
	
	public static class Remove extends SSPacket {
		public String name;
		public Remove(String name) {
			this.name = name;
		}
		
		public void write(ByteArrayDataOutput bado) {
			bado.writeUTF(this.name);
		}
		
		public void read(ByteArrayDataInput badi) {
			this.name = badi.readUTF();
		}

		public void exec(INetworkManager iman, EntityPlayer player, Side side) {
			EventHandlerUI.instance.removeMarker(this.name);
		}

		public int getID() {
			return 2;
		}
	}
}
