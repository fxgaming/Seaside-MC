package by.fxg.seaside.network.minimap;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

public class GuiWaypointEditorScreen extends GuiScreen implements GuiScreenInterface {
   private GuiWaypointScreen parrent;
   private Waypoint waypoint;
   private Waypoint waypointBackup;
   private GuiTextField nameTextField;
   private GuiTextField xCoordTextField;
   private GuiTextField yCoordTextField;
   private GuiTextField zCoordTextField;
   private GuiScrollbar[] rgb;
   private GuiSimpleButton okButton;
   private GuiSimpleButton cancelButton;

   public GuiWaypointEditorScreen(Minecraft mc, Waypoint waypoint) {
      this.waypoint = waypoint;
      this.waypointBackup = waypoint == null ? null : new Waypoint(waypoint);
      String name;
      int x;
      int y;
      int z;
      if (waypoint == null) {
         name = "";
         EntityPlayer player = mc.thePlayer;
         x = MathHelper.floor_double(player.posX);
         y = MathHelper.floor_double(player.posY);
         z = MathHelper.floor_double(player.posZ);
      } else {
         name = waypoint.name;
         x = waypoint.x;
         y = waypoint.y;
         z = waypoint.z;
      }

      this.nameTextField = new GuiTextField(name);
      this.nameTextField.setInputType(0);
      this.nameTextField.active();
      this.xCoordTextField = new GuiTextField(Integer.toString(x));
      this.xCoordTextField.setInputType(1);
      this.yCoordTextField = new GuiTextField(Integer.toString(y));
      this.yCoordTextField.setInputType(2);
      this.zCoordTextField = new GuiTextField(Integer.toString(z));
      this.zCoordTextField.setInputType(1);
      this.nameTextField.setNext(this.xCoordTextField);
      this.nameTextField.setPrev(this.zCoordTextField);
      this.xCoordTextField.setNext(this.yCoordTextField);
      this.xCoordTextField.setPrev(this.nameTextField);
      this.yCoordTextField.setNext(this.zCoordTextField);
      this.yCoordTextField.setPrev(this.xCoordTextField);
      this.zCoordTextField.setNext(this.nameTextField);
      this.zCoordTextField.setPrev(this.yCoordTextField);
      this.rgb = new GuiScrollbar[3];

      for(int i = 0; i < 3; ++i) {
         GuiScrollbar gs = new GuiScrollbar(0, 0, 0, 118, 10);
         gs.setMinimum(0.0F);
         gs.setMaximum(255.0F);
         gs.setVisibleAmount(0.0F);
         gs.setBlockIncrement(10.0F);
         gs.orientation = 1;
         this.rgb[i] = gs;
      }

      this.rgb[0].setValue((float)(waypoint == null ? Math.random() : (double)waypoint.red) * 255.0F);
      this.rgb[1].setValue((float)(waypoint == null ? Math.random() : (double)waypoint.green) * 255.0F);
      this.rgb[2].setValue((float)(waypoint == null ? Math.random() : (double)waypoint.blue) * 255.0F);
   }

   public GuiWaypointEditorScreen(GuiWaypointScreen parrent, Waypoint waypoint) {
      this(parrent.getMinecraft(), waypoint);
      this.parrent = parrent;
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);

      for(int i = 0; i < 3; ++i) {
         this.rgb[i].xPosition = super.width - 150 >> 1;
         this.rgb[i].yPosition = super.height / 2 + 20 + i * 10;
         super.controlList.add(this.rgb[i]);
      }

