package by.fxg.seaside.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import by.fxg.seaside.amain.GuiHandler;
import by.fxg.seaside.utils.Utils;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;

public class SPacketGuiData extends SSPacket {
	public int key;
	private String[][] guiData;
	public SPacketGuiData(String[][] guiData) {
		this.key = Utils.genKey();
		this.guiData = guiData;
	}
	
	public void write(ByteArrayDataOutput bado) {
		bado.writeInt(this.key);
		bado.writeInt(this.guiData.length);
		for (String[] str : guiData) {
			bado.writeInt(str.length);
			for (String str$ : str) {
				bado.writeUTF(str$);
			}
		}
	}
	public void read(ByteArrayDataInput badi) {
		this.key = badi.readInt();
		this.guiData = new String[badi.readInt()][];
		for (int i = 0; i != this.guiData.length; i++) {
			this.guiData[i] = new String[badi.readInt()];
			for (int j = 0; j != this.guiData[i].length; j++) {
				this.guiData[i][j] = badi.readUTF();
			}
		}
	}

	public void exec(INetworkManager iman, EntityPlayer player, Side side) {
		GuiHandler.instance.guiData.put(this.key, this.guiData);
	}

	public int getID() {
		return 4;
	}
}
