package by.fxg.sampui.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class SampDialogText<T> extends GuiContainer implements ISAMPDialog<SampDialogText<T>> {
	public int bSizeX = 53;
	public int bSizeY = 12;
	public int dialogid;
	public String title;
	public String[] text;
	public String button0;
	public List<ISAMPCallback> callbacks = new ArrayList<ISAMPCallback>();
	public T subData;
	
	public SampDialogText(int dialogid, String title, String[] text, String button0) {
		super(new Container() {
			public boolean canInteractWith(EntityPlayer entityplayer) {
				return true;
			}
		});
		this.dialogid = dialogid;
		this.title = title;
		this.text = text;
		this.bSizeX = SAMPUtils.setIfMaximal(this.bSizeX, this.button0 = button0, 3, Minecraft.getMinecraft().fontRenderer);
		int maxsize = SAMPUtils.setIfMaximal(128, title, 2, Minecraft.getMinecraft().fontRenderer);
		int maxysize = 27;
		if (text != null)
		for (String str : text) {
			maxsize = SAMPUtils.setIfMaximal(maxsize, str, 2, Minecraft.getMinecraft().fontRenderer);
			maxysize += 10;
		}
		this.xSize = maxsize > bSizeX + 2 ? maxsize : bSizeX + 2;
		this.ySize = maxysize;
	}
	
	public SampDialogText addCallback(ISAMPCallback callback) {
		this.callbacks.add(callback);
		return this;
	}
	
	public SampDialogText setSubData(T subData) {
		this.subData = subData;
		return this;
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		DrawHelper.drawRect(this.guiLeft, this.guiTop, this.xSize, 11, "0x000000", 0.6F);
		DrawHelper.drawRect(this.guiLeft, this.guiTop, this.xSize, this.ySize, "0x000000", 0.6F);
		super.fontRenderer.drawString(this.title, this.guiLeft + 1, this.guiTop + 2, Color.WHITE.getRGB());
		int stepping = 2;
		if (this.text != null)
		for (String str : this.text) {
			super.fontRenderer.drawString(str, this.guiLeft + 1, this.guiTop + (stepping += 10), Color.WHITE.getRGB());
		}
		boolean button = SAMPUtils.isInAreaSized(par1, par2, this.guiLeft + this.xSize / 2 - 26, this.guiTop + this.ySize - 15, 53, 12);
		DrawHelper.drawRect(this.guiLeft + this.xSize / 2 - 26, this.guiTop + this.ySize - 15, 53, 12, button ? "0xFF0000" : "0xFFFFFF", 1.0F);
		DrawHelper.drawRect(this.guiLeft + this.xSize / 2 - 25, this.guiTop + this.ySize - 14, 51, 10, "0x000000", 1.0F);
		super.fontRenderer.drawString(this.button0, this.guiLeft + this.xSize / 2 - super.fontRenderer.getStringWidth(this.button0) / 2, this.guiTop + this.ySize - 13, Color.WHITE.getRGB());
	}
	
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
		if (par2 == 1 || par2 == 28) {
			for (ISAMPCallback callback : this.callbacks) {
				callback.onButtonUse(this, this.dialogid, par2 == 28 ? 0 : 1, 3, this.subData);
			}
		}
	}
	
	protected void mouseClicked(int x, int y, int key) {
		boolean button = SAMPUtils.isInAreaSized(x, y, this.guiLeft + this.xSize / 2 - 26, this.guiTop + this.ySize - 15, 53, 12);
		if (button) {
			for (ISAMPCallback callback : this.callbacks) {
				callback.onButtonUse(this, this.dialogid, 0, key, this.subData);
			}
		}
	}

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {}
	public void onGuiClosed() {}
}