      this.nameTextField.setBounds(super.width - 150 >> 1, super.height / 2 - 40, 150, 9);
      this.xCoordTextField.setBounds(super.width - 150 >> 1, super.height / 2 - 20, 150, 9);
      this.yCoordTextField.setBounds(super.width - 150 >> 1, super.height / 2 - 10, 150, 9);
      this.zCoordTextField.setBounds(super.width - 150 >> 1, super.height / 2, 150, 9);
      super.controlList.add(this.nameTextField);
      super.controlList.add(this.xCoordTextField);
      super.controlList.add(this.yCoordTextField);
      super.controlList.add(this.zCoordTextField);
      this.okButton = new GuiSimpleButton(0, super.width / 2 - 65, super.height / 2 + 58, 60, 14, "OK");
      this.cancelButton = new GuiSimpleButton(1, super.width / 2 + 5, super.height / 2 + 58, 60, 14, "Cancel");
      super.controlList.add(this.okButton);
      super.controlList.add(this.cancelButton);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      super.onGuiClosed();
   }

   public void drawScreen(int mx, int my, float f) {
      int x = MathHelper.floor_double(super.mc.thePlayer.posX);
      int y = MathHelper.floor_double(super.mc.thePlayer.posY);
      int z = MathHelper.floor_double(super.mc.thePlayer.posZ);
      this.xCoordTextField.setNorm(x);
      this.yCoordTextField.setNorm(y);
      this.zCoordTextField.setNorm(z);
      String title = "Waypoint Edit";
      int titleWidth = super.fontRenderer.getStringWidth(title);
      int titleLeft = super.width - titleWidth >> 1;
      int titleRight = super.width + titleWidth >> 1;
      drawRect(titleLeft - 2, super.height / 2 - 71, titleRight + 2, super.height / 2 - 57, -1610612736);
      this.drawCenteredString(super.fontRenderer, title, super.width / 2, super.height / 2 - 68, -1);
      String temp = Integer.toString(x).equals(this.xCoordTextField.displayString) ? "xCoord: (Current)" : "xCoord:";
      this.drawString(super.fontRenderer, temp, (super.width - 150) / 2 + 1, super.height / 2 - 19, -1);
      temp = Integer.toString(y).equals(this.yCoordTextField.displayString) ? "yCoord: (Current)" : "yCoord:";
      this.drawString(super.fontRenderer, temp, (super.width - 150) / 2 + 1, super.height / 2 - 9, -1);
      temp = Integer.toString(z).equals(this.zCoordTextField.displayString) ? "zCoord: (Current)" : "zCoord:";
      this.drawString(super.fontRenderer, temp, (super.width - 150) / 2 + 1, super.height / 2 + 1, -1);
      drawRect((super.width - 150) / 2 - 2, super.height / 2 - 50, (super.width + 150) / 2 + 2, super.height / 2 + 52, -1610612736);
      this.drawCenteredString(super.fontRenderer, "Waypoint Name", super.width >> 1, super.height / 2 - 49, -1);
      this.drawCenteredString(super.fontRenderer, "Coordinate", super.width >> 1, super.height / 2 - 29, -1);
      this.drawCenteredString(super.fontRenderer, "Color", super.width >> 1, super.height / 2 + 11, -1);
      if (this.waypoint != null) {
         this.waypoint.red = this.rgb[0].getValue() / 255.0F;
         this.waypoint.green = this.rgb[1].getValue() / 255.0F;
         this.waypoint.blue = this.rgb[2].getValue() / 255.0F;
      }

      int r = (int)this.rgb[0].getValue() & 255;
      int g = (int)this.rgb[1].getValue() & 255;
      int b = (int)this.rgb[2].getValue() & 255;
      int color = -16777216 | r << 16 | g << 8 | b;
      this.drawCenteredString(super.fontRenderer, String.format("R:%03d", r), super.width / 2 - 15, super.height / 2 + 21, -2139062144);
      this.drawCenteredString(super.fontRenderer, String.format("G:%03d", g), super.width / 2 - 15, super.height / 2 + 31, -2139062144);
      this.drawCenteredString(super.fontRenderer, String.format("B:%03d", b), super.width / 2 - 15, super.height / 2 + 41, -2139062144);
      drawRect(super.width + 90 >> 1, super.height / 2 + 20, super.width + 150 >> 1, super.height / 2 + 50, color);
      super.drawScreen(mx, my, f);
   }

   protected void keyTyped(char c, int i) {
      if (i == 1) {
         this.cancel();
      } else if (i == 28 && GuiTextField.getActive() == this.zCoordTextField) {
         this.zCoordTextField.norm();
         this.accept();
      } else {
         GuiTextField.keyType(super.mc, c, i);
      }
   }

   private void cancel() {
      if (this.waypoint != null) {
         this.waypoint.set(this.waypointBackup);
      }

      super.mc.displayGuiScreen(this.parrent);
   }

   private void accept() {
      if (this.waypoint != null) {
         this.waypoint.name = this.nameTextField.displayString;
         this.waypoint.x = parseInt(this.xCoordTextField.displayString);
         this.waypoint.y = parseInt(this.yCoordTextField.displayString);
         this.waypoint.z = parseInt(this.zCoordTextField.displayString);
         this.waypoint.red = this.rgb[0].getValue() / 255.0F;
         this.waypoint.green = this.rgb[1].getValue() / 255.0F;
         this.waypoint.blue = this.rgb[2].getValue() / 255.0F;
         this.parrent.updateWaypoint(this.waypoint);
      } else {
         String name = this.nameTextField.displayString;
         int x = parseInt(this.xCoordTextField.displayString);
         int y = parseInt(this.yCoordTextField.displayString);
         int z = parseInt(this.zCoordTextField.displayString);
         float r = this.rgb[0].getValue() / 255.0F;
         float g = this.rgb[1].getValue() / 255.0F;
         float b = this.rgb[2].getValue() / 255.0F;
         this.waypoint = new Waypoint(name, x, y, z, true, r, g, b);
         if (this.parrent == null) {
            ReiMinimap rmm = ReiMinimap.instance;
            List wayPts = rmm.getWaypoints();
            wayPts.add(this.waypoint);
            rmm.saveWaypoints();
         } else {
            this.parrent.addWaypoint(this.waypoint);
         }
      }

      super.mc.displayGuiScreen(this.parrent);
   }

   private static int parseInt(String s) {
      try {
         return Integer.parseInt(s);
      } catch (Exception var2) {
         return 0;
      }
   }

   protected void actionPerformed(GuiButton guibutton) {
      if (guibutton == this.okButton) {
         this.accept();
      } else if (guibutton == this.cancelButton) {
         this.cancel();
      }
   }
}
