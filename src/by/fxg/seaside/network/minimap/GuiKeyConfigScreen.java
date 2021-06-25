package by.fxg.seaside.network.minimap;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiKeyConfigScreen extends GuiScreen implements GuiScreenInterface {
	private int top;
	private int bottom;
	private int left;
	private int right;
	private GuiSimpleButton okButton;
	private GuiSimpleButton cancelButton;
	private GuiSimpleButton defaultButton;
	private GuiKeyConfigButton edit;
	private int[] currentKeyCode;

	GuiKeyConfigScreen() {
		KeyInput[] keys = KeyInput.values();
		this.currentKeyCode = new int[keys.length];

		for (int i = 0; i < this.currentKeyCode.length; ++i) {
			this.currentKeyCode[i] = keys[i].getKey();
		}

	}

	public void initGui() {
		int label = this.calcLabelWidth();
		int button = this.calcButtonWidth();
		this.left = (super.width - label - button - 12) / 2;
		this.right = (super.width + label + button + 12) / 2;
		this.top = (super.height - KeyInput.values().length * 10) / 2;
		this.bottom = (super.height + KeyInput.values().length * 10) / 2;
		int y = this.top;
		KeyInput[] arr$ = KeyInput.values();
		int len$ = arr$.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			KeyInput ki = arr$[i$];
			GuiKeyConfigButton gkcb = new GuiKeyConfigButton(this, 0, this.left, y, label, button, ki);
			super.controlList.add(gkcb);
			y += 10;
		}

		int centerX = super.width / 2;
		this.okButton = new GuiSimpleButton(0, centerX - 74, this.bottom + 7, 46, 14, "OK");
		super.controlList.add(this.okButton);
		this.cancelButton = new GuiSimpleButton(0, centerX - 23, this.bottom + 7, 46, 14, "Cancel");
		super.controlList.add(this.cancelButton);
		this.defaultButton = new GuiSimpleButton(0, centerX + 28, this.bottom + 7, 46, 14, "Default");
		super.controlList.add(this.defaultButton);
	}

	private int calcLabelWidth() {
		FontRenderer fr = super.mc.fontRenderer;
		int width = -1;
		KeyInput[] arr$ = KeyInput.values();
		int len$ = arr$.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			KeyInput ki = arr$[i$];
			width = Math.max(width, fr.getStringWidth(ki.name()));
		}

		return width;
	}

	private int calcButtonWidth() {
		FontRenderer fr = super.mc.fontRenderer;
		int width = 30;
		KeyInput[] arr$ = KeyInput.values();
		int len$ = arr$.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			KeyInput ki = arr$[i$];
			width = Math.max(width, fr.getStringWidth(">" + ki.getKeyName() + "<"));
		}

		return width + 2;
	}

	public void drawScreen(int i, int j, float f) {
		String title = "Key Config";
		int titleWidth = super.fontRenderer.getStringWidth(title);
		int titleLeft = super.width - titleWidth >> 1;
		int titleRight = super.width + titleWidth >> 1;
		drawRect(titleLeft - 2, this.top - 22, titleRight + 2, this.top - 8, -1610612736);
		this.drawCenteredString(super.fontRenderer, title, super.width / 2, this.top - 19, -1);
		drawRect(this.left - 2, this.top - 2, this.right + 2, this.bottom + 1, -1610612736);
		super.drawScreen(i, j, f);
	}

	GuiKeyConfigButton getEditKeyConfig() {
		return this.edit;
	}

	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton instanceof GuiKeyConfigButton) {
			this.edit = (GuiKeyConfigButton) guibutton;
		}

		if (guibutton == this.okButton) {
			if (KeyInput.saveKeyConfig()) {
				ReiMinimap.instance.chatInfo("§E[Rei's Minimap] Keyconfig Saved.");
			} else {
				ReiMinimap.instance.chatInfo("§E[Rei's Minimap] Error Keyconfig Saving.");
			}

			super.mc.displayGuiScreen(new GuiOptionScreen());
		}

		KeyInput[] keys;
		int i;
		if (guibutton == this.defaultButton) {
			keys = KeyInput.values();
			i = keys.length;

			for (int i$ = 0; i$ < i; ++i$) {
				KeyInput ki = keys[i$];
				ki.setDefault();
			}

			super.controlList.clear();
			this.initGui();
		}

		if (guibutton == this.cancelButton) {
			keys = KeyInput.values();

			for (i = 0; i < this.currentKeyCode.length; ++i) {
				keys[i].setKey(this.currentKeyCode[i]);
			}

			super.mc.displayGuiScreen(new GuiOptionScreen());
		}

	}

	protected void keyTyped(char c, int i) {
		if (this.edit != null) {
			this.edit.getKeyInput().setKey(i);
			this.edit = null;
			super.controlList.clear();
			this.initGui();
		} else if (i == 1) {
			KeyInput[] keys = KeyInput.values();

			for (int j = 0; j < this.currentKeyCode.length; ++j) {
				keys[j].setKey(this.currentKeyCode[j]);
			}

			super.mc.displayGuiScreen((GuiScreen) null);
		}

	}
}
