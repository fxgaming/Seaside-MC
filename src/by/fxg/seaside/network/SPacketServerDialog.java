package by.fxg.seaside.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.amain.GuiHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;

public class SPacketServerDialog extends SSPacket {
	private int dialogID;
	private int buttonID;
	private int mouseID;
	private int ext;
	private int index;
	private String inputData;
	
	public SPacketServerDialog(int dialogID, int buttonID, int mouseID, int ext, int index, String inputData) {
		this.dialogID = dialogID;
		this.buttonID = buttonID;
		this.mouseID = mouseID;
		this.ext = ext;
		this.index = index;
		this.inputData = inputData;
	}
	
	public void write(ByteArrayDataOutput bado) {
		bado.writeInt(this.dialogID);
		bado.writeInt(this.buttonID);
		bado.writeInt(this.mouseID);
		bado.writeInt(this.ext);
		bado.writeInt(this.index);
		bado.writeUTF(this.inputData);
	}

	public void read(ByteArrayDataInput badi) {
		this.dialogID = badi.readInt();
		this.buttonID = badi.readInt();
		this.mouseID = badi.readInt();
		this.ext = badi.readInt();
		this.index = badi.readInt();
		this.inputData = badi.readUTF();
	}

	public void exec(INetworkManager iman, EntityPlayer player, Side side) {
		if (Seaside.SERVER) {
			GuiHandler.instance.onButtonServerUse(player, this.dialogID, this.buttonID, this.mouseID, this.ext, this.index, this.inputData);
		}
	}

	public int getID() {
		return 6;
	}
}
