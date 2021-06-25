package by.fxg.seaside.systems.pickup;

import org.lwjgl.opengl.GL11;

import by.fxg.seaside.ClientProxy;
import by.fxg.seaside.amain.ThreadTimer;
import by.fxg.seaside.managers.ModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class RenderPickup extends TileEntitySpecialRenderer implements IItemRenderer {
	private WavefrontObject[] pickups = {
			
	};
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
		if (var1 instanceof TilePickup) {
			if (((TilePickup)var1).renderType < ModelManager.instance.model_pickup.length) {
				GL11.glPushMatrix();
				//GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glTranslatef((float)var2 + 0.5F, (float)var4 + 0.75F, (float)var6 + 0.5F);
				GL11.glScalef(2.5F, 2.5F, 2.5F);
				GL11.glRotatef((float)ThreadTimer.instance.rotation360deg, 0.0F, 1.0F, 0.0F);
				Minecraft.getMinecraft().renderEngine.bindTexture("/mods/seaside/textures/models/pickup_" + ((TilePickup)var1).renderType + ".png");
				ModelManager.instance.model_pickup[((TilePickup)var1).renderType].renderAll();
				//GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glPopMatrix();
			}
		}
	}
	
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == type.EQUIPPED || type == type.ENTITY || type == type.INVENTORY;
	}

	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return type != type.EQUIPPED;
	}
	
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		switch (type) {
			case EQUIPPED:
				GL11.glTranslatef(0.5F, 0.25F, 0.0F);
				GL11.glScalef(3.0F, 3.0F, 3.0F);
				break;
			case ENTITY:
				GL11.glScalef(3.0F, 3.0F, 3.0F);
				break;
			case INVENTORY:
				GL11.glScalef(5.0F, 5.0F, 5.0F);
				break;
		}
		Minecraft.getMinecraft().renderEngine.bindTexture("/mods/seaside/textures/models/pickup_0.png");
		ModelManager.instance.model_pickup[0].renderAll();
		GL11.glPopMatrix();
	}
}
