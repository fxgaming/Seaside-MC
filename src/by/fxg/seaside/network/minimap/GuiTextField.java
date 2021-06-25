package by.fxg.seaside.network.minimap;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiTextField extends GuiButton {
   private static GuiTextField active;
   private int inputType;
   private GuiTextField prev;
   private GuiTextField next;
   private int norm = 0;

   public GuiTextField(String s) {
      super(0, 0, 0, 0, 0, s);
   }

   public GuiTextField() {
      super(0, 0, 0, 0, 0, "");
   }

   public void drawButton(Minecraft mc, int mx, int my) {
      int color = active == this ? -2134851392 : -2141167520;
      drawRect(super.xPosition, super.yPosition, super.xPosition + super.width, super.yPosition + super.height, color);
      if (this.inputType == 0) {
         this.drawCenteredString(mc.fontRenderer, super.displayString, super.xPosition + super.width / 2, super.yPosition + 1, -1);
      } else {
         int w = mc.fontRenderer.getStringWidth(super.displayString);
         this.drawString(mc.fontRenderer, super.displayString, super.xPosition + super.width - w - 1, super.yPosition + 1, -1);
      }

   }

   public boolean mousePressed(Minecraft mc, int mx, int my) {
      if (mx >= super.xPosition && mx < super.xPosition + super.width && my >= super.yPosition && my < super.yPosition + super.height) {
         this.active();
      }

      return false;
   }

   public void active() {
      if (active != null) {
         active.norm();
      }

      active = this;
   }

   static void keyType(Minecraft mc, char c, int i) {
      if (active != null) {
         active.kt(mc, c, i);
      }

   }

   private void kt(Minecraft mc, char c, int i) {
      String newString;
      int temp;
      if (this.inputType == 0 && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) && i == 47) {
         newString = GuiScreen.getClipboardString();
         if (newString == null) {
            return;
         }

         temp = 0;

         for(int k = newString.length(); temp < k; ++temp) {
            char ch = newString.charAt(temp);
            if (ch != '\r' && ch != '\n') {
               if (ch == ':') {
                  ch = ';';
               }

               newString = super.displayString + ch;
               if (mc.fontRenderer.getStringWidth(newString) >= super.width - 2) {
                  break;
               }

               super.displayString = newString;
            }
         }
      }

      if (i != 14 && i != 211) {
         if (i == 15) {
            if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54)) {
               next();
            } else {
               prev();
            }
         }

         if (i == 28) {
            next();
         }

         if (this.checkInput(c)) {
            newString = super.displayString + c;
            if (mc.fontRenderer.getStringWidth(newString) < super.width - 2) {
               try {
                  if (this.inputType == 1) {
                     temp = Integer.parseInt(newString);
                     newString = temp < -32000000 ? "-32000000" : (temp >= 32000000 ? "31999999" : Integer.toString(temp));
                  }

                  if (this.inputType == 2) {
                     temp = Integer.parseInt(newString);
                     newString = temp < 0 ? "0" : (temp > ReiMinimap.instance.getWorldHeight() + 2 ? Integer.toString(ReiMinimap.instance.getWorldHeight() + 2) : Integer.toString(temp));
                  }
               } catch (NumberFormatException var9) {
                  ;
               }

               super.displayString = newString;
            }
         }

      } else {
         if (!super.displayString.isEmpty()) {
            super.displayString = super.displayString.substring(0, super.displayString.length() - 1);
         }

      }
   }

   boolean checkInput(char c) {
      switch(this.inputType) {
      case 0:
         return " !\"#$%&'()*+,-./0123456789;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~⌂ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»".indexOf(c) != -1;
      case 1:
         return (super.displayString.isEmpty() ? "-0123456789" : "0123456789").indexOf(c) != -1;
      case 2:
         return "0123456789".indexOf(c) != -1;
      default:
         return false;
      }
   }

   void norm() {
      String newString = super.displayString;

      try {
         int temp;
         if (this.inputType == 1) {
            temp = Integer.parseInt(newString);
            newString = temp < -32000000 ? "-32000000" : (temp >= 32000000 ? "31999999" : Integer.toString(temp));
         }

         if (this.inputType == 2) {
            temp = Integer.parseInt(newString);
            newString = temp < 0 ? "0" : (temp > ReiMinimap.instance.getWorldHeight() + 2 ? Integer.toString(ReiMinimap.instance.getWorldHeight() + 2) : Integer.toString(temp));
         }
      } catch (NumberFormatException var3) {
         newString = Integer.toString(this.norm);
      }

      super.displayString = newString;
   }

   void setInputType(int i) {
      this.inputType = i;
   }

   void setPosition(int x, int y) {
      super.xPosition = x;
      super.yPosition = y;
   }

   void setSize(int w, int h) {
      super.width = w;
      super.height = h;
   }

   void setBounds(int x, int y, int w, int h) {
      super.xPosition = x;
      super.yPosition = y;
      super.width = w;
      super.height = h;
   }

   void setNext(GuiTextField next) {
      this.next = next;
   }

   void setPrev(GuiTextField prev) {
      this.prev = prev;
   }

   static void next() {
      if (active != null) {
         active.norm();
         active = active.next;
      }

   }

   static void prev() {
      if (active != null) {
         active.norm();
         active = active.prev;
      }

   }

   static GuiTextField getActive() {
      return active;
   }

   void setNorm(int norm) {
      this.norm = norm;
   }
}
