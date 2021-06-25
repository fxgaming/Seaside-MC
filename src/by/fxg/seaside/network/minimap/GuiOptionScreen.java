package by.fxg.seaside.network.minimap;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiOptionScreen extends GuiScreen implements GuiScreenInterface {
	private static final int LIGHTING_VERSION = 16844800;
	private static final int SUNRISE_DIRECTION = 16844931;
	public static final int minimapMenu = 0;
	public static final int optionMinimap = 1;
	public static final int optionSurfaceMap = 2;
	public static final int optionEntitiesRadar = 3;
	public static final int optionMarker = 4;
	public static final int aboutMinimap = 5;
	private static final String[] TITLE_STRING;
	private int page;
	private ArrayList buttonList = new ArrayList();
	private GuiSimpleButton exitMenu;
	private GuiSimpleButton waypoint;
	private GuiSimpleButton keyconfig;
	private int top;
	private int left;
	private int right;
	private int bottom;
	private int centerX;
	private int centerY;

	public GuiOptionScreen() {
	}

	GuiOptionScreen(int page) {
		this.page = page;
	}

	public void initGui() {
		this.centerX = super.width / 2;
		this.centerY = super.height / 2;
		super.controlList.clear();
		this.buttonList.clear();
		EnumOption[] arr$ = EnumOption.values();
		int len$ = arr$.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			EnumOption eo = arr$[i$];
			if (eo.getPage() == this.page && (!super.mc.theWorld.isRemote || eo != EnumOption.ENTITIES_RADAR_OPTION || ReiMinimap.instance.getAllowEntitiesRadar()) && eo != EnumOption.DIRECTION_TYPE) {
				GuiOptionButton button = new GuiOptionButton(super.mc.fontRenderer, eo);
				button.setValue(ReiMinimap.instance.getOption(eo));
				super.controlList.add(button);
				this.buttonList.add(button);
			}
		}

		this.left = super.width - GuiOptionButton.getWidth() >> 1;
		this.top = super.height - this.buttonList.size() * 10 >> 1;
		this.right = super.width + GuiOptionButton.getWidth() >> 1;
		this.bottom = super.height + this.buttonList.size() * 10 >> 1;

		for (int i = 0; i < this.buttonList.size(); ++i) {
			GuiOptionButton button = (GuiOptionButton) this.buttonList.get(i);
			button.xPosition = this.left;
			button.yPosition = this.top + i * 10;
		}

		if (this.page == 0) {
			this.exitMenu = new GuiSimpleButton(0, this.centerX - 95, this.bottom + 7, 60, 14, "Exit Menu");
			super.controlList.add(this.exitMenu);
			this.waypoint = new GuiSimpleButton(1, this.centerX - 30, this.bottom + 7, 60, 14, "Waypoints");
			super.controlList.add(this.waypoint);
			this.keyconfig = new GuiSimpleButton(2, this.centerX + 35, this.bottom + 7, 60, 14, "Keyconfig");
			super.controlList.add(this.keyconfig);
		} else {
			this.exitMenu = new GuiSimpleButton(0, this.centerX - 30, this.bottom + 7, 60, 14, "Back");
			super.controlList.add(this.exitMenu);
		}

	}

	public void drawScreen(int i, int j, float f) {
		drawRect(this.left - 2, this.top - 2, this.right + 2, this.bottom + 1, -1610612736);
		super.drawScreen(i, j, f);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton instanceof GuiOptionButton) {
			GuiOptionButton gob = (GuiOptionButton) guibutton;
			ReiMinimap.instance.setOption(gob.getOption(), gob.getValue());
			ReiMinimap.instance.saveOptions();
		}

		if (guibutton instanceof GuiSimpleButton) {
			if (guibutton == this.exitMenu) {
				super.mc.displayGuiScreen(this.page == 0 ? null : new GuiOptionScreen(0));
			}

			if (guibutton == this.waypoint) {
				super.mc.displayGuiScreen(new GuiWaypointScreen(this));
			}

			if (guibutton == this.keyconfig) {
				super.mc.displayGuiScreen(new GuiKeyConfigScreen());
			}
		}

	}

	static {
		TITLE_STRING = new String[] { "POSRAL" };
	}
}
