package by.fxg.sampui.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class SampDialogList<T> extends GuiContainer implements ISAMPDialog<SampDialogList<T>> {
	public int bSizeX = 53;
	public int bSizeY = 12;
	public int dialogid;
	public String title;
	public String[] text;
	public String button0;
	public String button1;
	public List<ISAMPCallback> callbacks = new ArrayList<ISAMPCallback>();
	public List<String> list = new ArrayList<String>();
	public int moveIndex = 0;
	public int selectedIndex = 0;
	public int tickSelectDelay = 0;
	public T subData;
	
	public SampDialogList(int dialogid, String title, String[] text, String[] list, String button0, String button1) {
		super(new Container() {
			public boolean canInteractWith(EntityPlayer entityplayer) {
				return true;
			}
		});
		int maxsize = SAMPUtils.setIfMaximal(128, title, 2, Minecraft.getMinecraft().fontRenderer);
		int maxysize = 121;
		this.dialogid = dialogid;
		this.title = title;
		this.text = text;
		if (list != null)
		for (String str : list) {
			maxsize = SAMPUtils.setIfMaximal(maxsize + 2, str, 8, Minecraft.getMinecraft().fontRenderer);
			this.list.add(str);
		}
		this.bSizeX = SAMPUtils.setIfMaximal(this.bSizeX, this.button0 = button0, 3, Minecraft.getMinecraft().fontRenderer);
		this.bSizeX = SAMPUtils.setIfMaximal(this.bSizeX, this.button1 = button1, 3, Minecraft.getMinecraft().fontRenderer);
		if (text != null)
		for (String str : text) {
			maxsize = SAMPUtils.setIfMaximal(maxsize, str, 2, Minecraft.getMinecraft().fontRenderer);
			maxysize += 10;
		}
		this.xSize = maxsize > bSizeX * 2 + 12 ? maxsize : bSizeX * 2 + 2;
		this.ySize = maxysize;
	}
	
	public SampDialogList addCallback(ISAMPCallback callback) {
		this.callbacks.add(callback);
		return this;
	}
	
	public SampDialogList setSubData(T subData) {
		this.subData = subData;
		return this;
	}
	
	public void updateScreen() {
        super.updateScreen();
        if (this.tickSelectDelay > 0) {
        	this.tickSelectDelay--;
        }
        int wheel = Mouse.getDWheel();
        if (wheel > 0) {
        	if (this.moveIndex > 0) {
        		this.moveIndex -= wheel / 120;
        		if (this.moveIndex < 0) {
        			this.moveIndex = 0;
        		}
        	}
        }
        if (wheel < 0) {
        	if (this.moveIndex + 9 < this.list.size()) {
        		this.moveIndex += -wheel / 120;
        		if (this.moveIndex + 9 > this.list.size()) {
        			this.moveIndex = this.list.size() - 10;
        		}
        	}
        }
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		DrawHelper.drawRect(this.guiLeft, this.guiTop, this.xSize, 11, "0x000000", 0.5F);
		DrawHelper.drawRect(this.guiLeft, this.guiTop, this.xSize, this.ySize, "0x000000", 0.4F);
		super.fontRenderer.drawString(this.title, this.guiLeft + 1, this.guiTop + 2, Color.WHITE.getRGB());
		int stepping = 2;
		if (this.text != null)
		for (String str : this.text) {
			super.fontRenderer.drawString(str, this.guiLeft + 1, this.guiTop + (stepping += 10), Color.WHITE.getRGB());
		}
		DrawHelper.drawRect(this.guiLeft + 2, this.guiTop + (stepping += 2) + 8, this.xSize - 6, 92, "0x000000", 0.4F);
		if (this.list.size() > 9) {
			int height = (int)(9.0D / ((double)this.list.size() / 92.0D));
			DrawHelper.drawRect(this.guiLeft + this.xSize - 4, this.guiTop + stepping + 8 -(int)((double)this.moveIndex / ((double)(this.list.size() - 9) / (double)(height - 92))), 2, height, "0xFF0000", 0.5F);
		} else {
			DrawHelper.drawRect(this.guiLeft + this.xSize - 4, this.guiTop + stepping + 8, 2, 92, "0xFF0000", 0.5F);
		}
		for (int i = 0; i != 9; i++) {
			if (this.list.size() > i + this.moveIndex) {
				if (SAMPUtils.isInAreaSized(par1, par2, this.guiLeft + 2, this.guiTop + (stepping += 10) - 1, this.xSize - 4, 8)) {
					DrawHelper.drawRect(this.guiLeft + 2, this.guiTop + stepping - 1, this.xSize - 6, 9, "0xFF0000", 0.2F);
				}
				if (this.selectedIndex > -1 && this.selectedIndex - this.moveIndex == i) {
					DrawHelper.drawRect(this.guiLeft + 2, this.guiTop + stepping - 1, this.xSize - 6, 9, "0xFF0000", 0.4F);
				}
				super.fontRenderer.drawString(this.list.get(i + this.moveIndex), this.guiLeft + 3, this.guiTop + stepping, Color.WHITE.getRGB());
			}
		}
		
		boolean button0 = SAMPUtils.isInAreaSized(par1, par2, this.guiLeft + this.xSize / 2 - bSizeX - 5, this.guiTop + this.ySize - bSizeY - 3, bSizeX, bSizeY);
		DrawHelper.drawRect(this.guiLeft + this.xSize / 2 - bSizeX - 5, this.guiTop + this.ySize - 15, bSizeX, bSizeY, button0 ? "0xFF0000" : "0xFFFFFF", 1.0F);
		DrawHelper.drawRect(this.guiLeft + this.xSize / 2 - bSizeX - 4, this.guiTop + this.ySize - 14, bSizeX - 2, bSizeY - 2, "0x000000", 1.0F);
		super.fontRenderer.drawString(this.button0, this.guiLeft + this.xSize / 2 - bSizeX / 2 - 5 - super.fontRenderer.getStringWidth(this.button0) / 2, this.guiTop + this.ySize - bSizeY - 1, Color.WHITE.getRGB());
		
		boolean button1 = SAMPUtils.isInAreaSized(par1, par2, this.guiLeft + this.xSize / 2 + 5, this.guiTop + this.ySize - bSizeY - 3, bSizeX, bSizeY);
		DrawHelper.drawRect(this.guiLeft + this.xSize / 2 + 5, this.guiTop + this.ySize - 15, bSizeX, bSizeY, button1 ? "0xFF0000" : "0xFFFFFF", 1.0F);
		DrawHelper.drawRect(this.guiLeft + this.xSize / 2 + 6, this.guiTop + this.ySize - 14, bSizeX - 2, bSizeY - 2, "0x000000", 1.0F);
		super.fontRenderer.drawString(this.button1, this.guiLeft + this.xSize / 2 + bSizeX / 2 + 5 - super.fontRenderer.getStringWidth(this.button1) / 2, this.guiTop + this.ySize - bSizeY - 1, Color.WHITE.getRGB());
	
	}
	
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
		if (par2 == 200 || par2 == 208) {
			if (par2 == 200) {
				if (this.selectedIndex > 0) {
					if (this.selectedIndex - this.moveIndex == 0) {
						this.moveIndex--;
						this.selectedIndex--;
					} else {
						this.selectedIndex--;
					}
				}
			} else {
				if (this.selectedIndex < this.list.size() - 1) {
					if (this.selectedIndex - this.moveIndex > 7) {
						this.moveIndex++;
						this.selectedIndex++;
					} else {
						this.selectedIndex++;
					}
				}
			}
		}
		if (par2 == 1 || par2 == 28) {
			for (ISAMPCallback callback : this.callbacks) {
				callback.onButtonUse(this, this.dialogid, par2 == 28 ? 0 : 1, 3, this.subData, this.selectedIndex);
			}
		}
	}
	
	protected void mouseClicked(int x, int y, int key) {
		int stepping = 4;
		if (this.text != null)
		for (String str : this.text) stepping += 10;
		for (int i = 0; i != 9; i++) {
			if (this.list.size() > i + this.moveIndex) {
				if (SAMPUtils.isInAreaSized(x, y, this.guiLeft + 2, this.guiTop + (stepping += 10) - 1, this.xSize - 4, 8)) {
					if (this.selectedIndex == this.moveIndex + i && this.tickSelectDelay > 0) {
						for (ISAMPCallback callback : this.callbacks) {
							callback.onButtonUse(this, this.dialogid, 2, key, this.subData, this.selectedIndex);
						}
					} else {
						this.selectedIndex = this.moveIndex + i;
						this.tickSelectDelay = 10;
					}
				}
			}
		}
		boolean button0 = SAMPUtils.isInAreaSized(x, y, this.guiLeft + this.xSize / 2 - bSizeX - 5, this.guiTop + this.ySize - bSizeY - 3, bSizeX, bSizeY);
		boolean button1 = SAMPUtils.isInAreaSized(x, y, this.guiLeft + this.xSize / 2 + 5, this.guiTop + this.ySize - bSizeY - 3, bSizeX, bSizeY);
		if (button0 || button1) {
			for (ISAMPCallback callback : this.callbacks) {
				callback.onButtonUse(this, this.dialogid, button0 ? 0 : 1, key, this.subData, this.selectedIndex);
			}
		}
	}

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {}
}
