package by.fxg.seaside.network.minimap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.texturepacks.ITexturePack;
import net.minecraft.client.texturepacks.TexturePackList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public final class BlockColor {
	private static final ArrayList list = new ArrayList();
	private static final float d = 0.003921569F;
	private static final int AIR_COLOR = 16711935;
	private static final BlockColor AIR_BLOCK = instance(16711935);
	private static final int BLOCK_NUM;
	private static final BlockColor[] defaultColor;
	private static final BlockColor[] textureColor;
	private static final BlockColor[] userColor;
	private static final BlockColor[] blockColor;
	private static final boolean[] opaqueList;
	private static final boolean[] useMetadata;
	private static final boolean mcpatcher;
	private static final boolean custom_lava;
	private static final boolean custom_water;
	private static final HashMap plasmacraftColor;
	private final int argb;
	public final TintType tintType;
	public final float alpha;
	public final float red;
	public final float green;
	public final float blue;

	private static void calcBlockColor(BlockColor[]... blockColors) {
		Arrays.fill(blockColor, AIR_BLOCK);
		Arrays.fill(useMetadata, false);

		for (int id = 0; id < BLOCK_NUM; ++id) {
			BlockColor[] colors = null;
			BlockColor base = null;
			BlockColor[][] arr$ = blockColors;
			int ptr = blockColors.length;

			for (int i$ = 0; i$ < ptr; ++i$) {
				BlockColor[] bs = arr$[i$];
				if (bs[id << 4] != null) {
					colors = bs;
					base = bs[id << 4];
					blockColor[id << 4] = base;
					break;
				}
			}

			if (colors != null) {
				for (int meta = 1; meta < 16; ++meta) {
					ptr = pointer(id, meta);
					if (colors[ptr] != AIR_BLOCK && colors[ptr] != base) {
						blockColor[ptr] = colors[ptr];
						useMetadata[id] = true;
					} else {
						blockColor[ptr] = base;
					}
				}
			}
		}

	}

	public static void calcBlockColorTD() {
		calcBlockColor(textureColor, defaultColor);
	}

	public static void calcBlockColorD() {
		calcBlockColor(defaultColor);
	}

	public static void calcBlockColorT() {
		calcBlockColor(textureColor);
	}

	public static boolean useMetadata(int id) {
		return useMetadata[id];
	}

	public static BlockColor getBlockColor(int id, int meta) {
		return blockColor[pointer(id, meta)];
	}

	private static BlockColor instance(int argb) {
		return instance(argb, TintType.NONE);
	}

	private static BlockColor instance(int argb, TintType tint) {
		BlockColor bc = new BlockColor(argb, tint);
		int index = list.indexOf(bc);
		if (index == -1) {
			list.add(bc);
			return bc;
		} else {
			return (BlockColor) list.get(index);
		}
	}

	private static void setDefaultColor(int id, int meta, int argb) {
		TintType tint = TintType.NONE;
		switch (id) {
		case 2:
		case 106:
			tint = TintType.GRASS;
			break;
		case 8:
		case 9:
		case 79:
			tint = TintType.WATER;
			break;
		case 18:
			int m = meta & 3;
			if (m == 0) {
				tint = TintType.FOLIAGE;
			}

			if (m == 1) {
				tint = TintType.PINE;
			}

			if (m == 2) {
				tint = TintType.BIRCH;
			}

			if (m == 3) {
				tint = TintType.FOLIAGE;
			}
			break;
		case 20:
			tint = TintType.GLASS;
			break;
		case 31:
			if (meta == 1 || meta == 2) {
				tint = TintType.TALL_GRASS;
			}
		}

		defaultColor[pointer(id, meta)] = instance(argb, tint);
	}

	public static void textureColorUpdate() {
		Minecraft mc = ReiMinimap.instance.theMinecraft;
		TexturePackList tpl = mc.texturePackList;
		ITexturePack tpb = tpl.getSelectedTexturePack();
		HashMap terrainImagesMap = new HashMap();
		BufferedImage[] terrainImages = splitImage(readImage(tpb, "/terrain.png"));
		terrainImagesMap.put((Object) null, terrainImages);
		boolean water = false;
		boolean lava = false;

		try {
			Class clazz = Class.forName("ModLoader");
			Field field = clazz.getDeclaredField("overrides");
			field.setAccessible(true);
			Map overrides = (Map) field.get((Object) null);
			if (overrides != null) {
				Map terrain = (Map) overrides.get(0);
				Entry entry;
				if (terrain != null) {
					for (Iterator i$ = terrain.entrySet().iterator(); i$.hasNext(); terrainImages[(Integer) entry.getValue()] = readImage(tpb, (String) entry.getKey())) {
						entry = (Entry) i$.next();
					}
				}
			}
		} catch (Exception var45) {
			;
		}

		Arrays.fill(textureColor, AIR_BLOCK);
		BlockColor.TempBlockAccess ba = new BlockColor.TempBlockAccess();
		int id = 0;

		for (int j = BLOCK_NUM; id < j; ++id) {
			Block block = Block.blocksList[id];
			if (block != null) {
				if ("Plasmacraft.BlockCausticStationary".equals(block.getClass().getName()) || "Plasmacraft.BlockCausticFlowing".equals(block.getClass().getName())) {
					BlockColor bc = (BlockColor) plasmacraftColor.get(block.getBlockName());
					if (bc != null) {
						textureColor[pointer(id, 0)] = bc;
						continue;
					}
				}

				ba.blockId = id;
				String textureName = getBlockTexture(block);
				terrainImages = (BufferedImage[]) terrainImagesMap.get(textureName);
				if (terrainImages == null) {
					terrainImages = splitImage(readImage(tpb, textureName));
					terrainImagesMap.put(textureName, terrainImages);
				}

				int renderType = block.getRenderType();

				for (int meta = 0; meta < 16; ++meta) {
					try {
						boolean redstoneTorch = block instanceof BlockRedstoneTorch;
						int texture = block.getBlockTextureFromSideAndMetadata(redstoneTorch ? 0 : 1, meta);
						if (id == 18) {
							texture &= -2;
						}

						if (Block.blocksList[id].getClass().getCanonicalName().equals("twilightforest.BlockTFLeaves")) {
							texture &= -2;
						}

						ba.blockMetadata = meta;
						block.setBlockBoundsBasedOnState(ba, 0, 0, 0);
						double minX = block.getBlockBoundsMinX();
						double minZ = block.getBlockBoundsMinZ();
						double maxX = block.getBlockBoundsMaxX();
						double maxZ = block.getBlockBoundsMaxZ();
						int a1;
						int a2;
						int color;
						int a;
						int r;
						int g;
						int b;
						switch (renderType) {
						case 0:
							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						case 1:
							a1 = calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ);
							if ((a1 & -16777216) != 0) {
								a2 = Math.max(a1 >>> 24, 48) << 24;
								setTextureColor(id, meta, a1 & 16777215 | a2);
							}
							break;
						case 2:
							b = calcColorInt(terrainImages[texture], 0.4375D, 0.4375D, 0.5625D, 0.5625D);
							int argb2 = calcColorInt(terrainImages[texture], 0.375D, 0.375D, 0.625D, 0.625D);
							a1 = b >> 24 & 255;
							a2 = argb2 >> 24 & 255;
							color = a1 + a2;
							if (color != 0) {
								a = ((b >> 16 & 255) * a1 + (argb2 >> 16 & 255) * a2) / color;
								r = ((b >> 8 & 255) * a1 + (argb2 >> 8 & 255) * a2) / color;
								g = ((b >> 0 & 255) * a1 + (argb2 >> 0 & 255) * a2) / color;
								setTextureColor(id, meta, Integer.MIN_VALUE | a << 16 | r << 8 | g);
								break;
							} else {
								b = calcColorInt(terrainImages[texture], 0.25D, 0.25D, 0.75D, 0.75D);
								argb2 = calcColorInt(terrainImages[texture], 0.0D, 0.0D, 1.0D, 1.0D);
								a1 = b >> 24 & 255;
								a2 = argb2 >> 24 & 255;
								color = a1 + a2;
								if (color != 0) {
									a = ((b >> 16 & 255) * a1 + (argb2 >> 16 & 255) * a2) / color;
									r = ((b >> 8 & 255) * a1 + (argb2 >> 8 & 255) * a2) / color;
									g = ((b >> 0 & 255) * a1 + (argb2 >> 0 & 255) * a2) / color;
									setTextureColor(id, meta, Integer.MIN_VALUE | a << 16 | r << 8 | g);
									break;
								}
							}
						case 3:
							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						case 4:
							String resource = null;
							BufferedImage img = terrainImages[texture];
							color = 0;
							if (mcpatcher) {
								if (textureName == null) {
									if ((water || !custom_water || id != 8) && id != 9) {
										if (!lava && custom_lava && id == 10 || id == 11) {
											resource = "/custom_lava_still.png";
										}
									} else {
										resource = "/custom_water_still.png";
									}
								}

								if (resource != null) {
									InputStream in = tpb.getResourceAsStream(resource);
									if (in != null) {
										try {
											img = ImageIO.read(in);
										} catch (IOException var42) {
											;
										} finally {
											try {
												in.close();
											} catch (IOException var41) {
												;
											}

										}
									}
								}

								if (img != terrainImages[texture]) {
									terrainImages[texture] = img;
									if (id == 8 || id == 9) {
										water = true;
									}

									if (id == 10 || id == 11) {
										lava = true;
									}
								}
							} else {
								if (id == 8 || id == 9) {
									terrainImages[texture] = new BufferedImage(1, 1, 2);
									terrainImages[texture].setRGB(0, 0, -1960157441);
									water = true;
								}

								if (id == 10 || id == 11) {
									terrainImages[texture] = new BufferedImage(1, 1, 2);
									terrainImages[texture].setRGB(0, 0, -2530028);
									water = true;
								}
							}

							color = calcColorInt(img, 0.0D, 0.0D, 1.0D, 1.0D);
							if (id == 8 || id == 10) {
								a = color >> 30 & 255;
								r = color >> 16 & 255;
								g = color >> 8 & 255;
								b = color >> 0 & 255;
								r = (int) ((double) r * 0.9D);
								g = (int) ((double) g * 0.9D);
								b = (int) ((double) b * 0.9D);
								color = a << 30 | r << 16 | g << 8 | b << 0;
							}

							setTextureColor(id, meta, color);
							break;
						case 5:
							float f = (float) meta / 15.0F;
							a2 = calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ);
							if ((a2 & -16777216) != 0) {
								color = Math.max(a2 >> 24 & 255, 108);
								a = (int) ((float) (a2 >> 16 & 255) * Math.max(0.3F, f * 0.6F + 0.4F));
								r = (int) ((float) (a2 >> 8 & 255) * Math.max(0.0F, f * f * 0.7F - 0.5F));
								setTextureColor(id, meta, color << 24 | a << 16 | r << 8);
							}
							break;
						case 6:
							a1 = calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ);
							if ((a1 & -16777216) != 0) {
								a2 = Math.max(a1 >>> 24, 32) << 24;
								setTextureColor(id, meta, a1 & 16777215 | a2);
							}
							break;
						case 7:
							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						case 8:
							a1 = calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ);
							if ((a1 & -16777216) != 0) {
								a2 = Math.min(a1 >>> 24, 40) << 24;
								setTextureColor(id, meta, a1 & 16777215 | a2);
							}
							break;
						case 9:
							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						case 10:
							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						case 11:
							a1 = calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ);
							if ((a1 & -16777216) != 0) {
								a2 = Math.min(a1 >>> 24, 96) << 24;
								setTextureColor(id, meta, a1 & 16777215 | a2);
							}
							break;
						case 12:
							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						case 13:
							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						case 14:
							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						case 15:
							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						case 16:
							if (meta >= 10 && meta <= 13) {
								setTextureColor(id, meta, calcColorInt(terrainImages[texture], 0.0D, 0.25D, 1.0D, 1.0D));
								break;
							}

							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						case 17:
							if ((meta & 7) != 0 && (meta & 7) != 1) {
								setTextureColor(id, meta, calcColorInt(terrainImages[texture], 0.0D, 0.0D, 1.0D, 0.25D));
								break;
							}

							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						case 18:
							a1 = calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ);
							if ((a1 & -16777216) != 0) {
								a2 = Math.min(a1 >>> 24, 40) << 24;
								setTextureColor(id, meta, a1 & 16777215 | a2);
							}
							break;
						case 19:
							a1 = calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ);
							if ((a1 & -16777216) != 0) {
								a2 = Math.max(48, a1 >> 24 & 255);
								color = Math.min(255, Math.max(0, meta * 32 * (a1 >> 16 & 255) / 255));
								a = Math.min(255, Math.max(0, (255 - meta * 8) * (a1 >> 8 & 255) / 255));
								r = Math.min(255, Math.max(0, meta * 4 * (a1 >> 0 & 255) / 255));
								setTextureColor(id, meta, a2 << 24 | color << 16 | a << 8 | r << 0);
							}
							break;
						case 20:
							a1 = calcColorInt(terrainImages[texture], 0.0D, 0.0D, 1.0D, 1.0D);
							if ((a1 & -16777216) != 0) {
								a2 = Math.min(a1 >>> 24, 32) << 24;
								setTextureColor(id, meta, a1 & 16777215 | a2);
							}
							break;
						case 21:
							a1 = calcColorInt(terrainImages[texture], 0.0D, 0.0D, 1.0D, 1.0D);
							if ((a1 & -16777216) != 0) {
								a2 = Math.min(a1 >>> 24, 128) << 24;
								setTextureColor(id, meta, a1 & 16777215 | a2);
							}
							break;
						case 22:
							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						case 23:
							a1 = calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ);
							if ((a1 & -16777216) != 0) {
								a2 = a1 >> 24 & 255;
								color = (int) ((float) ((a1 >> 16 & 255) * 32) * 0.003921569F);
								a = (int) ((float) ((a1 >> 8 & 255) * 128) * 0.003921569F);
								r = (int) ((float) ((a1 >> 0 & 255) * 48) * 0.003921569F);
								setTextureColor(id, meta, a2 << 24 | color << 16 | a << 8 | r << 0);
							}
							break;
						case 24:
							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
							break;
						default:
							setTextureColor(id, meta, calcColorInt(terrainImages[texture], minX, minZ, maxX, maxZ));
						}
					} catch (Exception var44) {
						;
					}
				}
			}
		}

		Arrays.fill(useMetadata, false);

		for (id = 0; id < BLOCK_NUM; ++id) {
			BlockColor bc = textureColor[pointer(id, 0)];
			if (bc != null) {
				boolean use = false;

				for (int meta = 1; !use && meta < 16; ++meta) {
					use = !bc.equals(textureColor[pointer(id, meta)]);
				}

				useMetadata[id] = use;
			}
		}

	}

	private static void setTextureColor(int id, int meta, int argb) {
		if (opaqueList[id]) {
			argb |= -16777216;
		}

		if ((argb & -16777216) == 0) {
			textureColor[pointer(id, meta)] = AIR_BLOCK;
		} else {
			TintType tint = TintType.NONE;
			switch (id) {
			case 2:
			case 106:
				tint = TintType.GRASS;
				break;
			case 8:
			case 9:
			case 79:
				tint = TintType.WATER;
				break;
			case 18:
				int m = meta & 3;
				if (m == 0) {
					tint = TintType.FOLIAGE;
				}

				if (m == 1) {
					tint = TintType.PINE;
				}

				if (m == 2) {
					tint = TintType.BIRCH;
				}

				if (m == 3) {
					tint = TintType.FOLIAGE;
				}
				break;
			case 20:
				tint = TintType.GLASS;
				break;
			case 31:
				if (meta == 1 || meta == 2) {
					tint = TintType.TALL_GRASS;
				}
			}

			String className = Block.blocksList[id].getClass().getCanonicalName();
			if (className.equals("ic2.common.BlockRubLeaves")) {
				tint = TintType.BIRCH;
			}

			int m;
			if (className.equals("eloraam.world.BlockCustomLeaves")) {
				m = meta & 3;
				if (m == 0) {
					tint = TintType.FOLIAGE;
				}

				if (m == 1) {
					tint = TintType.PINE;
				}

				if (m == 2) {
					tint = TintType.BIRCH;
				}

				if (m == 3) {
					tint = TintType.FOLIAGE;
				}
			}

			if (className.equals("twilightforest.BlockTFLeaves")) {
				m = meta & 3;
				if (m == 0) {
					tint = TintType.TF_OAK;
				}

				if (m == 1) {
					tint = TintType.TF_CANOPY;
				}

				if (m == 2) {
					tint = TintType.TF_MANGROVE;
				}

				if (m == 3) {
					tint = TintType.TF_OAK;
				}
			}

			textureColor[pointer(id, meta)] = instance(argb, tint);
		}
	}

	private static int pointer(int id, int meta) {
		return id << 4 | meta;
	}

	private static BufferedImage readImage(ITexturePack tpb, String file) {
		InputStream in = null;

		BufferedImage img;
		label87: {
			try {
				in = tpb.getResourceAsStream(file);
				if (in == null) {
					break label87;
				}

				img = ImageIO.read(in);
			} catch (IOException var14) {
				break label87;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException var13) {
						;
					}
				}

			}

			return img;
		}

		img = new BufferedImage(1, 1, 2);
		img.setRGB(0, 0, 16711935);
		return img;
	}

	private static BufferedImage[] splitImage(BufferedImage image) {
		if (image == null) {
			image = new BufferedImage(1, 1, 2);
			image.setRGB(0, 0, 16711935);
			BufferedImage[] images = new BufferedImage[256];
			Arrays.fill(images, image);
			return images;
		} else {
			int width = Math.max(1, image.getWidth() >> 4);
			int height = Math.max(1, image.getHeight() >> 4);
			BufferedImage[] images = new BufferedImage[256];

			for (int i = 0; i < 256; ++i) {
				images[i] = GLTextureBufferedImage.create(width, height);
				int offsetX = (i & 15) * image.getWidth() >> 4;
				int offsetY = (i >> 4) * image.getHeight() >> 4;

				for (int y = 0; y < height; ++y) {
					for (int x = 0; x < width; ++x) {
						images[i].setRGB(x, y, image.getRGB(x + offsetX, y + offsetY));
					}
				}
			}

			return images;
		}
	}

	private static int calcColorInt(BufferedImage image, double minX, double minZ, double maxX, double maxZ) {
		int startX = (int) Math.floor((double) image.getWidth() * minX);
		int startY = (int) Math.floor((double) image.getHeight() * minZ);
		int endX = (int) Math.floor((double) image.getWidth() * maxX);
		int endY = (int) Math.floor((double) image.getHeight() * maxZ);
		long a = 0L;
		long r = 0L;
		long g = 0L;
		long b = 0L;

		int cnt;
		for (cnt = startY; cnt < endY; ++cnt) {
			for (int x = startX; x < endX; ++x) {
				int argb = image.getRGB(x, cnt);
				int _a = argb >> 24 & 255;
				a += (long) _a;
				r += (long) ((argb >> 16 & 255) * _a);
				g += (long) ((argb >> 8 & 255) * _a);
				b += (long) ((argb >> 0 & 255) * _a);
			}
		}

		if (a == 0L) {
			return 16711935;
		} else {
			cnt = image.getWidth() * image.getHeight();
			double d = 1.0D / (double) a;
			a /= (long) cnt;
			r = (long) Math.min(255, Math.max(0, (int) ((double) r * d)));
			g = (long) Math.min(255, Math.max(0, (int) ((double) g * d)));
			b = (long) Math.min(255, Math.max(0, (int) ((double) b * d)));
			return (int) (a << 24 | r << 16 | g << 8 | b);
		}
	}

	private BlockColor(int argb, TintType tint) {
		if (tint == null) {
			tint = TintType.NONE;
		}

		float a = (float) (argb >> 24 & 255) * 0.003921569F;
		float r = (float) (argb >> 16 & 255) * 0.003921569F;
		float g = (float) (argb >> 8 & 255) * 0.003921569F;
		float b = (float) (argb >> 0 & 255) * 0.003921569F;
		this.alpha = a;
		this.red = r;
		this.green = g;
		this.blue = b;
		this.argb = argb;
		this.tintType = tint;
	}

	public String toString() {
		return String.format("%08X:%s", this.argb, this.tintType);
	}

	public int hashCode() {
		return this.argb;
	}

	public boolean equals(Object obj) {
		return obj instanceof BlockColor && this.equals((BlockColor) obj);
	}

	boolean equals(BlockColor col) {
		return this.argb == col.argb && this.tintType == col.tintType;
	}

	private static String getBlockTexture(Block block) {
		Method[] arr$ = block.getClass().getMethods();
		int len$ = arr$.length;
		int i$ = 0;

		while (true) {
			if (i$ < len$) {
				Method m = arr$[i$];
				if (m.getReturnType() != String.class || m.getParameterTypes().length != 0 || !m.getName().equals("getTextureFile")) {
					++i$;
					continue;
				}

				try {
					return (String) m.invoke(block);
				} catch (Exception var6) {
					;
				}
			}

			return null;
		}
	}

	static {
		BLOCK_NUM = Block.blocksList.length;
		defaultColor = new BlockColor[BLOCK_NUM * 16 + 1];
		textureColor = new BlockColor[BLOCK_NUM * 16 + 1];
		userColor = new BlockColor[BLOCK_NUM * 16 + 1];
		blockColor = new BlockColor[BLOCK_NUM * 16 + 1];
		opaqueList = new boolean[BLOCK_NUM];
		useMetadata = new boolean[BLOCK_NUM];
		plasmacraftColor = new HashMap();
		boolean mcp = false;
		boolean b = false;
		boolean cw = false;
		boolean al = false;
		boolean var4 = false;

		try {
			Class clazz = Class.forName("com.pclewis.mcpatcher.MCPatcherUtils");
			mcp = true;
			Method method = clazz.getMethod("getBoolean", String.class, String.class, Boolean.TYPE);
			b = (Boolean) method.invoke((Object) null, "HD Textures", "customLava", Boolean.TRUE);
			cw = (Boolean) method.invoke((Object) null, "HD Textures", "customWater", Boolean.TRUE);
		} catch (Exception var7) {
			;
		}

		mcpatcher = mcp;
		custom_lava = b;
		custom_water = cw;
		plasmacraftColor.put("tile.cryoniteStill", instance(-1505376514, TintType.WATER));
		plasmacraftColor.put("tile.cryoniteMoving", instance(-1505376514, TintType.WATER));
		plasmacraftColor.put("tile.acidStill", instance(-1542062556, TintType.WATER));
		plasmacraftColor.put("tile.acidMoving", instance(-1542062556, TintType.WATER));
		plasmacraftColor.put("tile.plutoniumStill", instance(-32158378, TintType.WATER));
		plasmacraftColor.put("tile.plutoniumMoving", instance(-32158378, TintType.WATER));
		plasmacraftColor.put("tile.radioniteStill", instance(-29541033, TintType.WATER));
		plasmacraftColor.put("tile.radioniteMoving", instance(-29541033, TintType.WATER));
		plasmacraftColor.put("tile.uraniumStill", instance(-1292372202, TintType.WATER));
		plasmacraftColor.put("tile.uraniumMoving", instance(-1292372202, TintType.WATER));
		plasmacraftColor.put("tile.neptuniumStill", instance(-922849754, TintType.WATER));
		plasmacraftColor.put("tile.neptuniumMoving", instance(-922849754, TintType.WATER));
		plasmacraftColor.put("tile.netherflowStill", instance(-570552047, TintType.WATER));
		plasmacraftColor.put("tile.netherflowMoving", instance(-570552047, TintType.WATER));
		plasmacraftColor.put("tile.obsidiumStill", instance(-65140962, TintType.WATER));
		plasmacraftColor.put("tile.obsidiumMoving", instance(-65140962, TintType.WATER));
		Arrays.fill(defaultColor, AIR_BLOCK);
		setDefaultColor(1, 0, -9934744);
		setDefaultColor(2, 0, -12096451);
		setDefaultColor(3, 0, -8825542);
		setDefaultColor(4, 0, -6974059);
		setDefaultColor(5, 0, -4417438);
		setDefaultColor(6, 0, 1816358162);
		setDefaultColor(6, 1, 1412577569);
		setDefaultColor(6, 2, 1819645267);
		setDefaultColor(7, 0, -13421773);
		setDefaultColor(8, 0, -1960157441);
		setDefaultColor(9, 0, -1960157441);
		setDefaultColor(10, 0, -2530028);
		setDefaultColor(11, 0, -2530028);
		setDefaultColor(12, 0, -2238560);
		setDefaultColor(13, 0, -7766146);
		setDefaultColor(14, 0, -7304324);
		setDefaultColor(15, 0, -7830913);
		setDefaultColor(16, 0, -9145485);
		setDefaultColor(17, 0, -10006222);
		setDefaultColor(17, 1, -13358823);
		setDefaultColor(17, 2, -3620193);
		setDefaultColor(18, 0, -1708107227);
		setDefaultColor(18, 1, -1522906074);
		setDefaultColor(18, 2, -1707912909);
		setDefaultColor(18, 3, -1707912909);
		setDefaultColor(18, 4, -1708107227);
		setDefaultColor(18, 5, -1522906074);
		setDefaultColor(18, 6, -1707912909);
		setDefaultColor(18, 7, -1707912909);
		setDefaultColor(19, 0, -1710770);
		setDefaultColor(20, 0, 1090519039);
		setDefaultColor(21, 0, -9998201);
		setDefaultColor(22, 0, -14858330);
		setDefaultColor(23, 0, -10987432);
		setDefaultColor(24, 0, -2370913);
		setDefaultColor(25, 0, -10206158);
		setDefaultColor(26, 0, -6339259);
		setDefaultColor(26, 1, -6339259);
		setDefaultColor(26, 2, -6339259);
		setDefaultColor(26, 3, -6339259);
		setDefaultColor(26, 4, -6339259);
		setDefaultColor(26, 5, -6339259);
		setDefaultColor(26, 6, -6339259);
		setDefaultColor(26, 7, -6339259);
		setDefaultColor(26, 8, -6397599);
		setDefaultColor(26, 9, -6397599);
		setDefaultColor(26, 10, -6397599);
		setDefaultColor(26, 11, -6397599);
		setDefaultColor(26, 12, -6397599);
		setDefaultColor(26, 13, -6397599);
		setDefaultColor(26, 14, -6397599);
		setDefaultColor(26, 15, -6397599);
		setDefaultColor(27, 0, -528457632);
		setDefaultColor(27, 1, -528457632);
		setDefaultColor(27, 2, -528457632);
		setDefaultColor(27, 3, -528457632);
		setDefaultColor(27, 4, -528457632);
		setDefaultColor(27, 5, -528457632);
		setDefaultColor(27, 6, -528457632);
		setDefaultColor(27, 7, -528457632);
		setDefaultColor(27, 8, -523214752);
		setDefaultColor(27, 9, -523214752);
		setDefaultColor(27, 10, -523214752);
		setDefaultColor(27, 11, -523214752);
		setDefaultColor(27, 12, -523214752);
		setDefaultColor(27, 13, -523214752);
		setDefaultColor(27, 14, -523214752);
		setDefaultColor(27, 15, -523214752);
		setDefaultColor(28, 0, -8952744);
		setDefaultColor(29, 0, -9605779);
		setDefaultColor(29, 1, -7499421);
		setDefaultColor(29, 2, -9804194);
		setDefaultColor(29, 3, -9804194);
		setDefaultColor(29, 4, -9804194);
		setDefaultColor(29, 5, -9804194);
		setDefaultColor(29, 8, -9605779);
		setDefaultColor(29, 9, -7499421);
		setDefaultColor(29, 10, -9804194);
		setDefaultColor(29, 11, -9804194);
		setDefaultColor(29, 12, -9804194);
		setDefaultColor(29, 13, -9804194);
		setDefaultColor(30, 0, 1775884761);
		setDefaultColor(31, 0, 1383747097);
		setDefaultColor(31, 1, -1571782606);
		setDefaultColor(31, 2, 1330675762);
		setDefaultColor(32, 0, 1383747097);
		setDefaultColor(33, 0, -9605779);
		setDefaultColor(33, 1, -6717094);
		setDefaultColor(33, 2, -9804194);
		setDefaultColor(33, 3, -9804194);
		setDefaultColor(33, 4, -9804194);
		setDefaultColor(33, 5, -9804194);
		setDefaultColor(33, 8, -9605779);
		setDefaultColor(33, 9, -6717094);
		setDefaultColor(33, 10, -9804194);
		setDefaultColor(33, 11, -9804194);
		setDefaultColor(33, 12, -9804194);
		setDefaultColor(33, 13, -9804194);
		setDefaultColor(34, 0, -6717094);
		setDefaultColor(34, 1, -6717094);
		setDefaultColor(34, 2, -2137423526);
		setDefaultColor(34, 3, -2137423526);
		setDefaultColor(34, 4, -2137423526);
		setDefaultColor(34, 5, -2137423526);
		setDefaultColor(34, 8, -6717094);
		setDefaultColor(34, 9, -7499421);
		setDefaultColor(34, 10, -2137423526);
		setDefaultColor(34, 11, -2137423526);
		setDefaultColor(34, 12, -2137423526);
		setDefaultColor(34, 13, -2137423526);
		setDefaultColor(35, 0, -2236963);
		setDefaultColor(35, 1, -1475018);
		setDefaultColor(35, 2, -4370744);
		setDefaultColor(35, 3, -9991469);
		setDefaultColor(35, 4, -4082660);
		setDefaultColor(35, 5, -12928209);
		setDefaultColor(35, 6, -2588006);
		setDefaultColor(35, 7, -12434878);
		setDefaultColor(35, 8, -6445916);
		setDefaultColor(35, 9, -14191468);
		setDefaultColor(35, 10, -8374846);
		setDefaultColor(35, 11, -14273895);
		setDefaultColor(35, 12, -11193573);
		setDefaultColor(35, 13, -13153256);
		setDefaultColor(35, 14, -6083544);
		setDefaultColor(35, 15, -15067369);
		setDefaultColor(37, 0, -1057883902);
		setDefaultColor(38, 0, -1057552625);
		setDefaultColor(39, 0, -1064211115);
		setDefaultColor(40, 0, -1063643364);
		setDefaultColor(41, 0, -66723);
		setDefaultColor(42, 0, -1447447);
		setDefaultColor(43, 0, -5723992);
		setDefaultColor(43, 1, -1712721);
		setDefaultColor(43, 2, -7046838);
		setDefaultColor(43, 3, -8224126);
		setDefaultColor(43, 4, -6591135);
		setDefaultColor(43, 5, -8750470);
		setDefaultColor(44, 0, -5723992);
		setDefaultColor(44, 1, -1712721);
		setDefaultColor(44, 2, -7046838);
		setDefaultColor(44, 3, -8224126);
		setDefaultColor(44, 4, -6591135);
		setDefaultColor(44, 5, -8750470);
		setDefaultColor(45, 0, -6591135);
		setDefaultColor(46, 0, -2407398);
		setDefaultColor(47, 0, -4943782);
		setDefaultColor(48, 0, -14727393);
		setDefaultColor(49, 0, -15527395);
		setDefaultColor(50, 0, 1627379712);
		setDefaultColor(51, 0, -4171263);
		setDefaultColor(52, 0, -14262393);
		setDefaultColor(53, 0, -4417438);
		setDefaultColor(54, 0, -7378659);
		setDefaultColor(55, 0, 1827466476);
		setDefaultColor(56, 0, -8287089);
		setDefaultColor(57, 0, -10428192);
		setDefaultColor(58, 0, -8038091);
		setDefaultColor(59, 0, 302029071);
		setDefaultColor(59, 1, 957524751);
		setDefaultColor(59, 2, 1444710667);
		setDefaultColor(59, 3, -1708815608);
		setDefaultColor(59, 4, -835813369);
		setDefaultColor(59, 5, -532579833);
		setDefaultColor(59, 6, -531663353);
		setDefaultColor(59, 7, -531208953);
		setDefaultColor(60, 0, -9221331);
		setDefaultColor(60, 1, -9550295);
		setDefaultColor(60, 2, -9879003);
		setDefaultColor(60, 3, -10207967);
		setDefaultColor(60, 4, -10536675);
		setDefaultColor(60, 5, -10865383);
		setDefaultColor(60, 6, -11194347);
		setDefaultColor(60, 7, -11523055);
		setDefaultColor(60, 8, -11786226);
		setDefaultColor(61, 0, -9145228);
		setDefaultColor(62, 0, -8355712);
		setDefaultColor(63, 0, -1598779307);
		setDefaultColor(64, 0, -1064934094);
		setDefaultColor(65, 0, -2139595212);
		setDefaultColor(66, 0, -8951211);
		setDefaultColor(67, 0, -6381922);
		setDefaultColor(68, 0, -1598779307);
		setDefaultColor(69, 0, -1603709901);
		setDefaultColor(70, 0, -7368817);
		setDefaultColor(71, 0, -1061043775);
		setDefaultColor(72, 0, -4417438);
		setDefaultColor(73, 0, -6981535);
		setDefaultColor(74, 0, -6981535);
		setDefaultColor(75, 0, -2141709038);
		setDefaultColor(76, 0, -2136923117);
		setDefaultColor(77, 0, -2139851660);
		setDefaultColor(78, 0, -1314833);
		setDefaultColor(79, 0, -1619219203);
		setDefaultColor(80, 0, -986896);
		setDefaultColor(81, 0, -15695840);
		setDefaultColor(82, 0, -6380624);
		setDefaultColor(83, 0, -7094428);
		setDefaultColor(84, 0, -9811658);
		setDefaultColor(85, 0, -4417438);
		setDefaultColor(86, 0, -4229867);
		setDefaultColor(87, 0, -9751501);
		setDefaultColor(88, 0, -11255757);
		setDefaultColor(89, 0, -4157626);
		setDefaultColor(90, 0, -9231226);
		setDefaultColor(91, 0, -3893474);
		setDefaultColor(92, 0, -1848115);
		setDefaultColor(93, 0, -6843501);
		setDefaultColor(94, 0, -4156525);
		setDefaultColor(95, 0, -7378659);
		setDefaultColor(96, 0, -8495827);
		setDefaultColor(96, 1, -8495827);
		setDefaultColor(96, 2, -8495827);
		setDefaultColor(96, 3, -8495827);
		setDefaultColor(96, 4, 545152301);
		setDefaultColor(96, 5, 545152301);
		setDefaultColor(96, 6, 545152301);
		setDefaultColor(96, 7, 545152301);
		setDefaultColor(97, 0, -9934744);
		setDefaultColor(97, 1, -6974059);
		setDefaultColor(97, 2, -8750470);
		setDefaultColor(98, 0, -8750470);
		setDefaultColor(98, 1, -9275542);
		setDefaultColor(98, 2, -9013642);
		setDefaultColor(99, 0, -3495048);
		setDefaultColor(99, 1, -7509421);
		setDefaultColor(99, 2, -7509421);
		setDefaultColor(99, 3, -7509421);
		setDefaultColor(99, 4, -7509421);
		setDefaultColor(99, 5, -7509421);
		setDefaultColor(99, 6, -7509421);
		setDefaultColor(99, 7, -7509421);
		setDefaultColor(99, 8, -7509421);
		setDefaultColor(99, 9, -7509421);
		setDefaultColor(99, 10, -3495048);
		setDefaultColor(100, 0, -3495048);
		setDefaultColor(100, 1, -4840156);
		setDefaultColor(100, 2, -4840156);
		setDefaultColor(100, 3, -4840156);
		setDefaultColor(100, 4, -4840156);
		setDefaultColor(100, 5, -4840156);
		setDefaultColor(100, 6, -4840156);
		setDefaultColor(100, 7, -4840156);
		setDefaultColor(100, 8, -4840156);
		setDefaultColor(100, 9, -4840156);
		setDefaultColor(100, 10, -3495048);
		setDefaultColor(101, 0, -2140312470);
		setDefaultColor(102, 0, 1627389951);
		setDefaultColor(103, 0, -6842076);
		setDefaultColor(104, 0, 1073780992);
		setDefaultColor(104, 1, 1209242626);
		setDefaultColor(104, 2, 1344704516);
		setDefaultColor(104, 3, 1480166151);
		setDefaultColor(104, 4, 1615693321);
		setDefaultColor(104, 5, 1751154956);
		setDefaultColor(104, 6, 1886616590);
		setDefaultColor(104, 7, 2022144016);
		setDefaultColor(105, 0, 1073780992);
		setDefaultColor(105, 1, 1209242626);
		setDefaultColor(105, 2, 1344704516);
		setDefaultColor(105, 3, 1480166151);
		setDefaultColor(105, 4, 1615693321);
		setDefaultColor(105, 5, 1751154956);
		setDefaultColor(105, 6, 1886616590);
		setDefaultColor(105, 7, 2022144016);
		setDefaultColor(106, 0, -2145432054);
		setDefaultColor(107, 0, -1061382046);
		setDefaultColor(108, 0, -6591135);
		setDefaultColor(109, 0, -8750470);

		for (int id = 0; id < BLOCK_NUM; ++id) {
			b = true;

			for (int meta = 0; b && meta < 16; ++meta) {
				BlockColor bc = defaultColor[pointer(id, meta)];
				b = bc == null || bc.alpha != 0.0F && bc.alpha != 1.0F;
			}

			opaqueList[id] = b;
		}

	}

	private static class TempBlockAccess implements IBlockAccess {
		private int blockId;
		private TileEntity blockTileEntity;
		private int lightBrightnessForSkyBlocks;
		private float brightness;
		private float getLightBrightness;
		private int blockMetadata;
		private Material blockMaterial;
		private boolean blockOpaqueCube;
		private boolean blockNormalCube;
		private boolean airBlock;
		private WorldChunkManager worldChunkManager;

		private TempBlockAccess() {
		}

		public int getBlockId(int i, int j, int k) {
			return this.blockId;
		}

		public TileEntity getBlockTileEntity(int i, int j, int k) {
			return this.blockTileEntity;
		}

		public int getLightBrightnessForSkyBlocks(int i, int j, int k, int l) {
			return this.lightBrightnessForSkyBlocks;
		}

		public float getBrightness(int i, int j, int k, int l) {
			return this.brightness;
		}

		public float getLightBrightness(int i, int j, int k) {
			return this.getLightBrightness;
		}

		public int getBlockMetadata(int i, int j, int k) {
			return this.blockMetadata;
		}

		public Material getBlockMaterial(int i, int j, int k) {
			return this.blockMaterial;
		}

		public boolean isBlockOpaqueCube(int i, int j, int k) {
			return this.blockOpaqueCube;
		}

		public boolean isBlockNormalCube(int i, int j, int k) {
			return this.blockNormalCube;
		}

		public boolean isAirBlock(int i, int j, int k) {
			return this.airBlock;
		}

		public BiomeGenBase getBiomeGenForCoords(int x, int z) {
			return BiomeGenBase.plains;
		}

		public int getHeight() {
			return 0;
		}

		public boolean extendedLevelsInChunkCache() {
			return false;
		}

		public boolean doesBlockHaveSolidTopSurface(int i, int j, int k) {
			return false;
		}

		public Vec3Pool getWorldVec3Pool() {
			return null;
		}

		public boolean isBlockProvidingPowerTo(int var1, int var2, int var3, int var4) {
			return false;
		}

		// $FF: synthetic method
		TempBlockAccess(Object x0) {
			this();
		}
	}
}
