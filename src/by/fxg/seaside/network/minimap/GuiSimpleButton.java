package by.fxg.seaside.network.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiSimpleButton extends GuiButton {
   public GuiSimpleButton(int i, int j, int k, int l, int i1, String s) {
      super(i, j, k, l, i1, s);
   }

   public void drawButton(Minecraft minecraft, int i, int j) {
      if (super.drawButton) {
         FontRenderer fontrenderer = minecraft.fontRenderer;
         boolean flag = i >= super.xPosition && j >= super.yPosition && i < super.xPosition + super.width && j < super.yPosition + super.height;
         int color = flag && super.enabled ? -932813210 : -1610612736;
         drawRect(super.xPosition, super.yPosition, super.xPosition + super.width, super.yPosition + super.height, color);
         this.drawCenteredString(fontrenderer, super.displayString, super.xPosition + super.width / 2, super.yPosition + (super.height - 8) / 2, super.enabled ? -1 : -8355712);
      }
   }
}
