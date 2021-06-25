package by.fxg.seaside.network.minimap;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiWaypointScreen extends GuiScreen implements GuiScreenInterface {
   static final int MIN_STRING_WIDTH = 64;
   static final int MAX_STRING_WIDTH = 160;
   static final int LIST_SIZE = 9;
   private ReiMinimap rmm;
   private List wayPts;
   private GuiWaypoint[] guiWaypoints;
   private GuiScrollbar scrollbar;
   private GuiSimpleButton backButton;
   private GuiSimpleButton addButton;
   private GuiSimpleButton removeFlagButton;
   private GuiSimpleButton removeApplyButton;
   private GuiSimpleButton removeCancelButton;
   private GuiSimpleButton prevDimension;
   private GuiSimpleButton nextDimension;
   private ConcurrentHashMap deleteObject;
   private int scroll;
   private boolean removeMode;
   private int maxStringWidth;
   private GuiScreen parent;

   public GuiWaypointScreen(GuiScreen parent) {
      this.rmm = ReiMinimap.instance;
      this.wayPts = this.rmm.getWaypoints();
      this.guiWaypoints = new GuiWaypoint[9];
      this.scrollbar = new GuiScrollbar(0, 0, 0, 12, 90);
      this.deleteObject = new ConcurrentHashMap();
      this.parent = parent;

      for(int i = 0; i < 9; ++i) {
         this.guiWaypoints[i] = new GuiWaypoint(i, this);
      }

   }

   public void initGui() {
      super.controlList.clear();
      Keyboard.enableRepeatEvents(true);
      GuiWaypoint[] arr$ = this.guiWaypoints;
      int bottom = arr$.length;

      for(int i$ = 0; i$ < bottom; ++i$) {
         GuiWaypoint gpt = arr$[i$];
         super.controlList.add(gpt);
      }

      super.controlList.add(this.scrollbar);
      this.updateWaypoints();
      int centerX = super.width / 2;
      bottom = super.height + 90 >> 1;
      this.backButton = new GuiSimpleButton(0, centerX - 65, bottom + 7, 40, 14, this.parent == null ? "Close" : "Back");
      super.controlList.add(this.backButton);
      this.addButton = new GuiSimpleButton(0, centerX - 20, bottom + 7, 40, 14, "Add");
      super.controlList.add(this.addButton);
      this.removeFlagButton = new GuiSimpleButton(0, centerX + 25, bottom + 7, 40, 14, "Remove");
      super.controlList.add(this.removeFlagButton);
      this.removeApplyButton = new GuiSimpleButton(0, centerX - 65, bottom + 7, 60, 14, "Remove");
      super.controlList.add(this.removeApplyButton);
      this.removeCancelButton = new GuiSimpleButton(0, centerX + 5, bottom + 7, 60, 14, "Cancel");
      super.controlList.add(this.removeCancelButton);
      this.prevDimension = new GuiSimpleButton(0, 0, 0, 14, 14, "<");
      super.controlList.add(this.prevDimension);
      this.nextDimension = new GuiSimpleButton(0, 0, 0, 14, 14, ">");
      super.controlList.add(this.nextDimension);
      this.setRemoveMode(this.removeMode);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void drawScreen(int mouseX, int mouseY, float f) {
      this.backButton.enabled = this.backButton.drawButton = !this.removeMode;
      this.addButton.enabled = this.addButton.drawButton = !this.removeMode;
      this.removeFlagButton.enabled = this.removeFlagButton.drawButton = !this.removeMode;
      this.removeApplyButton.enabled = this.removeApplyButton.drawButton = this.removeMode;
      this.removeCancelButton.enabled = this.removeCancelButton.drawButton = this.removeMode;
      this.addButton.enabled = this.rmm.getCurrentDimension() == this.rmm.getWaypointDimension();
      int gwpWidth = Math.min(160, this.maxStringWidth) + 16;
      int top = super.height - 90 >> 1;
      int bottom = super.height + 90 >> 1;
      int left = super.width - gwpWidth - 45 - 10 >> 1;
      int right = super.width + gwpWidth + 45 + 10 >> 1;
      drawRect(left - 2, top - 2, right + 2, bottom + 2, -1610612736);
      String title = String.format("Waypoints [%s]", ReiMinimap.instance.getDimensionName(ReiMinimap.instance.getWaypointDimension()));
      int titleWidth = super.fontRenderer.getStringWidth(title);
      int titleLeft = super.width - titleWidth >> 1;
      int titleRight = super.width + titleWidth >> 1;
      this.prevDimension.xPosition = titleLeft - 18;
      this.prevDimension.yPosition = top - 22;
      this.nextDimension.xPosition = titleRight + 4;
      this.nextDimension.yPosition = top - 22;
      super.drawScreen(mouseX, mouseY, f);
      drawRect(titleLeft - 2, top - 22, titleRight + 2, top - 8, -1610612736);
      this.drawCenteredString(super.fontRenderer, title, super.width / 2, top - 19, -1);
   }

   public void updateScreen() {
      int temp = (int)this.scrollbar.getValue();
      if (this.scroll != temp) {
         this.scroll = temp;
         this.setWaypoints();
      }

   }

   protected void keyTyped(char c, int i) {
      super.keyTyped(c, i);
      switch(i) {
      case 199:
         this.scrollbar.setValue(this.scrollbar.getMinimum());
         break;
      case 200:
         this.scrollbar.unitDecrement();
         break;
      case 201:
         this.scrollbar.blockDecrement();
      case 202:
      case 203:
      case 204:
      case 205:
      case 206:
      default:
         break;
      case 207:
         this.scrollbar.setValue(this.scrollbar.getMaximum());
         break;
      case 208:
         this.scrollbar.unitIncrement();
         break;
      case 209:
         this.scrollbar.blockIncrement();
      }

   }

   public void handleMouseInput() {
      super.handleMouseInput();
      int i = Mouse.getDWheel();
      if (i != 0) {
         i = i < 0 ? 3 : -3;
         this.scrollbar.setValue(this.scrollbar.getValue() + (float)i);
      }

   }

   protected void actionPerformed(GuiButton guibutton) {
      if (guibutton == this.backButton) {
         super.mc.displayGuiScreen(this.parent);
      }

      if (guibutton == this.removeFlagButton) {
         this.setRemoveMode(true);
      }

      if (guibutton == this.removeCancelButton) {
         this.setRemoveMode(false);
      }

      if (guibutton == this.removeApplyButton) {
         boolean remove = false;

         Waypoint wp;
         for(Iterator i$ = this.deleteObject.keySet().iterator(); i$.hasNext(); remove |= this.wayPts.remove(wp)) {
            wp = (Waypoint)i$.next();
         }

         if (remove) {
            this.rmm.saveWaypoints();
            this.updateWaypoints();
         }

         this.setRemoveMode(false);
      }

      if (guibutton == this.addButton && this.rmm.getCurrentDimension() == this.rmm.getWaypointDimension()) {
         super.mc.displayGuiScreen(new GuiWaypointEditorScreen(this, (Waypoint)null));
      }

      if (guibutton == this.prevDimension) {
         this.setRemoveMode(false);
         this.rmm.prevDimension();
         this.wayPts = this.rmm.getWaypoints();
         this.updateWaypoints();
      }

      if (guibutton == this.nextDimension) {
         this.setRemoveMode(false);
         this.rmm.nextDimension();
         this.wayPts = this.rmm.getWaypoints();
         this.updateWaypoints();
      }

   }

   void setRemoveMode(boolean b) {
      this.removeMode = b;
      this.deleteObject.clear();
   }

   boolean getRemoveMode() {
      return this.removeMode;
   }

   boolean isRemove(Waypoint wp) {
      return this.deleteObject.containsKey(wp);
   }

   void addWaypoint(Waypoint wp) {
      if (!this.wayPts.contains(wp)) {
         this.wayPts.add(wp);
         this.rmm.saveWaypoints();
         this.updateWaypoints();
         this.scrollbar.setValue(this.scrollbar.getMaximum());
      }

   }

   void removeWaypoint(Waypoint wp) {
      if (this.removeMode) {
         if (this.deleteObject.remove(wp) == null) {
            this.deleteObject.put(wp, wp);
         }
      } else if (this.wayPts.remove(wp)) {
         this.rmm.saveWaypoints();
         this.updateWaypoints();
      }

   }

   void updateWaypoint(Waypoint wp) {
      if (this.wayPts.contains(wp)) {
         this.rmm.saveWaypoints();
         this.updateWaypoints();
      }

   }

   private void updateWaypoints() {
      this.maxStringWidth = 64;
      int i = 0;

      for(int j = this.wayPts.size(); i < j; ++i) {
         Waypoint pt = (Waypoint)this.wayPts.get(i);
         this.maxStringWidth = Math.max(this.maxStringWidth, super.fontRenderer.getStringWidth(i + 1 + ") " + pt.name));
      }

      this.scrollbar.setMinimum(0.0F);
      this.scrollbar.setMaximum((float)this.wayPts.size());
      this.scrollbar.setVisibleAmount((float)Math.min(9, this.wayPts.size()));
      this.scroll = (int)this.scrollbar.getValue();
      this.updateGui();
      this.setWaypoints();
   }

   private void updateGui() {
      int gwpWidth = Math.min(160, this.maxStringWidth) + 16;
      int top = super.height - 90 - 4 >> 1;
      int left = super.width - gwpWidth - 45 - 12 >> 1;
      int right = super.width + gwpWidth + 45 + 12 >> 1;

      for(int i = 0; i < 9; ++i) {
         this.guiWaypoints[i].bounds(left + 2, top + 2 + 10 * i, gwpWidth + 45, 9);
      }

      this.scrollbar.xPosition = right - 12;
      this.scrollbar.yPosition = top + 2;
   }

   private void setWaypoints() {
      for(int i = 0; i < 9; ++i) {
         int num = i + this.scroll;
         this.guiWaypoints[i].setWaypoint(num + 1, num < this.wayPts.size() ? (Waypoint)this.wayPts.get(num) : null);
      }

   }

   Minecraft getMinecraft() {
      return super.mc;
   }
}
