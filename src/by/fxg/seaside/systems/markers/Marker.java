package by.fxg.seaside.systems.markers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import by.fxg.seaside.ClientProxy;
import by.fxg.seaside.amain.ThreadTimer;
import by.fxg.seaside.storage.Coords;
import net.minecraft.client.renderer.entity.RenderManager;

public class Marker {
	private static final Cylinder cyl = new Cylinder();
	public String name;
	public MarkerColor color;
	public Coords pos;
	
	public Marker(String name, MarkerColor color, Coords pos) {
		this.name = name;
		this.color = color;
		this.pos = pos;
	}
	
	public void render() {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		
		GL11.glTranslated((double)pos.x - RenderManager.renderPosX + 0.5D, (double)pos.y - RenderManager.renderPosY + 1.0D, (double)pos.z - RenderManager.renderPosZ + 0.5D);
		switch (this.color) {
			case BLUE: GL11.glColor4f(0.25F, 0.25F, 1.0F, 0.75F); break;
			case GREEN: GL11.glColor4f(0.25F, 1.0F, 0.25F, 0.75F); break;
			case YELLOW: GL11.glColor4f(1.0F, 1.0F, 0.25F, 0.75F); break;
			case DARKAQUA: GL11.glColor4f(0.25F, 0.55F, 0.75F, 0.75F); break;
			default: GL11.glColor4f(1.0F, 0.25F, 0.25F, 0.75F); break;
		}
		GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef((float)ThreadTimer.instance.rotation360deg, 0.0F, 0.0F, 1.0F);
		cyl.setDrawStyle(100013);
		cyl.setNormals(100013);
		cyl.draw(0.5F, 0.5F, 0.99F, 16, 0);
	
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}
}
