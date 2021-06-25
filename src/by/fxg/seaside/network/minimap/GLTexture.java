package by.fxg.seaside.network.minimap;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

public class GLTexture {
	private static String DEFAULT_PACK = "/reifnsk/minimap/";
	private static String pack;
	private static ArrayList list;
	private static GLTexture missing;
	static final GLTexture TEMPERATURE;
	static final GLTexture HUMIDITY;
	static final GLTexture ROUND_MAP;
	static final GLTexture ROUND_MAP_MASK;
	static final GLTexture SQUARE_MAP;
	static final GLTexture SQUARE_MAP_MASK;
	static final GLTexture ENTITY;
	static final GLTexture ENTITY2;
	static final GLTexture LIGHTNING;
	static final GLTexture N;
	static final GLTexture E;
	static final GLTexture W;
	static final GLTexture S;
	static final GLTexture MMARROW;
	static final GLTexture WAYPOINT1;
	static final GLTexture WAYPOINT2;
	static final GLTexture MARKER1;
	static final GLTexture MARKER2;
	private final String fileName;
	private final boolean blur;
	private final boolean clamp;
	private int textureId;

	static void setPack(String newPack) {
		if (!newPack.equals(pack)) {
			Iterator i$ = list.iterator();

			while (i$.hasNext()) {
				GLTexture glt = (GLTexture) i$.next();
				glt.release();
			}

			pack = newPack;
		}
	}

	private GLTexture(String name, boolean blur, boolean clamp) {
		this.fileName = name;
		this.blur = blur;
		this.clamp = clamp;
		list.add(this);
	}

	int[] getData() {
		BufferedImage image = read(this.fileName);
		int w = image.getWidth();
		int h = image.getHeight();
		int[] rgbArray = new int[w * h];
		image.getRGB(0, 0, w, h, rgbArray, 0, w);
		return rgbArray;
	}

	void bind() {
		if (this.textureId == 0) {
			BufferedImage image = read(this.fileName);
			if (image == null) {
				this.textureId = this == missing ? -2 : -1;
			} else {
				this.textureId = GL11.glGenTextures();
				int w = image.getWidth();
				int h = image.getHeight();
				int[] rgbArray = new int[w * h];
				image.getRGB(0, 0, w, h, rgbArray, 0, w);
				GLTextureBufferedImage.createTexture(rgbArray, w, h, this.textureId, this.blur, this.clamp);
			}
		}

		if (this.textureId == -2) {
			GL11.glBindTexture(3553, 0);
		} else {
			if (this.textureId == -1) {
				missing.bind();
			}

			GL11.glBindTexture(3553, this.textureId);
		}
	}

	void release() {
		if (this.textureId > 0) {
			GL11.glDeleteTextures(this.textureId);
		}

		this.textureId = 0;
	}

	private static BufferedImage read(String name) {
		BufferedImage image = readImage(pack + name);
		return image == null ? readImage(DEFAULT_PACK + name) : image;
	}

	private static BufferedImage readImage(String stream) {
		InputStream in = GLTexture.class.getResourceAsStream(stream);
		if (in == null) {
			return null;
		} else {
			Object var3;
			try {
				BufferedImage var2 = ImageIO.read(in);
				return var2;
			} catch (Exception var13) {
				var3 = null;
			} finally {
				try {
					in.close();
				} catch (Exception var12) {
					;
				}

			}

			return (BufferedImage) var3;
		}
	}

	static {
		pack = DEFAULT_PACK;
		list = new ArrayList();
		missing = new GLTexture("missing.png", true, false);
		TEMPERATURE = new GLTexture("temperature.png", true, true);
		HUMIDITY = new GLTexture("humidity.png", true, true);
		ROUND_MAP = new GLTexture("roundmap.png", true, true);
		ROUND_MAP_MASK = new GLTexture("roundmap_mask.png", false, true);
		SQUARE_MAP = new GLTexture("squaremap.png", true, true);
		SQUARE_MAP_MASK = new GLTexture("squaremap_mask.png", false, true);
		ENTITY = new GLTexture("entity.png", true, true);
		ENTITY2 = new GLTexture("entity2.png", true, true);
		LIGHTNING = new GLTexture("lightning.png", true, true);
		N = new GLTexture("n.png", true, true);
		E = new GLTexture("e.png", true, true);
		W = new GLTexture("w.png", true, true);
		S = new GLTexture("s.png", true, true);
		MMARROW = new GLTexture("mmarrow.png", true, true);
		WAYPOINT1 = new GLTexture("waypoint.png", true, true);
		WAYPOINT2 = new GLTexture("waypoint2.png", true, true);
		MARKER1 = new GLTexture("marker.png", true, true);
		MARKER2 = new GLTexture("marker2.png", true, true);
	}
}
