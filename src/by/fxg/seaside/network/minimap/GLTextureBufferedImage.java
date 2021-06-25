package by.fxg.seaside.network.minimap;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GLAllocation;

public class GLTextureBufferedImage extends BufferedImage {
	private static final ByteBuffer buffer = GLAllocation.createDirectByteBuffer(262144);
	private static final HashMap registerImage = new HashMap();
	private static final Lock lock = new ReentrantLock();
	public byte[] data;
	private int register;
	private boolean magFiltering;
	private boolean minFiltering;
	private boolean clampTexture;

	private GLTextureBufferedImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied, Hashtable properties) {
		super(cm, raster, isRasterPremultiplied, properties);
		this.data = ((DataBufferByte) raster.getDataBuffer()).getData();
	}

	public static GLTextureBufferedImage create(int w, int h) {
		ColorSpace colorspace1 = ColorSpace.getInstance(1000);
		int[] bits = new int[] { 8, 8, 8, 8 };
		int[] bandOffsets = new int[] { 0, 1, 2, 3 };
		ColorModel colorModel = new ComponentColorModel(colorspace1, bits, true, false, 3, 0);
		WritableRaster raster = Raster.createInterleavedRaster(0, w, h, w * 4, 4, bandOffsets, (Point) null);
		return new GLTextureBufferedImage(colorModel, raster, false, (Hashtable) null);
	}

	public static GLTextureBufferedImage create(BufferedImage image) {
		GLTextureBufferedImage img = create(image.getWidth(), image.getHeight());
		Graphics g = img.getGraphics();
		g.drawImage(image, 0, 0, (ImageObserver) null);
		g.dispose();
		return img;
	}

	public int register() {
		lock.lock();

		int var2;
		try {
			int clamp;
			if (this.register == 0) {
				this.register = GL11.glGenTextures();
				GL11.glBindTexture(3553, this.register);
				GL11.glTexParameteri(3553, 10241, this.minFiltering ? 9729 : 9728);
				GL11.glTexParameteri(3553, 10240, this.magFiltering ? 9729 : 9728);
				clamp = this.clampTexture ? 10496 : 10497;
				GL11.glTexParameteri(3553, 10242, clamp);
				GL11.glTexParameteri(3553, 10243, clamp);
				buffer.clear();
				buffer.put(this.data);
				buffer.flip();
				GL11.glTexImage2D(3553, 0, 6408, this.getWidth(), this.getHeight(), 0, 6408, 5121, buffer);
				registerImage.put(this.register, this);
				var2 = this.register;
				return var2;
			}

			GL11.glBindTexture(3553, this.register);
			GL11.glTexParameteri(3553, 10241, this.minFiltering ? 9729 : 9728);
			GL11.glTexParameteri(3553, 10240, this.magFiltering ? 9729 : 9728);
			clamp = this.clampTexture ? 10496 : 10497;
			GL11.glTexParameteri(3553, 10242, clamp);
			GL11.glTexParameteri(3553, 10243, clamp);
			buffer.clear();
			buffer.put(this.data);
			buffer.flip();
			GL11.glTexSubImage2D(3553, 0, 0, 0, this.getWidth(), this.getHeight(), 6408, 5121, buffer);
			var2 = this.register;
		} finally {
			lock.unlock();
		}

		return var2;
	}

	public boolean bind() {
		lock.lock();

		boolean var1;
		try {
			if (this.register != 0) {
				GL11.glBindTexture(3553, this.register);
				var1 = true;
				return var1;
			}

			var1 = false;
		} finally {
			lock.unlock();
		}

		return var1;
	}

	public void unregister() {
		lock.lock();

		try {
			if (this.register == 0) {
				return;
			}

			GL11.glDeleteTextures(this.register);
			this.register = 0;
			registerImage.remove(this.register);
		} finally {
			lock.unlock();
		}

	}

	public static void unregister(int id) {
		lock.lock();

		try {
			GLTextureBufferedImage image = (GLTextureBufferedImage) registerImage.get(id);
			if (image != null) {
				image.unregister();
			}
		} finally {
			lock.unlock();
		}

	}

	public void setMagFilter(boolean b) {
		this.magFiltering = b;
	}

	public void setMinFilter(boolean b) {
		this.minFiltering = b;
	}

	public int getId() {
		return this.register;
	}

	public boolean getMagFilter() {
		return this.magFiltering;
	}

	public boolean getMinFilter() {
		return this.minFiltering;
	}

	public void setClampTexture(boolean b) {
		this.clampTexture = b;
	}

	public boolean isClampTexture() {
		return this.clampTexture;
	}

	public void setRGBA(int x, int y, byte r, byte g, byte b, byte a) {
		int i = (y * this.getWidth() + x) * 4;
		this.data[i++] = r;
		this.data[i++] = g;
		this.data[i++] = b;
		this.data[i] = a;
	}

	public void setRGB(int x, int y, byte r, byte g, byte b) {
		int i = (y * this.getWidth() + x) * 4;
		this.data[i++] = r;
		this.data[i++] = g;
		this.data[i++] = b;
		this.data[i] = -1;
	}

	public void setRGB(int x, int y, int rgb) {
		int i = (y * this.getWidth() + x) * 4;
		this.data[i++] = (byte) (rgb >> 16);
		this.data[i++] = (byte) (rgb >> 8);
		this.data[i++] = (byte) (rgb >> 0);
		this.data[i] = (byte) (rgb >> 24);
	}

	public static void createTexture(int[] data, int w, int h, int name, boolean blur, boolean clamp) {
		byte[] bs = new byte[w * h * 4];
		int i = 0;
		int j = data.length;

		for (int var9 = 0; i < j; ++i) {
			int pixel = data[i];
			bs[var9++] = (byte) (pixel >> 16);
			bs[var9++] = (byte) (pixel >> 8);
			bs[var9++] = (byte) (pixel >> 0);
			bs[var9++] = (byte) (pixel >> 24);
		}

		createTexture(bs, w, h, name, blur, clamp);
	}

	public static void createTexture(byte[] data, int w, int h, int name, boolean blur, boolean clamp) {
		GL11.glBindTexture(3553, name);
		GL11.glTexParameteri(3553, 10241, blur ? 9729 : 9728);
		GL11.glTexParameteri(3553, 10240, blur ? 9729 : 9728);
		GL11.glTexParameteri(3553, 10242, clamp ? 10496 : 10497);
		GL11.glTexParameteri(3553, 10243, clamp ? 10496 : 10497);
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		GL11.glTexImage2D(3553, 0, 6408, w, h, 0, 6408, 5121, buffer);
	}
}
