package by.fxg.seaside.amain;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import by.fxg.sampui.client.DrawHelper;
import by.fxg.seaside.managers.PlayerManager;
import by.fxg.seaside.storage.Coords;
import by.fxg.seaside.systems.houses.House;
import by.fxg.seaside.systems.markers.Marker;
import by.fxg.seaside.utils.ServerUtils;
import by.fxg.seaside.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

public class EventHandlerUI {
	public static EventHandlerUI instance;
	private Map<String, Marker> markers = new HashMap<String, Marker>();
	public House houseInfo = null;
	public Coords houseInfoPos = null;
	public int prevMoney = 0;
	public EventHandlerUI() {
		instance = this;
	}
	
	@ForgeSubscribe
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer && PlayerManager.get((EntityPlayer)event.entity) == null) PlayerManager.register((EntityPlayer) event.entity);
		if (event.entity instanceof EntityPlayer && event.entity.getExtendedProperties("pme") == null) event.entity.registerExtendedProperties("pme", new PlayerManager((EntityPlayer) event.entity));
	}
	
	@ForgeSubscribe 
	public void onPreRenderUI(Pre event) {
		if (event.type == ElementType.ARMOR || event.type == ElementType.HEALTH || event.type == ElementType.BOSSHEALTH || event.type == ElementType.FOOD || event.type == ElementType.EXPERIENCE) event.setCanceled(true);
		if (event.type == ElementType.CROSSHAIRS && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
			
		}
	}
	
	@ForgeSubscribe
	public void onRenderUI(Post event) {
		if (event.type == ElementType.CROSSHAIRS) {
			String str;
			ScaledResolution sr = event.resolution;
			FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
			PlayerManager pm = PlayerManager.get(Minecraft.getMinecraft().thePlayer);
			if (this.prevMoney > pm.money) {
				this.prevMoney -= 1111;
				if (this.prevMoney - 1111 < pm.money) this.prevMoney = pm.money;
			} else if (this.prevMoney < pm.money) {
				this.prevMoney += 1111;
				if (this.prevMoney + 1111 > pm.money) this.prevMoney = pm.money;
			}
			
			
			
			/*HUD*/{
				GL11.glPushMatrix();
				DrawHelper.drawRect(sr.getScaledWidth() / 2 - 91, sr.getScaledHeight() - 35, sr.getScaledWidth() / 2 - 2, sr.getScaledHeight() - 25, 0.0F, 0.0F, 0.0F, 0.75F);
				DrawHelper.drawRect(sr.getScaledWidth() / 2 + 2, sr.getScaledHeight() - 35, sr.getScaledWidth() / 2 + 91, sr.getScaledHeight() - 25, 0.0F, 0.0F, 0.0F, 0.75F);
				DrawHelper.drawRect(sr.getScaledWidth() / 2 - Utils.getPercentOf(90, Minecraft.getMinecraft().thePlayer.getHealth(), 20), sr.getScaledHeight() - 34, sr.getScaledWidth() / 2 - 3, sr.getScaledHeight() - 26, 1.0F, 0.0F, 0.0F, 0.75F);
				DrawHelper.drawRect(sr.getScaledWidth() / 2 + 3, sr.getScaledHeight() - 34, sr.getScaledWidth() / 2 + Utils.getPercentOf(90, Minecraft.getMinecraft().thePlayer.getTotalArmorValue(), 20), sr.getScaledHeight() - 26, 0.9F, 1.0F, 0.9F, 0.75F);
				GL11.glScalef(1.5F, 1.50F, 1.5F);
				fr.drawStringWithShadow(str = "§a" + this.prevMoney + "$", (int)((double)sr.getScaledWidth() / 2 / 1.5D - fr.getStringWidth(str) / 2 + 1), (int)((double)sr.getScaledHeight() / 1.5D - 32), 0);
				GL11.glPopMatrix();
			}
			
			/*HOUSE INFO*/{
				if (this.houseInfo != null && this.houseInfoPos != null) {
					if (this.houseInfoPos.compare(Coords.getFromEntity(Minecraft.getMinecraft().thePlayer))) {
						boolean p20 = this.houseInfo.isOnRent || this.houseInfo.owner.equals("none");
						int len = fr.getStringWidth("Владелец " + (this.houseInfo.owner.equals("none") ? "отсутствует" : this.houseInfo.owner));
						len = len < fr.getStringWidth("Для покупки нажмите " + KeyLoader.keyInteract2.getKeyButton()) + 2 && p20 ? fr.getStringWidth("Для покупки нажмите " + KeyLoader.keyInteract2.getKeyButton()) : len;
						DrawHelper.drawRect(sr.getScaledWidth() - 1, sr.getScaledHeight() / 2 + 24 + (p20 ? 20 : 0), sr.getScaledWidth() - (len + 3 < 125 ? 125 : len + 3), sr.getScaledHeight() / 2 - 25 - (p20 ? 20 : 0), 0.0F, 0.0F, 0.0F, 0.5F);
						fr.drawStringWithShadow(str = "Дом " + this.houseInfo.ID, sr.getScaledWidth() - fr.getStringWidth(str) - 2, sr.getScaledHeight() / 2 - 24 - (p20 ? 20 : 0), Color.white.getRGB());
						fr.drawStringWithShadow(str = "Класс " + this.houseInfo.classID, sr.getScaledWidth() - fr.getStringWidth(str) - 2, sr.getScaledHeight() / 2 - 14 - (p20 ? 20 : 0), Color.white.getRGB());
						fr.drawStringWithShadow(str = "Интерьер " + this.houseInfo.interiorID, sr.getScaledWidth() - fr.getStringWidth(str) - 2, sr.getScaledHeight() / 2 - 4 - (p20 ? 20 : 0), Color.white.getRGB());
						fr.drawStringWithShadow(str = "Владелец " + (this.houseInfo.owner.equals("none") ? "отсутствует" : this.houseInfo.owner), sr.getScaledWidth() - fr.getStringWidth(str) - 2, sr.getScaledHeight() / 2 + 5 - (p20 ? 20 : 0), Color.white.getRGB());
						fr.drawStringWithShadow(str = "Дом " + (this.houseInfo.isOpen ? "§aоткрыт" : "§cзакрыт"), sr.getScaledWidth() - fr.getStringWidth(str) - 2, sr.getScaledHeight() / 2 + 14 - (p20 ? 20 : 0), Color.white.getRGB());
						if (this.houseInfo.isOnRent) {
							fr.drawStringWithShadow(str = "Доступен к аренде", sr.getScaledWidth() - fr.getStringWidth(str) - 2, sr.getScaledHeight() / 2 + 15, Color.white.getRGB());
							fr.drawStringWithShadow(str = "Цена за час: §a" + this.houseInfo.rentPrice + "$", sr.getScaledWidth() - fr.getStringWidth(str) - 2, sr.getScaledHeight() / 2 + 25, Color.white.getRGB());
							fr.drawStringWithShadow(str = "Для аренды нажмите " + KeyLoader.keyInteract2.getKeyButton(), sr.getScaledWidth() - fr.getStringWidth(str) - 2, sr.getScaledHeight() / 2 + 35, Color.white.getRGB());
						} else if (this.houseInfo.owner.equals("none")) {
							fr.drawStringWithShadow(str = "Доступен к продаже", sr.getScaledWidth() - fr.getStringWidth(str) - 2, sr.getScaledHeight() / 2 + 15, Color.white.getRGB());
							fr.drawStringWithShadow(str = "Цена: §a" + this.houseInfo.govPrice + "$", sr.getScaledWidth() - fr.getStringWidth(str) - 2, sr.getScaledHeight() / 2 + 25, Color.white.getRGB());
							fr.drawStringWithShadow(str = "Для покупки нажмите " + KeyLoader.keyInteract2.getKeyButton(), sr.getScaledWidth() - fr.getStringWidth(str) - 2, sr.getScaledHeight() / 2 + 35, Color.white.getRGB());
						}
					}
				}
			}
			
			/*MINIMAP*/{
				GL11.glPushMatrix();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				
				GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
				GL11.glScissor(20, 20, 256, 256);//x2
				Coords c = Coords.getFromEntity(Minecraft.getMinecraft().thePlayer);
				c.x += 348;
				c.z += 39;
				DrawHelper.drawImage(10 + -c.x, sr.getScaledHeight() - 138 + -c.z, "/mods/seaside/minimap/map.png", 557, 730);
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				GL11.glPopMatrix();
				
				//GL11.glAlphaFunc(515, 0.1F);
				GL11.glEnable(GL11.GL_STENCIL_TEST);
				GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
				GL11.glStencilFunc(519, 1, -1);//519,1,-1 было, стало 512,7680,7680
				GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_REPLACE, GL11.GL_REPLACE);//7680,7681,7681 ->
				DrawHelper.drawImage(10, sr.getScaledHeight() - 138, "/mods/seaside/minimap/roundmap_mask.png", 128, 128);
				GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
				GL11.glStencilFunc(514, 1, -1);
				GL11.glDisable(GL11.GL_STENCIL_TEST);
				//GL11.glAlphaFunc(516, 0.0F);
				
				
				//GL11.glBlendFunc(770, 771);
				DrawHelper.drawImage(10, sr.getScaledHeight() - 138, "/mods/seaside/minimap/roundmap.png", 128, 128);
				GL11.glTranslated(74, sr.getScaledHeight() - 74, 0);
				GL11.glRotatef(180.0F + Minecraft.getMinecraft().thePlayer.rotationYawHead % 360.0F, 0.0F, 0.0F, 1.0F);
				DrawHelper.drawImage(-8, -8, "/mods/seaside/minimap/entity2.png", 16, 16);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glPopMatrix();
			}
		}
	}
	
	@ForgeSubscribe
	public void renderWorldLast(RenderWorldLastEvent event) {
		for (Marker marker : this.markers.values()) {
			marker.render();
		}
	}
	
	public void addSetMarker(Marker marker) {
		if (this.markers.containsKey(marker.name)) {
			this.markers.replace(marker.name, marker);
		} else {
			this.markers.put(marker.name, marker);
		}
	}
	
	public void removeMarker(String marker) {
		if (this.markers.containsKey(marker)) {
			this.markers.remove(marker);
		}
	}
}
