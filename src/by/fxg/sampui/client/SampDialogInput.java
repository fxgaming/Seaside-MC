package by.fxg.sampui.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class SampDialogInput<T> extends GuiContainer implements ISAMPDialog<SampDialogInput<T>> {
	public int bSizeX = 53;
	public int bSizeY = 12;
	public int dialogid;
	public String title;
	public String[] text;
	public String button0;
	public String button1;
	public GuiTextField gtf;
	public List<ISAMPCallback> callbacks = new ArrayList<ISAMPCallback>();
	public T subData;
	
	public SampDialogInput(int dialogid, String title, String[] text, String button0, String button1) {
		super(new Container() {
			public boolean canInteractWith(EntityPlayer entityplayer) {
				return true;
			}
		});
		this.dialogid = dialogid;
		this.title = title;
		this.text = text;
		this.bSizeX = SAMPUtils.setIfMaximal(this.bSizeX, this.button0 = button0, 3, Minecraft.getMinecraft().fontRenderer);
		this.bSizeX = SAMPUtils.setIfMaximal(this.bSizeX, this.button1 = button1, 3, Minecraft.getMinecraft().fontRenderer);
		int maxsize = SAMPUtils.setIfMaximal(128, title, 2, Minecraft.getMinecraft().fontRenderer);
		int maxysize = 39;
		if (text != null)
		for (String str : text) {
			maxsize = SAMPUtils.setIfMaximal(maxsize, str, 2, Minecraft.getMinecraft().fontRenderer);
			maxysize += 10;
		}
		this.xSize = maxsize > bSizeX * 2 + 12 ? maxsize : bSizeX * 2 + 2;
		this.ySize = maxysize;
	}
	
	public SampDialogInput addCallback(ISAMPCallback callback) {
		this.callbacks.add(callback);
		return this;
	}
	
	public SampDialogInput setSubData(T subData) {
		this.subData = subData;
		return this;
	}
	
	public void initGui() {
		super.initGui();
		this.gtf = new GuiTextField(this.fontRenderer, this.guiLeft + 2, this.guiTop + this.ySize - 27, this.xSize - 4, 10);
        this.gtf.setMaxStringLength(256);
        this.gtf.setFocused(true);
    }
	
	public void updateScreen() {
        super.updateScreen();
        this.gtf.updateCursorCounter();
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
		boolean button0 = SAMPUtils.isInAreaSized(par1, par2, this.guiLeft + this.xSize / 2 - bSizeX - 5, this.guiTop + this.ySize - bSizeY - 3, bSizeX, bSizeY);
		DrawHelper.drawRect(this.guiLeft + this.xSize / 2 - bSizeX - 5, this.guiTop + this.ySize - 15, bSizeX, bSizeY, button0 ? "0xFF0000" : "0xFFFFFF", 1.0F);
		DrawHelper.drawRect(this.guiLeft + this.xSize / 2 - bSizeX - 4, this.guiTop + this.ySize - 14, bSizeX - 2, bSizeY - 2, "0x000000", 1.0F);
		super.fontRenderer.drawString(this.button0, this.guiLeft + this.xSize / 2 - bSizeX / 2 - 5 - super.fontRenderer.getStringWidth(this.button0) / 2, this.guiTop + this.ySize - bSizeY - 1, Color.WHITE.getRGB());
		
		boolean button1 = SAMPUtils.isInAreaSized(par1, par2, this.guiLeft + this.xSize / 2 + 5, this.guiTop + this.ySize - bSizeY - 3, bSizeX, bSizeY);
		DrawHelper.drawRect(this.guiLeft + this.xSize / 2 + 5, this.guiTop + this.ySize - 15, bSizeX, bSizeY, button1 ? "0xFF0000" : "0xFFFFFF", 1.0F);
		DrawHelper.drawRect(this.guiLeft + this.xSize / 2 + 6, this.guiTop + this.ySize - 14, bSizeX - 2, bSizeY - 2, "0x000000", 1.0F);
		super.fontRenderer.drawString(this.button1, this.guiLeft + this.xSize / 2 + bSizeX / 2 + 5 - super.fontRenderer.getStringWidth(this.button1) / 2, this.guiTop + this.ySize - bSizeY - 1, Color.WHITE.getRGB());
	
		this.gtf.drawTextBox();
	}
	
	protected void keyTyped(char par1, int par2) {
		if (!this.gtf.isFocused()) {
			super.keyTyped(par1, par2);
		}
		if (par2 == 1 || par2 == 28) {
			for (ISAMPCallback callback : this.callbacks) {
				callback.onButtonUse(this, this.dialogid, par2 == 28 ? 0 : 1, 3, this.subData, this.gtf.getText());
			}
		}
    	this.gtf.textboxKeyTyped(par1, par2);
	}
	
	protected void mouseClicked(int x, int y, int key) {
		this.gtf.mouseClicked(x, y, key);
		boolean button0 = SAMPUtils.isInAreaSized(x, y, this.guiLeft + this.xSize / 2 - bSizeX - 5, this.guiTop + this.ySize - bSizeY - 3, bSizeX, bSizeY);
		boolean button1 = SAMPUtils.isInAreaSized(x, y, this.guiLeft + this.xSize / 2 + 5, this.guiTop + this.ySize - bSizeY - 3, bSizeX, bSizeY);
		if (button0 || button1) {
			for (ISAMPCallback callback : this.callbacks) {
				callback.onButtonUse(this, this.dialogid, button0 ? 0 : 1, key, this.subData, this.gtf.getText());
			}
		}
	}

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {}
}
