package by.fxg.seaside.network.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

class GuiWaypoint extends GuiButton {
   private static final int[] COLOR1 = new int[]{-1, -65536};
   private static final int[] COLOR2 = new int[]{-4144960, -4194304};
   private static final int COLOR_SIZE = 9;
   private static final int BUTTON_SIZE = 30;
   private static final int ADD_SPACE = 2;
   static final int SIZE = 45;
   private GuiWaypointScreen gws;
   private Waypoint waypoint;
   private int number;
   private String name;
   private int top;
   private int bottom;
   private int left;
   private int right;
   private int ctop;
   private int cbottom;
   private int cleft;
   private int cright;
   private int btop;
   private int bbottom;
   private int bleft;
   private int bright;
   private long clickTime = System.nanoTime();

   GuiWaypoint(int i, GuiWaypointScreen gws) {
      super(i, 0, 0, 0, 0, (String)null);
      this.gws = gws;
   }

   void setWaypoint(int num, Waypoint pt) {
      this.number = num;
      this.waypoint = pt;
      this.name = null;
   }

   public void drawButton(Minecraft minecraft, int i, int j) {
      if (this.waypoint != null) {
         FontRenderer fontRenderer = minecraft.fontRenderer;
         if (this.name == null) {
            for(this.name = this.number + ") " + this.waypoint.name; fontRenderer.getStringWidth(this.name) > 160; this.name = this.name.substring(0, this.name.length() - 1)) {
               ;
            }
         }

         boolean hover = this.mouseIn(i, j);
         this.drawString(fontRenderer, this.name, super.xPosition + 1, super.yPosition + 1, (hover ? COLOR1 : COLOR2)[this.waypoint.type]);
         boolean tooltip = hover && i < this.cleft;
         int r = (int)(this.waypoint.red * 255.0F) & 255;
         int g = (int)(this.waypoint.green * 255.0F) & 255;
         int b = (int)(this.waypoint.blue * 255.0F) & 255;
         int color = -16777216 | r << 16 | g << 8 | b;
         drawRect(this.cleft, this.ctop, this.cright, this.cbottom, color);
         hover = this.buttonIn(i, j);
         String text = this.gws.getRemoveMode() ? (this.gws.isRemove(this.waypoint) ? "X" : "KEEP") : (this.waypoint.enable ? "ON" : "OFF");
         color = hover ? -2130706433 : (text == "X" ? -1593901056 : (text == "KEEP" ? -1610547456 : (this.waypoint.enable ? -1610547456 : -1593901056)));
         drawRect(this.bleft, this.btop, this.bright, this.bbottom, color);
         this.drawCenteredString(minecraft.fontRenderer, text, this.bleft + this.bright >> 1, this.btop + 1, -1);
         if (tooltip) {
            String tooltipText = String.format("X:%d, Y:%d, Z:%d", this.waypoint.x, this.waypoint.y, this.waypoint.z);
            int width = fontRenderer.getStringWidth(tooltipText);
            int drawLeft = i - width / 2 - 1;
            int drawRight = drawLeft + width + 2;
            drawRect(drawLeft, j - 11, drawRight, j - 1, -1610612736);
            this.drawCenteredString(fontRenderer, tooltipText, i, j - 10, -1);
         }

      }
   }

   public boolean mousePressed(Minecraft minecraft, int i, int j) {
      if (this.waypoint == null) {
         return false;
      } else {
         if (this.mouseIn(i, j)) {
            if (this.colorIn(i, j)) {
               this.waypoint.red = (float)Math.random();
               this.waypoint.green = (float)Math.random();
               this.waypoint.blue = (float)Math.random();
               this.gws.updateWaypoint(this.waypoint);
               return true;
            }

            if (this.buttonIn(i, j)) {
               if (this.gws.getRemoveMode()) {
                  this.gws.removeWaypoint(this.waypoint);
               } else {
                  this.waypoint.enable = !this.waypoint.enable;
                  this.gws.updateWaypoint(this.waypoint);
               }

               return true;
            }

            long time = System.nanoTime();
            if (!this.gws.getRemoveMode() && time < this.clickTime + 300000000L) {
               minecraft.displayGuiScreen(new GuiWaypointEditorScreen(this.gws, this.waypoint));
               return true;
            }

            this.clickTime = time;
         }

         return false;
      }
   }

   void bounds(int x, int y, int w, int h) {
      super.xPosition = x;
      super.yPosition = y;
      super.width = w;
      super.height = h;
      this.top = y;
      this.bottom = y + h;
      this.left = x;
      this.right = x + w;
      this.ctop = this.top;
      this.cbottom = this.bottom;
      this.cright = this.right - 2 - 30 - 2;
      this.cleft = this.cright - 9;
      this.btop = this.top;
      this.bbottom = this.bottom;
      this.bright = this.right - 2;
      this.bleft = this.bright - 30;
   }

   private boolean mouseIn(int x, int y) {
      return y >= this.top && y < this.bottom && x >= this.left && x < this.right;
   }

   private boolean colorIn(int x, int y) {
      return y >= this.ctop && y < this.cbottom && x >= this.cleft && x < this.cright;
   }

   private boolean buttonIn(int x, int y) {
      return y >= this.btop && y < this.bbottom && x >= this.bleft && x < this.bright;
   }
}
