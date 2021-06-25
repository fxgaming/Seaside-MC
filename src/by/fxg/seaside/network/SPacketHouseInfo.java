package by.fxg.seaside.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import by.fxg.seaside.amain.EventHandlerUI;
import by.fxg.seaside.storage.Coords;
import by.fxg.seaside.systems.houses.House;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;

public class SPacketHouseInfo extends SSPacket {
	public House house;
	public Coords coords;
	public SPacketHouseInfo(House house, Coords coords) {
		this.house = house;
		this.coords = coords;
	}
	
	public void write(ByteArrayDataOutput bado) {
		this.writeNBTTagCompound(this.house.save(), bado);
		this.coords.writeBytes(bado);
	}

	public void read(ByteArrayDataInput badi) {
		this.house = House.get(this.readNBTTagCompound(badi));
		this.coords = new Coords().readBytes(badi);
	}

	public void exec(INetworkManager iman, EntityPlayer player, Side side) {
		EventHandlerUI.instance.houseInfo = this.house;
		EventHandlerUI.instance.houseInfoPos = this.coords;
	}

	public int getID() {
		return 5;
	}
}
