package by.fxg.seaside.network.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiKeyConfigButton extends GuiButton {
	private GuiKeyConfigScreen parrent;
	private KeyInput keyInput;
	private String labelText;
	private String buttonText;
	private int labelWidth;
	private int buttonWidth;

	public GuiKeyConfigButton(GuiKeyConfigScreen parrent, int id, int x, int y, int label, int button, KeyInput key) {
		super(id, x, y, label + 12 + button, 9, "");
		this.parrent = parrent;
		this.keyInput = key;
		this.labelWidth = label;
		this.buttonWidth = button;
		this.labelText = this.keyInput.label();
		this.buttonText = this.keyInput.getKeyName();
	}

	public void drawButton(Minecraft minecraft, int i, int j) {
		if (this.keyInput != null) {
			boolean b = i >= super.xPosition && i < super.xPosition + super.width && j >= super.yPosition && j < super.yPosition + super.height;
			this.drawString(minecraft.fontRenderer, this.labelText, super.xPosition, super.yPosition + 1, b ? -1 : -4144960);
			String text = this.buttonText;
			if (this == this.parrent.getEditKeyConfig()) {
				text = ">" + text + "<";
			}

			b = i >= super.xPosition + super.width - this.buttonWidth && i < super.xPosition + super.width && j >= super.yPosition && j < super.yPosition + super.height;
			int color = b ? 1728053247 : (this.keyInput.getKey() == 0 ? (this.keyInput.isDefault() ? -1610612481 : -1593868288) : (this.keyInput.isDefault() ? -1610547456 : -1593901056));
			drawRect(super.xPosition + super.width - this.buttonWidth, super.yPosition, super.xPosition + super.width, super.yPosition + super.height, color);
			this.drawCenteredString(minecraft.fontRenderer, text, super.xPosition + super.width - this.buttonWidth / 2, super.yPosition + 1, -1);
		}
	}

	public boolean mousePressed(Minecraft minecraft, int i, int j) {
		return i >= super.xPosition + super.width - this.buttonWidth && i < super.xPosition + super.width && j >= super.yPosition && j < super.yPosition + super.height;
	}

	void setBounds(int x, int y, int label, int button) {
		super.xPosition = x;
		super.yPosition = y;
		this.labelWidth = label;
		this.buttonWidth = button;
		super.width = label + button + 2;
	}

	KeyInput getKeyInput() {
		return this.keyInput;
	}
}
