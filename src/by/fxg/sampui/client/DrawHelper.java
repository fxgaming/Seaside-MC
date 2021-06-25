package by.fxg.sampui.client;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

public class DrawHelper {
   public static int gameColor = 15987699;
   public static int crosshairColor = 16777215;
   public static float particleTick;
   private static Minecraft mc = Minecraft.getMinecraft();
   private static RenderPlayer playerRenderer;
   private static float zLevel = 0.0F;

   public static void renderColor(int par1) {
      Color color = Color.decode("" + par1);
      float red = (float)color.getRed() / 255.0F;
      float green = (float)color.getGreen() / 255.0F;
      float blue = (float)color.getBlue() / 255.0F;
      GL11.glColor3f(red, green, blue);
   }

   public static void renderColor() {
      Color color = Color.decode("" + gameColor);
      float red = (float)color.getRed() / 255.0F;
      float green = (float)color.getGreen() / 255.0F;
      float blue = (float)color.getBlue() / 255.0F;
      GL11.glColor3f(red, green, blue);
   }

   public static void renderCrosshairColor() {
      Color color = Color.decode("" + crosshairColor);
      float red = (float)color.getRed() / 255.0F;
      float green = (float)color.getGreen() / 255.0F;
      float blue = (float)color.getBlue() / 255.0F;
      GL11.glColor4f(red, green, blue, 0.9F);
   }

