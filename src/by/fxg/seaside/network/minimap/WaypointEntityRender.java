package by.fxg.seaside.network.minimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;

public class WaypointEntityRender extends Render {
	static final ReiMinimap rm;
	final Minecraft mc;
	double far = 1.0D;
	double _d = 1.0D;
	static final boolean optifine;
	static int ofRenderDistanceFine;

	public WaypointEntityRender(Minecraft mc) {
		this.mc = mc;
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.far = (double) (512 >> this.mc.gameSettings.renderDistance) * 0.8D;
		this._d = 1.0D / (double) (256 >> this.mc.gameSettings.renderDistance);

		double dscale = rm.getVisibleDimensionScale();
		ArrayList list = new ArrayList();
		if (rm.getMarker()) {
			Iterator i$ = rm.getWaypoints().iterator();

			while (i$.hasNext()) {
				Waypoint w = (Waypoint) i$.next();
				if (w.enable) {
					list.add(new WaypointEntityRender.ViewWaypoint(w, dscale));
				}
			}

			if (!list.isEmpty()) {
				Collections.sort(list);
				this.mc.entityRenderer.disableLightmap(0.0D);
				GL11.glDisable(2896);
				GL11.glDisable(2912);
				i$ = list.iterator();

				while (i$.hasNext()) {
					WaypointEntityRender.ViewWaypoint w = (WaypointEntityRender.ViewWaypoint) i$.next();
					this.draw(w, f, f1);
				}

				GL11.glEnable(2912);
				GL11.glEnable(2896);
				this.mc.entityRenderer.enableLightmap(0.0D);
				super.shadowSize = 0.0F;
			}
		}
	}

	void draw(WaypointEntityRender.ViewWaypoint w, float f, float f1) {
		float alpha = (float) Math.max(0.0D, 1.0D - w.distance * this._d);
		FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
		GL11.glPushMatrix();
		StringBuilder sb = new StringBuilder();
		if (rm.getMarkerLabel() && w.name != null) {
			sb.append(w.name);
		}

		if (rm.getMarkerDistance()) {
			if (sb.length() != 0) {
				sb.append(" ");
			}

			sb.append(String.format("[%1.2fm]", w.distance));
		}

		String str = sb.toString();
		double scale = (w.dl * 0.1D + 1.0D) * 0.02666666666666667D;
		int slideY = rm.getMarkerIcon() ? -16 : 0;
		GL11.glTranslated(w.dx, w.dy, w.dz);
		GL11.glRotatef(-super.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(this.mc.gameSettings.thirdPersonView == 2 ? -super.renderManager.playerViewX : super.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScaled(-scale, -scale, scale);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		Tessellator tessellator = Tessellator.instance;
		if (rm.getMarkerIcon()) {
			GL11.glEnable(3553);
			GL11.glDisable(2929);
			GL11.glDepthMask(false);
			Waypoint.FILE[w.type].bind();
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_F(w.red, w.green, w.blue, 0.4F);
			tessellator.addVertexWithUV(-8.0D, -8.0D, 0.0D, 0.0D, 0.0D);
			tessellator.addVertexWithUV(-8.0D, 8.0D, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(8.0D, 8.0D, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(8.0D, -8.0D, 0.0D, 1.0D, 0.0D);
			tessellator.draw();
			GL11.glEnable(2929);
			GL11.glDepthMask(true);
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_F(w.red, w.green, w.blue, alpha);
			tessellator.addVertexWithUV(-8.0D, -8.0D, 0.0D, 0.0D, 0.0D);
			tessellator.addVertexWithUV(-8.0D, 8.0D, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(8.0D, 8.0D, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(8.0D, -8.0D, 0.0D, 1.0D, 0.0D);
			tessellator.draw();
		}

		int j = fontrenderer.getStringWidth(str) >> 1;
		if (j != 0) {
			GL11.glDisable(3553);
			GL11.glDisable(2929);
			GL11.glDepthMask(false);
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.6275F);
			tessellator.addVertex((double) (-j - 1), (double) (slideY - 1), 0.0D);
			tessellator.addVertex((double) (-j - 1), (double) (slideY + 8), 0.0D);
			tessellator.addVertex((double) (j + 1), (double) (slideY + 8), 0.0D);
			tessellator.addVertex((double) (j + 1), (double) (slideY - 1), 0.0D);
			tessellator.draw();
			GL11.glEnable(3553);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			fontrenderer.drawString(str, -j, slideY, w.type == 0 ? 1627389951 : 1627324416);
			GL11.glEnable(2929);
			GL11.glDepthMask(true);
			int a = (int) (255.0F * alpha);
			if (a != 0) {
				fontrenderer.drawString(str, -j, slideY, (w.type == 0 ? 16777215 : 16711680) | a << 24);
			}
		}

		GL11.glDisable(3042);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(3553);
		GL11.glPopMatrix();
	}

	// $FF: synthetic method
	static RenderManager access$000(WaypointEntityRender x0) {
		return x0.renderManager;
	}

	// $FF: synthetic method
	static RenderManager access$100(WaypointEntityRender x0) {
		return x0.renderManager;
	}

	// $FF: synthetic method
	static RenderManager access$200(WaypointEntityRender x0) {
		return x0.renderManager;
	}

	static {
		rm = ReiMinimap.instance;
		boolean b = false;

		try {
			GameSettings.class.getField("ofRenderDistanceFine");
			Class.forName("GuiPerformanceSettingsOF");
			b = true;
		} catch (Exception var2) {
			;
		}

		optifine = b;
	}

	private class ViewWaypoint extends Waypoint implements Comparable<ViewWaypoint> {
		double dx;
		double dy;
		double dz;
		double dl;
		double distance;

		ViewWaypoint(Waypoint w, double dscale) {
			super(w);
			double var10001 = (double) w.x * dscale;
			WaypointEntityRender.access$000(WaypointEntityRender.this);
			this.dx = var10001 - RenderManager.renderPosX + 0.5D;
			var10001 = (double) w.y;
			WaypointEntityRender.access$100(WaypointEntityRender.this);
			this.dy = var10001 - RenderManager.renderPosY + 0.5D;
			var10001 = (double) w.z * dscale;
			WaypointEntityRender.access$200(WaypointEntityRender.this);
			this.dz = var10001 - RenderManager.renderPosZ + 0.5D;
			this.dl = this.distance = Math.sqrt(this.dx * this.dx + this.dy * this.dy + this.dz * this.dz);
			if (this.dl > WaypointEntityRender.this.far) {
				double d = WaypointEntityRender.this.far / this.dl;
				this.dx *= d;
				this.dy *= d;
				this.dz *= d;
				this.dl = WaypointEntityRender.this.far;
			}

		}

		public int compareTo(WaypointEntityRender.ViewWaypoint o) {
			return o.distance < this.distance ? -1 : (o.distance > this.distance ? 1 : 0);
		}
	}
}