   public static void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6) {
      float f = 0.00390625F;
      float f1 = 0.00390625F;
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
      tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
      tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
      tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
      tessellator.draw();
   }

   public static void renderItemStackIntoGUI(ItemStack itemstack, int posx, int posy) {
      GL11.glPushMatrix();
      RenderItem itemRenderer = new RenderItem();
      itemRenderer.field_77024_a = true;
      itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, posx, posy);
      itemRenderer.field_77024_a = false;
      GL11.glDisable(2896);
      GL11.glPopMatrix();
   }

   public static void renderScaledItemStackIcon(ItemStack par1, double x, double y, double scale) {
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, 0.0D);
      GL11.glScaled(scale, scale, scale);
      renderItemStackIntoGUI(par1, 0, 0);
      GL11.glPopMatrix();
   }

   public static void drawRectWithShadow(int par0, int par1, int par2, int par3, String par4Hex, float par5Alpha) {
      drawRect(par0 - 1, par1 - 1, par2 + 2, par3 + 2, "0x000000", 0.2F);
      drawRect(par0, par1, par2, par3, par4Hex, par5Alpha);
   }

   public static void drawRect(int par0, int par1, int par2, int par3, String par4Hex, float par5Alpha) {
      Color color = Color.decode(par4Hex);
      float red = (float)color.getRed() / 255.0F;
      float green = (float)color.getGreen() / 255.0F;
      float blue = (float)color.getBlue() / 255.0F;
      drawRect(par0, par1, par0 + par2, par1 + par3, red, green, blue, par5Alpha);
   }

   public static void drawRect(int par0, int par1, int par2, int par3, float par4Red, float par5Green, float par6Blue, float par7Alpha) {
      Tessellator var9 = Tessellator.instance;
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glColor4f(par4Red, par5Green, par6Blue, par7Alpha);
      var9.startDrawingQuads();
      var9.addVertex((double)par0, (double)par3, 0.0D);
      var9.addVertex((double)par2, (double)par3, 0.0D);
      var9.addVertex((double)par2, (double)par1, 0.0D);
      var9.addVertex((double)par0, (double)par1, 0.0D);
      var9.draw();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   public static void drawTextureRect(int x, int y, int width, int height) {
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int uw = 176;
      int uh = 146;
      drawTexturedModalRect(x, y, 0, 0, width / 2, height / 2);
      drawTexturedModalRect(x + width / 2, y, uw - width / 2, 0, width / 2, height / 2);
      drawTexturedModalRect(x, y + height / 2, 0, uh - height / 2, width / 2, height / 2);
      drawTexturedModalRect(x + width / 2, y + height / 2, uw - width / 2, uh - height / 2, width / 2, height / 2);
      GL11.glPopMatrix();
   }

   public static int resWidth() {
      Minecraft mc = Minecraft.getMinecraft();
      ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
      return scaledresolution.getScaledWidth();
   }

   public static int resHeight() {
      Minecraft mc = Minecraft.getMinecraft();
      ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
      return scaledresolution.getScaledHeight();
   }

   public static void renderTextScaled(String text, int posX, int posY, double par4) {
      GL11.glPushMatrix();
      GL11.glTranslated((double)posX, (double)posY, 0.0D);
      GL11.glScaled(par4, par4, par4);
      renderText(text, 0, 0);
      GL11.glPopMatrix();
   }

   public static void renderText(String text, int posX, int posY) {
      Minecraft mc = Minecraft.getMinecraft();
      mc.fontRenderer.drawString(text, posX, posY, gameColor);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void renderCenteredText(String text, int posX, int posY) {
      Minecraft mc = Minecraft.getMinecraft();
      mc.fontRenderer.drawString(text, posX - mc.fontRenderer.getStringWidth(text) / 2, posY, gameColor);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static void renderTextScaled(String text, int posX, int posY, double par4, int par5) {
      GL11.glPushMatrix();
      GL11.glTranslated((double)posX, (double)posY, 0.0D);
      GL11.glScaled(par4, par4, par4);
      renderText(text, 0, 0, par5);
      GL11.glPopMatrix();
   }

   public static void renderText(String text, int posX, int posY, int par4) {
      Minecraft mc = Minecraft.getMinecraft();
      mc.fontRenderer.drawString(text, posX, posY, par4);
   }

   public static void renderCenteredText(String text, int posX, int posY, int par4) {
      Minecraft mc = Minecraft.getMinecraft();
      mc.fontRenderer.drawString(text, posX - mc.fontRenderer.getStringWidth(text) / 2, posY, par4);
   }

   public static void drawImage(double par0, double par1, String image, double par2Width, double par3Height) {
      Minecraft.getMinecraft().renderEngine.bindTexture(image);
      Tessellator tess = Tessellator.instance;
      tess.startDrawingQuads();
      tess.addVertexWithUV(par0, par1 + par3Height, 0.0D, 0.0D, 1.0D);
      tess.addVertexWithUV(par0 + par2Width, par1 + par3Height, 0.0D, 1.0D, 1.0D);
      tess.addVertexWithUV(par0 + par2Width, par1, 0.0D, 1.0D, 0.0D);
      tess.addVertexWithUV(par0, par1, 0.0D, 0.0D, 0.0D);
      tess.draw();
   }

//   @Deprecated
//   public static void renderPlayerHead(String par1, int par2, int par3) {
//      if (par1.length() > 0) {
//         ResourceLocation resourcelocation = AbstractClientPlayer.locationStevePng;
//         GL11.glPushMatrix();
//         drawRect(par2 - 1, par3 - 1, 20, 21, "0x000000", 0.5F);
//         Minecraft.getMinecraft().renderEngine.bindTexture(resourcelocation);
//         GL11.glTranslated((double)par2, (double)par3, 0.0D);
//         GL11.glScaled(0.75D, 0.39D, 0.0D);
//         double scale = 0.75D;
//         GL11.glScaled(scale, scale, scale);
//         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//         Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 32, 64, 32, 64);
//         Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 160, 64, 32, 64);
//         GL11.glPopMatrix();
//      }
//   }

   protected void drawOutlinedBox(int x, int y, int z) {
      drawOutlinedBox((double)x, (double)y, (double)z, 255.0F, 255.0F, 255.0F, 1.0D);
   }

   protected void drawOutlinedBoxUpdated(int x, int y, int z, boolean par4) {
      if (par4) {
         drawOutlinedBox((double)x, (double)y, (double)z, 255.0F, 255.0F, 255.0F, 1.0D);
      } else {
         this.drawOutlinedBoxRed(x, y, z);
      }

   }

   protected void drawOutlinedBoxRed(int x, int y, int z) {
      drawOutlinedBox((double)x, (double)y, (double)z, 255.0F, 0.0F, 0.0F, 1.0D);
   }

   protected void drawOutlinedBoxGreen(int x, int y, int z) {
      drawOutlinedBox((double)x, (double)y, (double)z, 0.0F, 255.0F, 0.0F, 1.0D);
   }

   public static void drawOutlinedBox(double x, double y, double z, float f1, float f2, float f3, double boxsize) {
      EntityPlayer player = Minecraft.getMinecraft().thePlayer;
      double posX = x + boxsize / 2.0D;
      double posY = y + boxsize / 2.0D;
      double posZ = z + boxsize / 2.0D;
      double min = -(boxsize / 2.0D);
      double max = boxsize / 2.0D;
      AxisAlignedBB box = AxisAlignedBB.getBoundingBox(posX + min, posY + min, posZ + min, posX + max, posY + max, posZ + max);
      GL11.glPushMatrix();
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(f1, f2, f3, 0.4F);
      GL11.glLineWidth(5.0F);
      GL11.glDisable(3553);
      GL11.glDepthMask(false);
      double par4 = 0.98D;
      double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)particleTick;
      double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)particleTick;
      double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)particleTick;
      drawOutlinedBoundingBox(box.getOffsetBoundingBox(-d0, -d1, -d2));
      GL11.glDepthMask(true);
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glPopMatrix();
   }

   protected static void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB) {
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawing(3);
      tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
      tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
      tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
      tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
      tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
      tessellator.draw();
      tessellator.startDrawing(3);
      tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
      tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
      tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
      tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
      tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
      tessellator.draw();
      tessellator.startDrawing(1);
      tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
      tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
      tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
      tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
      tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
      tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
      tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
      tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
      tessellator.draw();
   }

   public static void renderPlayerModel(EntityPlayer par1, int par2, int par3, float par4) {
      renderPlayerModel(par1, par2, par3, par4, 0.0F);
   }

   public static void renderPlayerModel(EntityPlayer par1, int par2, int par3, float par4, float par5) {
      RenderManager.instance.livingPlayer = par1;
      RenderManager.instance.renderEngine = mc.renderEngine;
      if (playerRenderer == null) {
         playerRenderer = new RenderPlayer();
         playerRenderer.setRenderManager(RenderManager.instance);
      } else {
         GL11.glEnable(2903);
         GL11.glPushMatrix();
         GL11.glTranslatef((float)par2, (float)par3, 50.0F);
         GL11.glScalef(-par4, par4, par4);
         GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         GL11.glEnable(2884);
         GL11.glEnable(3008);
         GL11.glEnable(2929);
         float f2 = par1.renderYawOffset;
         float f3 = par1.rotationYaw;
         float f4 = par1.rotationPitch;
         GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
         RenderHelper.enableStandardItemLighting();
         GL11.glRotatef(-110.0F + par5, 0.0F, 1.0F, 0.0F);
         par1.renderYawOffset = 0.0F;
         par1.rotationYaw = 0.0F;
         par1.rotationPitch = 0.0F;
         par1.rotationYawHead = par1.renderYawOffset;
         GL11.glTranslatef(0.0F, par1.yOffset, 0.0F);
         RenderManager.instance.playerViewY = 180.0F;
         playerRenderer.doRender(par1, 0.0D, 0.0D, 0.0D, 0.0F, 0.625F);
         par1.renderYawOffset = f2;
         par1.rotationYaw = f3;
         par1.rotationPitch = f4;
         GL11.glPopMatrix();
         RenderHelper.disableStandardItemLighting();
         GL11.glDisable(32826);
         OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GL11.glDisable(3553);
         OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
      }
   }
}
