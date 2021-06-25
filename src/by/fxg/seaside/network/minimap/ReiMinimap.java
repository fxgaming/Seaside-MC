package by.fxg.seaside.network.minimap;

import java.awt.Desktop;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.texturepacks.ITexturePack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public class ReiMinimap implements Runnable {
	public static final boolean DEBUG_BUILD = false;
	public static final boolean SUPPORT_HEIGHT_MOD = false;
	public static final boolean SUPPORT_NEW_LIGHTING = false;
	public static final boolean SUPPORT_SWAMPLAND_BIOME_COLOR = true;
	public static final boolean CHANGE_SUNRISE_DIRECTION = true;
	public boolean useModloader = true;
	private static final double renderZ = 1.0D;
	private static final boolean noiseAdded = false;
	private static final float noiseAlpha = 0.1F;
	static final File directory;
	private float[] lightBrightnessTable = this.generateLightBrightnessTable(0.125F);
	private static final int[] updateFrequencys;
	public static final ReiMinimap instance;
	private static final int TEXTURE_SIZE = 256;
	private int updateCount;
	private static BiomeGenBase[] bgbList;
	Minecraft theMinecraft;
	MinecraftServer server;
	private Tessellator tessellator;
	private World theWorld;
	private EntityPlayer thePlayer;
	private double playerPosX;
	private double playerPosY;
	private double playerPosZ;
	private float playerRotationYaw;
	private float playerRotationPitch;
	private GuiIngame ingameGUI;
	private ScaledResolution scaledResolution;
	private String errorString;
	private boolean multiplayer;
	private SocketAddress currentServer;
	private String currentLevelName;
	private int currentDimension;
	private int scWidth;
	private int scHeight;
	private GLTextureBufferedImage texture;
	final Thread mcThread;
	private Thread workerThread;
	private Lock lock;
	private Condition condition;
	private StripCounter stripCounter;
	private int stripCountMax1;
	private int stripCountMax2;
	private GuiScreen guiScreen;
	private int posX;
	private int posY;
	private double posYd;
	private int posZ;
	private int chunkCoordX;
	private int chunkCoordZ;
	private float sin;
	private float cos;
	private int lastX;
	private int lastY;
	private int lastZ;
	private int skylightSubtracted;
	private boolean isUpdateImage;
	private boolean isCompleteImage;
	private boolean enable;
	private boolean showMenuKey;
	private boolean filtering;
	private int mapPosition;
	private int textureView;
	private float mapOpacity;
	private float largeMapOpacity;
	private boolean largeMapLabel;
	private int lightmap;
	private int lightType;
	private boolean undulate;
	private boolean transparency;
	private boolean environmentColor;
	private boolean omitHeightCalc;
	private int updateFrequencySetting;
	private boolean threading;
	private int threadPriority;
	private boolean preloadedChunks;
	private boolean hideSnow;
	private boolean showChunkGrid;
	private boolean showSlimeChunk;
	private boolean heightmap;
	private boolean showCoordinate;
	private int fontScale;
	private int mapScale;
	private int largeMapScale;
	private int coordinateType;
	private boolean visibleWaypoints;
	private boolean deathPoint;
	private boolean useStencil;
	private boolean notchDirection;
	private boolean roundmap;
	private boolean fullmap;
	private boolean forceUpdate;
	private boolean marker;
	private boolean markerLabel;
	private boolean markerIcon;
	private boolean markerDistance;
	private long currentTimeMillis;
	private long currentTime;
	private long previousTime;
	private int renderType;
	private TreeMap wayPtsMap;
	private List wayPts;
	private int waypointDimension;
	private static final double[] ZOOM_LIST;
	private int defaultZoom;
	private int flagZoom;
	private int largeZoom;
	private double targetZoom;
	private double currentZoom;
	private float zoomVisible;
	private int grassColor;
	private int foliageColor;
	private int foliageColorPine;
	private int foliageColorBirch;
	private int tfOakColor;
	private int tfCanopyColor;
	private int tfMangroveColor;
	private ITexturePack texturePack;
	private int worldHeight;
	private int[] temperatureColor;
	private int[] humidityColor;
	private HashMap dimensionName;
	private HashMap dimensionScale;
	private boolean chatWelcomed;
	private List chatLineList;
	private ChatLine chatLineLast;
	private long chatTime;
	private boolean configEntityDirection;
	private boolean allowEntitiesRadar;
	private boolean allowEntityPlayer;
	private boolean visibleEntitiesRadar;
	private boolean visibleEntityPlayer;
	private long seed;
	private long ticksExisted;
	private static final int ENTITY_PLAYER_TYPE = 0;
	private static final int ENTITY_MOB_TYPE = 1;
	private static final int ENTITY_ANIMAL_TYPE = 2;
	private static final int ENTITY_SQUID_TYPE = 3;
	private static final int ENTITY_SLIME_TYPE = 4;
	private static final int ENTITY_LIVING_TYPE = 5;
	private static final int ENTITY_INVASION_MOB_TYPE = 6;
	private List[] visibleEntities;
	private int[] visibleEntityColor;
	private List weatherEffects;
	private static final Class entityIMWaveAttackerClass;
	private boolean autoUpdateCheck;
	private int updateCheckFlag;
	private URL updateCheckURL;
	private Timer timer;
	private float renderPartialTicks;
	long ntime;
	int count;
	static float[] temp;
	private float[] lightmapRed;
	private float[] lightmapGreen;
	private float[] lightmapBlue;

	boolean getAllowEntitiesRadar() {
		return this.allowEntitiesRadar;
	}

	public ReiMinimap() {
		this.tessellator = Tessellator.instance;
		this.texture = GLTextureBufferedImage.create(256, 256);
		this.lock = new ReentrantLock();
		this.condition = this.lock.newCondition();
		this.stripCounter = new StripCounter(289);
		this.stripCountMax1 = 0;
		this.stripCountMax2 = 0;
		this.enable = true;
		this.showMenuKey = true;
		this.filtering = true;
		this.mapPosition = 2;
		this.textureView = 0;
		this.mapOpacity = 1.0F;
		this.largeMapOpacity = 1.0F;
		this.largeMapLabel = false;
		this.lightmap = 0;
		this.lightType = 0;
		this.undulate = true;
		this.transparency = true;
		this.environmentColor = true;
		this.omitHeightCalc = true;
		this.updateFrequencySetting = 2;
		this.threading = false;
		this.threadPriority = 1;
		this.preloadedChunks = false;
		this.hideSnow = false;
		this.showChunkGrid = false;
		this.showSlimeChunk = false;
		this.heightmap = true;
		this.showCoordinate = true;
		this.fontScale = 1;
		this.mapScale = 1;
		this.largeMapScale = 1;
		this.coordinateType = 1;
		this.visibleWaypoints = true;
		this.deathPoint = false;
		this.useStencil = false;
		this.notchDirection = true;
		this.roundmap = false;
		this.fullmap = false;
		this.marker = true;
		this.markerLabel = true;
		this.markerIcon = true;
		this.markerDistance = true;
		this.renderType = 0;
		this.wayPtsMap = new TreeMap();
		this.wayPts = new ArrayList();
		this.defaultZoom = 1;
		this.flagZoom = 1;
		this.largeZoom = 0;
		this.targetZoom = 1.0D;
		this.currentZoom = 1.0D;
		this.tfOakColor = 4764952;
		this.tfCanopyColor = 6330464;
		this.tfMangroveColor = 8431445;
		this.worldHeight = 255;
		this.dimensionName = new HashMap();
		this.dimensionScale = new HashMap();
		this.dimensionName.put(0, "Overworld");
		this.dimensionScale.put(0, 1.0D);
		this.dimensionName.put(-1, "Nether");
		this.dimensionScale.put(-1, 8.0D);
		this.dimensionName.put(1, "The Ender");
		this.dimensionScale.put(1, 1.0D);
		this.chatTime = 0L;
		this.configEntityDirection = true;
		this.visibleEntities = new ArrayList[7];
		this.visibleEntityColor = new int[] { -16711681, -65536, -1, -16760704, -10444704, -12533632, -8388416 };
		this.weatherEffects = new ArrayList(256);
		this.autoUpdateCheck = false;
		this.updateCheckFlag = 0;

		try {
			this.updateCheckURL = new URL("http://dl.dropbox.com/u/34787499/minecraft/version.txt");
		} catch (Exception var2) {
			;
		}

		this.ntime = 0L;
		this.count = 0;
		this.lightmapRed = new float[256];
		this.lightmapGreen = new float[256];
		this.lightmapBlue = new float[256];
		if (!directory.exists()) {
			directory.mkdirs();
		}

		if (!directory.isDirectory()) {
			this.errorString = "[Rei's Minimap] ERROR: Failed to create the rei_minimap folder.";
			error(this.errorString);
		}

		this.loadOptions();
		this.mcThread = Thread.currentThread();

		for (int i = 0; i < this.visibleEntities.length; ++i) {
			this.visibleEntities[i] = new ArrayList();
		}

	}

	public void onTickInGame(Minecraft mc) {
		this.onTickInGame(1.0F, mc);
	}

	public void onTickInGame(float f, Minecraft mc) {
		try {
			this.renderPartialTicks = this.timer == null ? f : this.timer.renderPartialTicks;
			this.currentTimeMillis = System.currentTimeMillis();
			GL11.glPushAttrib(1048575);
			GL11.glPushClientAttrib(-1);
			GL11.glPushMatrix();

			label4178: {
				try {
					if (mc == null) {
						return;
					}

					if (this.errorString == null) {
						Field[] levelName;
						int len$;
						int type;
						int z;
						if (this.theMinecraft == null) {
							this.theMinecraft = mc;
							this.ingameGUI = this.theMinecraft.ingameGUI;

							try {
								int temp = 0;
								levelName = GuiNewChat.class.getDeclaredFields();
								len$ = levelName.length;

								for (type = 0; type < len$; ++type) {
									Field fields = levelName[type];
									if (fields.getType() == List.class && temp++ == 1) {
										fields.setAccessible(true);
										this.chatLineList = (List) fields.get(this.ingameGUI.getChatGUI());
										break;
									}
								}
							} catch (Exception var56) {
								;
							}

							this.chatLineList = (List) (this.chatLineList == null ? new ArrayList() : this.chatLineList);

							Field[] arr$;
							Field field;
							try {
								arr$ = RenderManager.class.getDeclaredFields();
								z = arr$.length;

								for (len$ = 0; len$ < z; ++len$) {
									field = arr$[len$];
									if (field.getType() == Map.class) {
										WaypointEntityRender wer = new WaypointEntityRender(mc);
										wer.setRenderManager(RenderManager.instance);
										field.setAccessible(true);
										((Map) field.get(RenderManager.instance)).put(WaypointEntity.class, wer);
										break;
									}
								}
							} catch (Exception var55) {
								var55.printStackTrace();
							}

							try {
								arr$ = Minecraft.class.getDeclaredFields();
								z = arr$.length;

								for (len$ = 0; len$ < z; ++len$) {
									field = arr$[len$];
									if (field.getType() == Timer.class) {
										field.setAccessible(true);
										this.timer = (Timer) field.get(mc);
									}
								}
							} catch (Exception var54) {
								var54.printStackTrace();
							}
						}

						if (this.texturePack != mc.texturePackList.getSelectedTexturePack()) {
							this.texturePack = mc.texturePackList.getSelectedTexturePack();
							BlockColor.textureColorUpdate();
							BlockColor.calcBlockColorTD();
							this.temperatureColor = GLTexture.TEMPERATURE.getData();
							this.humidityColor = GLTexture.HUMIDITY.getData();
						}

						this.thePlayer = this.theMinecraft.thePlayer;
						this.playerPosX = this.thePlayer.prevPosX + (this.thePlayer.posX - this.thePlayer.prevPosX) * (double) f;
						this.playerPosY = this.thePlayer.prevPosY + (this.thePlayer.posY - this.thePlayer.prevPosY) * (double) f;
						this.playerPosZ = this.thePlayer.prevPosZ + (this.thePlayer.posZ - this.thePlayer.prevPosZ) * (double) f;
						this.playerRotationYaw = this.thePlayer.prevRotationYaw + (this.thePlayer.rotationYaw - this.thePlayer.prevRotationYaw) * f;
						this.playerRotationPitch = this.thePlayer.prevRotationPitch + (this.thePlayer.rotationPitch - this.thePlayer.prevRotationPitch) * f;
						int y;
						int scale;
						int x;
						String name;
						if (this.theWorld != this.theMinecraft.theWorld) {
							this.updateCount = 0;
							this.isUpdateImage = false;
							this.texture.unregister();
							this.theWorld = this.theMinecraft.theWorld;
							this.theWorld.addWeatherEffect(new WaypointEntity(this.theMinecraft));
							this.multiplayer = this.theMinecraft.getIntegratedServer() == null;
							Arrays.fill(this.texture.data, (byte) 0);
							if (this.theWorld != null) {
								this.worldHeight = this.theWorld.getHeight() - 1;
								ChunkData.init();
								boolean changeWorld;
								String levelNameZ;
								if (this.multiplayer) {
									this.seed = 0L;
									levelNameZ = null;
									SocketAddress addr = getRemoteSocketAddress(this.thePlayer);
									if (addr == null) {
										throw new MinimapException("SMP ADDRESS ACQUISITION FAILURE");
									}

									changeWorld = this.currentServer != addr;
									if (changeWorld) {
										name = addr.toString().replaceAll("[\r\n]", "");
										Matcher matcher = Pattern.compile("(.*)/(.*):([0-9]+)").matcher(name);
										if (!matcher.matches()) {
											String str = addr.toString().replaceAll("[a-z]", "a").replaceAll("[A-Z]", "A").replaceAll("[0-9]", "*");
											throw new MinimapException("SMP ADDRESS FORMAT EXCEPTION: " + str);
										}

										levelNameZ = matcher.group(1);
										if (levelNameZ.isEmpty()) {
											levelNameZ = matcher.group(2);
										}

										if (!matcher.group(3).equals("25565")) {
											levelNameZ = levelNameZ + "[" + matcher.group(3) + "]";
										}

										char[] arr$ = ChatAllowedCharacters.allowedCharactersArray;
										y = arr$.length;

										for (scale = 0; scale < y; ++scale) {
											char c = arr$[scale];
											levelNameZ = levelNameZ.replace(c, '_');
										}

										this.currentLevelName = levelNameZ;
										this.currentServer = addr;
									}
								} else {
									levelNameZ = this.theMinecraft.getIntegratedServer().getWorldName();
									if (levelNameZ == null) {
										throw new MinimapException("WORLD_NAME ACQUISITION FAILURE");
									}

									char[] arr$ = ChatAllowedCharacters.allowedCharactersArray;
									type = arr$.length;

									for (x = 0; x < type; ++x) {
										char c = arr$[x];
										levelNameZ = levelNameZ.replace(c, '_');
									}

									changeWorld = !levelNameZ.equals(this.currentLevelName) || this.currentServer != null;
									if (changeWorld) {
										this.currentLevelName = levelNameZ;
										changeWorld = true;
									}

									this.currentServer = null;
								}

								if (changeWorld) {
									this.chatTime = System.currentTimeMillis();
									this.chatWelcomed = !this.multiplayer;
									this.allowEntitiesRadar = !this.multiplayer;
									this.allowEntityPlayer = !this.multiplayer;
									this.loadWaypoints();
								}
							}

							this.stripCounter.reset();
							this.currentDimension = -2147483647;
						}

						if (this.currentDimension != this.thePlayer.dimension) {
							this.currentDimension = this.thePlayer.dimension;
							this.waypointDimension = this.currentDimension;
							this.wayPts = (List) this.wayPtsMap.get(this.waypointDimension);
							if (this.wayPts == null) {
								this.wayPts = new ArrayList();
								this.wayPtsMap.put(this.waypointDimension, this.wayPts);
							}
						}

						if (!this.chatWelcomed && System.currentTimeMillis() < this.chatTime + 10000L) {
							Iterator i$ = this.chatLineList.iterator();

							while (i$.hasNext()) {
								ChatLine cl = (ChatLine) i$.next();
								if (cl == null || this.chatLineLast == cl) {
									break;
								}

								Matcher matcher = Pattern.compile("§0§0((?:§[1-9a-d])+)§e§f").matcher(cl.getChatLineString());

								while (matcher.find()) {
									this.chatWelcomed = true;
									char[] arr$ = matcher.group(1).toCharArray();
									x = arr$.length;

									for (x = 0; x < x; ++x) {
										char ch = arr$[x];
										switch (ch) {
										case '2':
											this.allowEntityPlayer = true;
											break;
										}
									}
								}

								this.chatLineLast = this.chatLineList.isEmpty() ? null : (ChatLine) this.chatLineList.get(0);
								if (this.chatWelcomed) {
									this.allowEntitiesRadar = this.allowEntityPlayer;
									if (this.allowEntitiesRadar) {
										StringBuilder sb = new StringBuilder("§E[Rei's Minimap] enabled: entities radar (");
										if (this.allowEntityPlayer) {
											sb.append("Player, ");
										}
										sb.setLength(sb.length() - 2);
										sb.append(")");
										this.chatInfo(sb.toString());
									}
								}
							}
						} else {
							this.chatWelcomed = true;
						}

						this.visibleEntitiesRadar = this.allowEntitiesRadar;
						this.visibleEntityPlayer = this.allowEntityPlayer;
						Entity ticksEntity = this.thePlayer.ridingEntity == null ? this.thePlayer : this.thePlayer.ridingEntity;
						if ((long) ((Entity) ticksEntity).ticksExisted != this.ticksExisted) {
							++this.updateCount;
							this.ticksExisted = (long) this.thePlayer.ticksExisted;

							for (z = -8; z <= 8; ++z) {
								for (len$ = -8; len$ <= 8; ++len$) {
									ChunkData cd = ChunkData.createChunkData(this.thePlayer.chunkCoordX + len$, this.thePlayer.chunkCoordZ + z);
									if (cd != null) {
										cd.updateChunk(this.preloadedChunks);
									}
								}
							}

							List[] arr$ = this.visibleEntities;
							len$ = arr$.length;

							for (type = 0; type < len$; ++type) {
								List list = arr$[type];
								list.clear();
							}

							this.weatherEffects.clear();
							if (this.visibleEntitiesRadar) {
								Iterator i$ = this.theWorld.loadedEntityList.iterator();

								while (i$.hasNext()) {
									Entity entity = (Entity) i$.next();
									type = this.getVisibleEntityType(entity);
									if (type != -1) {
										this.visibleEntities[type].add((EntityLiving) entity);
									}
								}

								this.weatherEffects.addAll(this.theWorld.weatherEffects);
							}
						}

						z = this.theMinecraft.displayWidth;
						len$ = this.theMinecraft.displayHeight;
						this.scaledResolution = new ScaledResolution(this.theMinecraft.gameSettings, z, len$);
						GL11.glScaled(1.0D / (double) this.scaledResolution.getScaleFactor(), 1.0D / (double) this.scaledResolution.getScaleFactor(), 1.0D);
						this.scWidth = mc.displayWidth;
						this.scHeight = mc.displayHeight;
						KeyInput.update();
						if (mc.currentScreen == null) {
							if (!this.fullmap) {
								if (KeyInput.TOGGLE_ZOOM.isKeyPush()) {
									if (this.theMinecraft.gameSettings.keyBindSneak.pressed) {
										this.flagZoom = (this.flagZoom == 0 ? ZOOM_LIST.length : this.flagZoom) - 1;
									} else {
										this.flagZoom = (this.flagZoom + 1) % ZOOM_LIST.length;
									}
								} else if (KeyInput.ZOOM_IN.isKeyPush() && this.flagZoom < ZOOM_LIST.length - 1) {
									++this.flagZoom;
								} else if (KeyInput.ZOOM_OUT.isKeyPush() && this.flagZoom > 0) {
									--this.flagZoom;
								}

								this.targetZoom = ZOOM_LIST[this.flagZoom];
							} else {
								if (KeyInput.TOGGLE_ZOOM.isKeyPush()) {
									if (this.theMinecraft.gameSettings.keyBindSneak.pressed) {
										this.largeZoom = (this.largeZoom == 0 ? ZOOM_LIST.length : this.largeZoom) - 1;
									} else {
										this.largeZoom = (this.largeZoom + 1) % ZOOM_LIST.length;
									}
								} else if (KeyInput.ZOOM_IN.isKeyPush() && this.largeZoom < ZOOM_LIST.length - 1) {
									++this.largeZoom;
								} else if (KeyInput.ZOOM_OUT.isKeyPush() && this.largeZoom > 0) {
									--this.largeZoom;
								}

								this.targetZoom = ZOOM_LIST[this.largeZoom];
							}

							if (KeyInput.TOGGLE_ENABLE.isKeyPush()) {
								this.enable = !this.enable;
								this.stripCounter.reset();
								this.forceUpdate = true;
							}

							if (KeyInput.TOGGLE_RENDER_TYPE.isKeyPush()) {
								if (this.theMinecraft.gameSettings.keyBindSneak.pressed) {
									--this.renderType;
									if (this.renderType < 0) {
										this.renderType = EnumOption.RENDER_TYPE.getValueNum() - 1;
									}

									if (EnumOption.RENDER_TYPE.getValue(this.renderType) == EnumOptionValue.CAVE) {
										--this.renderType;
									}
								} else {
									++this.renderType;
									if (EnumOption.RENDER_TYPE.getValue(this.renderType) == EnumOptionValue.CAVE) {
										++this.renderType;
									}

									if (this.renderType >= EnumOption.RENDER_TYPE.getValueNum()) {
										this.renderType = 0;
									}
								}

								this.stripCounter.reset();
								this.forceUpdate = true;
							}

							if (KeyInput.TOGGLE_WAYPOINTS_DIMENSION.isKeyPush()) {
								if (this.theMinecraft.gameSettings.keyBindSneak.pressed) {
									this.prevDimension();
								} else {
									this.nextDimension();
								}
							}

							if (KeyInput.TOGGLE_WAYPOINTS_VISIBLE.isKeyPush()) {
								this.visibleWaypoints = !this.visibleWaypoints;
							}

							if (KeyInput.TOGGLE_WAYPOINTS_MARKER.isKeyPush()) {
								this.marker = !this.marker;
							}

							if (KeyInput.TOGGLE_LARGE_MAP.isKeyPush()) {
								this.fullmap = !this.fullmap;
								this.currentZoom = this.targetZoom = ZOOM_LIST[this.fullmap ? this.largeZoom : this.flagZoom];
								this.forceUpdate = true;
								this.stripCounter.reset();
								if (this.threading) {
									this.lock.lock();

									try {
										this.stripCounter.reset();
										this.mapCalc(false);
									} finally {
										this.lock.unlock();
									}
								}
							}

							if (KeyInput.TOGGLE_LARGE_MAP_LABEL.isKeyPush() && this.fullmap) {
								this.largeMapLabel = !this.largeMapLabel;
							}

							if (KeyInput.SET_WAYPOINT.isKeyPushUp()) {
								this.waypointDimension = this.currentDimension;
								this.wayPts = (List) this.wayPtsMap.get(this.waypointDimension);
								if (this.wayPts == null) {
									this.wayPts = new ArrayList();
									this.wayPtsMap.put(this.waypointDimension, this.wayPts);
								}

								mc.displayGuiScreen(new GuiWaypointEditorScreen(mc, (Waypoint) null));
							}

							if (KeyInput.WAYPOINT_LIST.isKeyPushUp()) {
								mc.displayGuiScreen(new GuiWaypointScreen((GuiScreen) null));
							}

							if (KeyInput.MENU_KEY.isKeyPush()) {
								mc.displayGuiScreen(new GuiOptionScreen());
							}
						} else if (this.fullmap) {
							this.currentZoom = this.targetZoom = ZOOM_LIST[this.flagZoom];
							this.fullmap = false;
							this.forceUpdate = true;
							this.stripCounter.reset();
						}

						if (EnumOption.RENDER_TYPE.getValue(this.renderType) == EnumOptionValue.CAVE) {
							this.renderType = 0;
						}

						if (this.deathPoint && this.theMinecraft.currentScreen instanceof GuiGameOver && !(this.guiScreen instanceof GuiGameOver)) {
							this.waypointDimension = this.currentDimension;
							this.wayPts = (List) this.wayPtsMap.get(this.currentDimension);
							if (this.wayPts == null) {
								this.wayPts = new ArrayList();
								this.wayPtsMap.put(this.currentDimension, this.wayPts);
							}

							name = "Death Point";
							x = MathHelper.floor_double(this.playerPosX);
							x = MathHelper.floor_double(this.playerPosY);
							y = MathHelper.floor_double(this.playerPosZ);
							Random rng = new Random();
							float r = rng.nextFloat();
							float g = rng.nextFloat();
							float b = rng.nextFloat();
							boolean contains = false;
							Iterator i$ = this.wayPts.iterator();

							while (i$.hasNext()) {
								Waypoint wp = (Waypoint) i$.next();
								if (wp.type == 1 && wp.x == x && wp.y == x && wp.z == y && wp.enable) {
									contains = true;
									break;
								}
							}

							if (!contains) {
								this.wayPts.add(new Waypoint(name, x, x, y, true, r, g, b, 1));
								this.saveWaypoints();
							}
						}

						this.guiScreen = this.theMinecraft.currentScreen;
						if (this.enable && checkGuiScreen(mc.currentScreen)) {
							if (this.threading) {
								if (this.workerThread == null || !this.workerThread.isAlive()) {
									this.workerThread = new Thread(this);
									this.workerThread.setPriority(3 + this.threadPriority);
									this.workerThread.setDaemon(true);
									this.workerThread.start();
								}
							} else {
								this.mapCalc(true);
							}

							if (this.lock.tryLock()) {
								try {
									if (this.isUpdateImage) {
										this.isUpdateImage = false;
										this.texture.setMinFilter(this.filtering);
										this.texture.setMagFilter(this.filtering);
										this.texture.setClampTexture(true);
										this.texture.register();
									}

									this.condition.signal();
								} finally {
									this.lock.unlock();
								}
							}

							this.currentTime = System.nanoTime();
							double elapseTime = (double) (this.currentTime - this.previousTime) * 1.0E-9D;
							this.zoomVisible = (float) ((double) this.zoomVisible - elapseTime);
							if (this.currentZoom != this.targetZoom) {
								double d = Math.max(0.0D, Math.min(1.0D, elapseTime * 4.0D));
								this.currentZoom += (this.targetZoom - this.currentZoom) * d;
								if (Math.abs(this.currentZoom - this.targetZoom) < 5.0E-4D) {
									this.currentZoom = this.targetZoom;
								}

								this.zoomVisible = 3.0F;
							}

							this.previousTime = this.currentTime;
							if (this.texture.getId() != 0) {
								scale = this.fontScale == 0 ? this.scaledResolution.getScaleFactor() + 1 >> 1 : this.fontScale;
								int var10000;
								boolean var89;
								switch (this.mapPosition) {
								case 0:
									x = 1;
									var89 = true;
									break;
								case 1:
									x = 1;
									y = this.scHeight - 37;
									var10000 = y - scale * ((this.showMenuKey | this.showCoordinate ? 2 : 0) + (this.showMenuKey ? 9 : 0) + (this.showCoordinate ? 18 : 0)) / this.scaledResolution.getScaleFactor();
									break;
								case 2:
								default:
									x = this.scWidth - 37;
									var89 = true;
									break;
								case 3:
									x = this.scWidth - 37;
									y = this.scHeight - 37;
									var10000 = y - scale * ((this.showMenuKey | this.showCoordinate ? 2 : 0) + (this.showMenuKey ? 9 : 0) + (this.showCoordinate ? 18 : 0)) / this.scaledResolution.getScaleFactor();
								}

								if (this.fullmap) {
									this.renderFullMap();
								} else if (this.roundmap) {
									this.renderRoundMap();
								} else {
									this.renderSquareMap();
								}
							}
							break label4178;
						}

						return;
					}

					this.scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
					mc.fontRenderer.drawStringWithShadow(this.errorString, this.scaledResolution.getScaledWidth() - mc.fontRenderer.getStringWidth(this.errorString) - 2, 2, -65536);
				} catch (RuntimeException var57) {
					var57.printStackTrace();
					this.errorString = "[Rei's Minimap] ERROR: " + var57.getMessage();
					error("mainloop runtime exception", var57);
					break label4178;
				} finally {
					GL11.glPopMatrix();
					GL11.glPopClientAttrib();
					GL11.glPopAttrib();
				}

				return;
			}

			if (this.count != 0) {
				this.theMinecraft.fontRenderer.drawStringWithShadow(String.format("%12d", this.ntime / (long) this.count), 2, 12, -1);
			}

			Thread.yield();
		} finally {
			;
		}
	}

	public void run() {
		if (this.theMinecraft != null) {
			Thread currentThread = Thread.currentThread();

			while (true) {
				while (!this.enable || currentThread != this.workerThread || !this.threading) {
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException var20) {
						return;
					}

					this.lock.lock();

					label203: {
						try {
							this.condition.await();
							break label203;
						} catch (InterruptedException var21) {
							;
						} finally {
							this.lock.unlock();
						}

						return;
					}

					if (currentThread != this.workerThread) {
						return;
					}
				}

				try {
					if (this.renderType == 0) {
						Thread.sleep((long) (updateFrequencys[updateFrequencys.length - this.updateFrequencySetting - 1] * 2));
					} else {
						Thread.sleep((long) (updateFrequencys[updateFrequencys.length - this.updateFrequencySetting - 1] * 6));
					}
				} catch (InterruptedException var19) {
					return;
				}

				this.lock.lock();

				try {
					this.mapCalc(false);
					if (this.isCompleteImage || this.isUpdateImage) {
						this.condition.await();
					}
					continue;
				} catch (InterruptedException var23) {
					;
				} catch (Exception var24) {
					var24.printStackTrace();
					this.errorString = "[Rei's Minimap] ERROR: " + var24.getMessage();
					error("mainloop runtime exception", var24);
					continue;
				} finally {
					this.lock.unlock();
				}

				return;
			}
		}
	}

	private void startDrawingQuads() {
		this.tessellator.startDrawingQuads();
	}

	private void draw() {
		this.tessellator.draw();
	}

	private void addVertexWithUV(double x, double y, double z, double u, double v) {
		this.tessellator.addVertexWithUV(x, y, z, u, v);
	}

	private void mapCalc(boolean strip) {
		if (this.theWorld != null && this.thePlayer != null) {
			Thread thread = Thread.currentThread();
			double d;
			if (this.stripCounter.count() == 0) {
				this.posX = MathHelper.floor_double(this.playerPosX);
				this.posY = MathHelper.floor_double(this.playerPosY);
				this.posYd = this.playerPosY;
				this.posZ = MathHelper.floor_double(this.playerPosZ);
				this.chunkCoordX = this.thePlayer.chunkCoordX;
				this.chunkCoordZ = this.thePlayer.chunkCoordZ;
				this.skylightSubtracted = this.calculateSkylightSubtracted(this.theWorld.getWorldTime(), 0.0F);
				if (this.lightType == 0) {
					switch (this.lightmap) {
					case 0:
						this.updateLightmap(this.theWorld.getWorldTime(), 0.0F);
						break;
					case 1:
						this.updateLightmap(6000L, 0.0F);
						break;
					case 2:
						this.updateLightmap(18000L, 0.0F);
						break;
					case 3:
						this.updateLightmap(6000L, 0.0F);
					}
				}

				d = Math.toRadians(this.roundmap && !this.fullmap ? (double) (45.0F - this.playerRotationYaw) : (double) (this.notchDirection ? 225 : -45));
				this.sin = (float) Math.sin(d);
				this.cos = (float) Math.cos(d);
				this.grassColor = ColorizerGrass.getGrassColor(0.5D, 1.0D);
				this.foliageColor = ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
				this.foliageColorPine = ColorizerFoliage.getFoliageColorPine();
				this.foliageColorBirch = ColorizerFoliage.getFoliageColorBirch();
			}

			if (this.fullmap) {
				this.stripCountMax1 = 289;
				this.stripCountMax2 = 289;
			} else if (this.currentZoom < this.targetZoom) {
				d = Math.ceil(4.0D / this.currentZoom) * 2.0D + 1.0D;
				this.stripCountMax1 = (int) (d * d);
				d = Math.ceil(4.0D / this.targetZoom) * 2.0D + 1.0D;
				this.stripCountMax2 = (int) (d * d);
			} else {
				d = Math.ceil(4.0D / this.targetZoom) * 2.0D + 1.0D;
				this.stripCountMax1 = this.stripCountMax2 = (int) (d * d);
			}

			if (this.renderType == 1) {
				if (!this.forceUpdate && strip) {
					this.biomeCalcStrip(thread);
				} else {
					this.biomeCalc(thread);
				}
			} else if (this.renderType == 2) {
				if (!this.forceUpdate && strip) {
					this.caveCalcStrip();
				} else {
					this.caveCalc();
				}
			} else if (!this.forceUpdate && strip) {
				this.surfaceCalcStrip(thread);
			} else {
				this.surfaceCalc(thread);
			}

			if (this.isCompleteImage) {
				this.forceUpdate = false;
				this.isCompleteImage = false;
				this.stripCounter.reset();
				this.lastX = this.posX;
				this.lastY = this.posY;
				this.lastZ = this.posZ;
			}

		}
	}

	private void surfaceCalc(Thread thread) {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);

		while (this.stripCounter.count() < limit) {
			Point point = this.stripCounter.next();
			ChunkData chunkData = ChunkData.getChunkData(this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.surfaceCalc(chunkData, thread);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void surfaceCalcStrip(Thread thread) {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);
		int limit2 = updateFrequencys[this.updateFrequencySetting];

		for (int i = 0; i < limit2 && this.stripCounter.count() < limit; ++i) {
			Point point = this.stripCounter.next();
			ChunkData chunkData = ChunkData.getChunkData(this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.surfaceCalc(chunkData, thread);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void surfaceCalc(ChunkData chunkData, Thread thread) {
		if (chunkData != null) {
			Chunk chunk = chunkData.getChunk();
			if (chunk != null && !(chunk instanceof EmptyChunk)) {
				int offsetX = 128 + chunk.xPosition * 16 - this.posX;
				int offsetZ = 128 + chunk.zPosition * 16 - this.posZ;
				boolean slime = this.showSlimeChunk && this.currentDimension == 0 && chunkData.slime;
				PixelColor pixel = new PixelColor(this.transparency);
				ChunkData chunkMinusX = null;
				ChunkData chunkPlusX = null;
				ChunkData chunkMinusZ = null;
				ChunkData chunkPlusZ = null;
				ChunkData cmx = null;
				ChunkData cpx = null;
				ChunkData cmz = null;
				ChunkData cpz = null;
				if (this.undulate) {
					chunkMinusZ = ChunkData.getChunkData(chunk.xPosition, chunk.zPosition - 1);
					chunkPlusZ = ChunkData.getChunkData(chunk.xPosition, chunk.zPosition + 1);
					chunkMinusX = ChunkData.getChunkData(chunk.xPosition - 1, chunk.zPosition);
					chunkPlusX = ChunkData.getChunkData(chunk.xPosition + 1, chunk.zPosition);
				}

				for (int z = 0; z < 16; ++z) {
					int zCoord = offsetZ + z;
					if (zCoord >= 0) {
						if (zCoord >= 256) {
							break;
						}

						if (this.undulate) {
							cmz = z == 0 ? chunkMinusZ : chunkData;
							cpz = z == 15 ? chunkPlusZ : chunkData;
						}

						for (int x = 0; x < 16; ++x) {
							int xCoord = offsetX + x;
							if (xCoord >= 0) {
								if (xCoord >= 256) {
									break;
								}

								pixel.clear();
								int height = !this.omitHeightCalc && !this.heightmap && !this.undulate ? this.worldHeight : Math.min(this.worldHeight, chunk.getHeightValue(x, z));
								int y = this.omitHeightCalc ? Math.min(this.worldHeight, height + 1) : this.worldHeight;
								chunkData.setHeightValue(x, z, (float) height);
								if (y < 0) {
									if (this.transparency) {
										this.texture.setRGB(xCoord, zCoord, 16711935);
									} else {
										this.texture.setRGB(xCoord, zCoord, -16777216);
									}
								} else {
									this.surfaceCalc(chunkData, x, y, z, pixel, (TintType) null, thread);
									float factor;
									float mz;
									if (this.heightmap) {
										factor = this.undulate ? 0.15F : 0.6F;
										double d1 = (double) height - this.posYd;
										mz = (float) Math.log10(Math.abs(d1) * 0.125D + 1.0D) * factor;
										if (d1 >= 0.0D) {
											pixel.red += mz * (1.0F - pixel.red);
											pixel.green += mz * (1.0F - pixel.green);
											pixel.blue += mz * (1.0F - pixel.blue);
										} else {
											mz = Math.abs(mz);
											pixel.red -= mz * pixel.red;
											pixel.green -= mz * pixel.green;
											pixel.blue -= mz * pixel.blue;
										}
									}

									factor = 1.0F;
									if (this.undulate) {
										cmx = x == 0 ? chunkMinusX : chunkData;
										cpx = x == 15 ? chunkPlusX : chunkData;
										float mx = cmx == null ? 0.0F : cmx.getHeightValue(x - 1 & 15, z);
										float px = cpx == null ? 0.0F : cpx.getHeightValue(x + 1 & 15, z);
										mz = cmz == null ? 0.0F : cmz.getHeightValue(x, z - 1 & 15);
										float pz = cpz == null ? 0.0F : cpz.getHeightValue(x, z + 1 & 15);
										factor += Math.max(-4.0F, Math.min(3.0F, (mx - px) * this.sin + (mz - pz) * this.cos)) * 0.14142136F * 0.8F;
									}

									if (slime) {
										pixel.red = (float) ((double) pixel.red * 1.2D);
										pixel.green = (float) ((double) pixel.green * 0.5D);
										pixel.blue = (float) ((double) pixel.blue * 0.5D);
									}

									if (this.showChunkGrid && (x == 0 || z == 0)) {
										pixel.red = (float) ((double) pixel.red * 0.7D);
										pixel.green = (float) ((double) pixel.green * 0.7D);
										pixel.blue = (float) ((double) pixel.blue * 0.7D);
									}

									byte red = ftob(pixel.red * factor);
									byte green = ftob(pixel.green * factor);
									byte blue = ftob(pixel.blue * factor);
									if (this.transparency) {
										this.texture.setRGBA(xCoord, zCoord, red, green, blue, ftob(pixel.alpha));
									} else {
										this.texture.setRGB(xCoord, zCoord, red, green, blue);
									}
								}
							}
						}
					}
				}

			}
		}
	}

	private void biomeCalc(Thread thread) {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);

		while (this.stripCounter.count() < limit) {
			Point point = this.stripCounter.next();
			ChunkData chunkData = ChunkData.getChunkData(this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.biomeCalc(chunkData, thread);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void biomeCalcStrip(Thread thread) {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);
		int limit2 = updateFrequencys[this.updateFrequencySetting];

		for (int i = 0; i < limit2 && this.stripCounter.count() < limit; ++i) {
			Point point = this.stripCounter.next();
			ChunkData chunkData = ChunkData.getChunkData(this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.biomeCalc(chunkData, thread);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void biomeCalc(ChunkData chunkData, Thread thread) {
		if (chunkData != null) {
			int offsetX = 128 + chunkData.xPosition * 16 - this.posX;
			int offsetZ = 128 + chunkData.zPosition * 16 - this.posZ;

			for (int z = 0; z < 16; ++z) {
				int zCoord = z + offsetZ;
				if (zCoord >= 0) {
					if (zCoord >= 256) {
						break;
					}

					for (int x = 0; x < 16; ++x) {
						int xCoord = x + offsetX;
						if (xCoord >= 0) {
							if (xCoord >= 256) {
								break;
							}

							BiomeGenBase bgb = chunkData.biomes[z << 4 | x];
							int color = bgb != null ? bgb.color : BiomeGenBase.plains.color;
							byte r = (byte) (color >> 16);
							byte g = (byte) (color >> 8);
							byte b = (byte) (color >> 0);
							this.texture.setRGB(xCoord, zCoord, r, g, b);
						}
					}
				}
			}

		}
	}

	private void temperatureCalc(Thread thread) {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);

		while (this.stripCounter.count() < limit) {
			Point point = this.stripCounter.next();
			ChunkData chunkData = ChunkData.getChunkData(this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.temperatureCalc(chunkData, thread);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void temperatureCalcStrip(Thread thread) {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);
		int limit2 = updateFrequencys[this.updateFrequencySetting];

		for (int i = 0; i < limit2 && this.stripCounter.count() < limit; ++i) {
			Point point = this.stripCounter.next();
			ChunkData chunkData = ChunkData.getChunkData(this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.temperatureCalc(chunkData, thread);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void temperatureCalc(ChunkData chunkData, Thread thread) {
		if (chunkData != null) {
			int offsetX = 128 + chunkData.xPosition * 16 - this.posX;
			int offsetZ = 128 + chunkData.zPosition * 16 - this.posZ;

			for (int z = 0; z < 16; ++z) {
				int zCoord = z + offsetZ;
				if (zCoord >= 0) {
					if (zCoord >= 256) {
						break;
					}

					for (int x = 0; x < 16; ++x) {
						int xCoord = x + offsetX;
						if (xCoord >= 0) {
							if (xCoord >= 256) {
								break;
							}

							float temperature = chunkData.biomes[z << 4 | x].temperature;
							int rgb = (int) (temperature * 255.0F);
							this.texture.setRGB(xCoord, zCoord, this.temperatureColor[rgb]);
						}
					}
				}
			}

		}
	}

	private void humidityCalc(Thread thread) {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);

		while (this.stripCounter.count() < limit) {
			Point point = this.stripCounter.next();
			ChunkData chunkData = ChunkData.getChunkData(this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.humidityCalc(chunkData, thread);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void humidityCalcStrip(Thread thread) {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);
		int limit2 = updateFrequencys[this.updateFrequencySetting];

		for (int i = 0; i < limit2 && this.stripCounter.count() < limit; ++i) {
			Point point = this.stripCounter.next();
			ChunkData chunkData = ChunkData.getChunkData(this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.humidityCalc(chunkData, thread);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void humidityCalc(ChunkData chunkData, Thread thread) {
		if (chunkData != null) {
			int offsetX = 128 + chunkData.xPosition * 16 - this.posX;
			int offsetZ = 128 + chunkData.zPosition * 16 - this.posZ;

			for (int z = 0; z < 16; ++z) {
				int zCoord = z + offsetZ;
				if (zCoord >= 0) {
					if (zCoord >= 256) {
						break;
					}

					for (int x = 0; x < 16; ++x) {
						int xCoord = x + offsetX;
						if (xCoord >= 0) {
							if (xCoord >= 256) {
								break;
							}

							float humidity = chunkData.biomes[z << 4 | x].rainfall;
							int rgb = (int) (humidity * 255.0F);
							this.texture.setRGB(xCoord, zCoord, this.humidityColor[rgb]);
						}
					}
				}
			}

		}
	}

	private static final byte ftob(float f) {
		return (byte) Math.max(0, Math.min(255, (int) (f * 255.0F)));
	}

	private void surfaceCalc(ChunkData chunkData, int x, int y, int z, PixelColor pixel, TintType tintType, Thread thread) {
		Chunk chunk = chunkData.getChunk();
		int blockID = chunk.getBlockID(x, y, z);
		if (blockID == 0 || this.hideSnow && blockID == 78) {
			if (y > 0) {
				this.surfaceCalc(chunkData, x, y - 1, z, pixel, (TintType) null, thread);
			}

		} else {
			int metadata = BlockColor.useMetadata(blockID) ? chunk.getBlockMetadata(x, y, z) : 0;
			BlockColor color = BlockColor.getBlockColor(blockID, metadata);
			if (this.transparency) {
				if (color.alpha < 1.0F && y > 0) {
					this.surfaceCalc(chunkData, x, y - 1, z, pixel, color.tintType, thread);
					if (color.alpha == 0.0F) {
						return;
					}
				}
			} else if (color.alpha == 0.0F && y > 0) {
				this.surfaceCalc(chunkData, x, y - 1, z, pixel, color.tintType, thread);
				return;
			}

			int argb;
			float lr;
			float lg;
			float lb;
			int skyLight;
			if (this.lightType == 0) {
				skyLight = 1;
				switch (this.lightmap) {
				case 3:
					skyLight = 15;
					break;
				default:
					this.lightmap = 0;
				case 0:
				case 1:
				case 2:
					skyLight = y < this.worldHeight ? chunk.getSavedLightValue(EnumSkyBlock.Sky, x, y + 1, z) : 15;
				}

				int blockLight = Math.max(Block.lightValue[blockID], chunk.getSavedLightValue(EnumSkyBlock.Block, x, Math.min(this.worldHeight, y + 1), z));
				argb = skyLight << 4 | blockLight;
				lr = this.lightmapRed[argb];
				lg = this.lightmapGreen[argb];
				lb = this.lightmapBlue[argb];
				if (color.tintType == TintType.WATER && tintType == TintType.WATER) {
					return;
				}

				if (this.environmentColor) {
					switch (color.tintType) {
					case GRASS:
						argb = chunkData.smoothGrassColors[z << 4 | x];
						pixel.composite(color.alpha, argb, lr * color.red, lg * color.green, lb * color.blue);
						return;
					case TALL_GRASS:
						long l = (long) (x * 3129871 + z * 6129781 + y);
						l = l * l * 42317861L + l * 11L;
						int _x = (int) ((long) x + ((l >> 14 & 31L) - 16L));
						int _z = (int) ((long) z + ((l >> 24 & 31L) - 16L));
						argb = chunkData.grassColors[z << 4 | x];
						pixel.composite(color.alpha, argb, lr * color.red, lg * color.green, lb * color.blue);
						return;
					case FOLIAGE:
						argb = chunkData.smoothFoliageColors[z << 4 | x];
						pixel.composite(color.alpha, argb, lr * color.red, lg * color.green, lb * color.blue);
						return;
					case WATER:
						argb = chunkData.smoothWaterColors[z << 4 | x];
						pixel.composite(color.alpha, argb, lr * color.red, lg * color.green, lb * color.blue);
						return;
					case TF_OAK:
						argb = chunkData.smoothFoliageColors[z << 4 | x];
						pixel.composite(color.alpha, argb, lr * color.red, lg * color.green, lb * color.blue);
						return;
					case TF_CANOPY:
						argb = chunkData.smoothFoliageColors[z << 4 | x];
						argb = (argb & 16711422) + 4627046 >> 1;
						pixel.composite(color.alpha, argb, lr * color.red, lg * color.green, lb * color.blue);
						return;
					}
				} else {
					switch (color.tintType) {
					case GRASS:
						pixel.composite(color.alpha, this.grassColor, lr * color.red, lg * color.green, lb * color.blue);
						return;
					case TALL_GRASS:
						pixel.composite(color.alpha, this.grassColor, lr * color.red * 0.9F, lg * color.green * 0.9F, lb * color.blue * 0.9F);
						return;
					case FOLIAGE:
						pixel.composite(color.alpha, this.foliageColor, lr * color.red, lg * color.green, lb * color.blue);
						return;
					case WATER:
					default:
						break;
					case TF_OAK:
						pixel.composite(color.alpha, this.tfOakColor, lr * color.red, lg * color.green, lb * color.blue);
						return;
					case TF_CANOPY:
						pixel.composite(color.alpha, this.tfCanopyColor, lr * color.red, lg * color.green, lb * color.blue);
						return;
					}
				}

				if (color.tintType == TintType.PINE) {
					pixel.composite(color.alpha, this.foliageColorPine, lr * color.red, lg * color.green, lb * color.blue);
					return;
				}

				if (color.tintType == TintType.BIRCH) {
					pixel.composite(color.alpha, this.foliageColorBirch, lr * color.red, lg * color.green, lb * color.blue);
					return;
				}

				if (color.tintType == TintType.TF_MANGROVE) {
					pixel.composite(color.alpha, this.tfMangroveColor, lr * color.red, lg * color.green, lb * color.blue);
					return;
				}

				if (color.tintType == TintType.GLASS && tintType == TintType.GLASS) {
					return;
				}

				pixel.composite(color.alpha, color.red * lr, color.green * lg, color.blue * lb);
			} else {
				switch (this.lightmap) {
				case 1:
					skyLight = y < this.worldHeight ? chunk.getBlockLightValue(x, y + 1, z, 0) : 15;
					break;
				case 2:
					skyLight = y < this.worldHeight ? chunk.getBlockLightValue(x, y + 1, z, 11) : 4;
					break;
				case 3:
					skyLight = 15;
					break;
				default:
					this.lightmap = 0;
				case 0:
					skyLight = y < this.worldHeight ? chunk.getBlockLightValue(x, y + 1, z, this.skylightSubtracted) : 15 - this.skylightSubtracted;
				}

				float lightBrightness = this.lightBrightnessTable[skyLight];
				if (color.tintType == TintType.WATER && tintType == TintType.WATER) {
					return;
				}

				if (this.environmentColor) {
					switch (color.tintType) {
					case GRASS:
						argb = chunkData.smoothGrassColors[z << 4 | x];
						pixel.composite(color.alpha, argb, lightBrightness * 0.6F);
						return;
					case TALL_GRASS:
						long l = (long) (x * 3129871 + z * 6129781 + y);
						l = l * l * 42317861L + l * 11L;
						int _x = (int) ((long) x + ((l >> 14 & 31L) - 16L));
						int _z = (int) ((long) z + ((l >> 24 & 31L) - 16L));
						argb = chunkData.grassColors[z << 4 | x];
						pixel.composite(color.alpha, argb, lightBrightness * 0.5F);
						return;
					case FOLIAGE:
						argb = chunkData.smoothFoliageColors[z << 4 | x];
						pixel.composite(color.alpha, argb, lightBrightness * 0.5F);
						return;
					case WATER:
						argb = chunkData.smoothWaterColors[z << 4 | x];
						lr = (float) (argb >> 16 & 255) * 0.003921569F;
						lg = (float) (argb >> 8 & 255) * 0.003921569F;
						lb = (float) (argb >> 0 & 255) * 0.003921569F;
						pixel.composite(color.alpha, color.red * lr, color.green * lg, color.blue * lb, lightBrightness);
						return;
					case TF_OAK:
						argb = chunkData.smoothFoliageColors[z << 4 | x];
						pixel.composite(color.alpha, argb, lightBrightness * color.red, lightBrightness * color.green, lightBrightness * color.blue);
						return;
					case TF_CANOPY:
						argb = chunkData.smoothFoliageColors[z << 4 | x];
						argb = (argb & 16711422) + 4627046 >> 1;
						pixel.composite(color.alpha, argb, lightBrightness * color.red, lightBrightness * color.green, lightBrightness * color.blue);
						return;
					}
				} else {
					switch (color.tintType) {
					case GRASS:
						pixel.composite(color.alpha, this.grassColor, lightBrightness * color.red, lightBrightness * color.green, lightBrightness * color.blue);
						return;
					case TALL_GRASS:
						pixel.composite(color.alpha, this.grassColor, lightBrightness * color.red * 0.9F, lightBrightness * color.green * 0.9F, lightBrightness * color.blue * 0.9F);
						return;
					case FOLIAGE:
						pixel.composite(color.alpha, this.foliageColor, lightBrightness * color.red, lightBrightness * color.green, lightBrightness * color.blue);
						return;
					case WATER:
					default:
						break;
					case TF_OAK:
						pixel.composite(color.alpha, this.tfOakColor, lightBrightness * color.red, lightBrightness * color.green, lightBrightness * color.blue);
						return;
					case TF_CANOPY:
						pixel.composite(color.alpha, this.tfCanopyColor, lightBrightness * color.red, lightBrightness * color.green, lightBrightness * color.blue);
						return;
					}
				}

				if (color.tintType == TintType.PINE) {
					pixel.composite(color.alpha, this.foliageColorPine, lightBrightness * color.red, lightBrightness * color.green, lightBrightness * color.blue);
					return;
				}

				if (color.tintType == TintType.BIRCH) {
					pixel.composite(color.alpha, this.foliageColorBirch, lightBrightness * color.red, lightBrightness * color.green, lightBrightness * color.blue);
					return;
				}

				if (color.tintType == TintType.TF_MANGROVE) {
					pixel.composite(color.alpha, this.tfMangroveColor, lightBrightness * color.red, lightBrightness * color.green, lightBrightness * color.blue);
					return;
				}

				if (color.tintType == TintType.GLASS && tintType == TintType.GLASS) {
					return;
				}

				pixel.composite(color.alpha, color.red, color.green, color.blue, lightBrightness);
			}

		}
	}

	private void caveCalc() {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);

		while (this.stripCounter.count() < limit) {
			Point point = this.stripCounter.next();
			ChunkData chunkData = ChunkData.getChunkData(this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.caveCalc(chunkData);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void caveCalcStrip() {
		int limit = Math.max(this.stripCountMax1, this.stripCountMax2);
		int limit2 = updateFrequencys[this.updateFrequencySetting];

		for (int i = 0; i < limit2 && this.stripCounter.count() < limit; ++i) {
			Point point = this.stripCounter.next();
			ChunkData chunkData = ChunkData.getChunkData(this.chunkCoordX + point.x, this.chunkCoordZ + point.y);
			this.caveCalc(chunkData);
		}

		this.isUpdateImage = this.stripCounter.count() >= this.stripCountMax1;
		this.isCompleteImage = this.isUpdateImage && this.stripCounter.count() >= this.stripCountMax2;
	}

	private void caveCalc(ChunkData chunkData) {
		if (chunkData != null) {
			Chunk chunk = chunkData.getChunk();
			if (chunk != null && !(chunk instanceof EmptyChunk)) {
				int offsetX = 128 + chunk.xPosition * 16 - this.posX;
				int offsetZ = 128 + chunk.zPosition * 16 - this.posZ;

				for (int z = 0; z < 16; ++z) {
					int zCoord = offsetZ + z;
					if (zCoord >= 0) {
						if (zCoord >= 256) {
							break;
						}

						for (int x = 0; x < 16; ++x) {
							int xCoord = offsetX + x;
							if (xCoord >= 0) {
								if (xCoord >= 256) {
									break;
								}

								float f;
								f = 0.0F;
								int y;
								int _y;
								label138: switch (this.currentDimension) {
								case -1:
									y = 0;

									while (true) {
										if (y >= temp.length) {
											break label138;
										}

										_y = this.posY - y;
										if (_y >= 0 && _y <= this.worldHeight && chunk.getBlockID(x, _y, z) == 0 && chunk.getBlockLightValue(x, _y, z, 12) != 0) {
											f += temp[y];
										}

										_y = this.posY + y + 1;
										if (_y >= 0 && _y <= this.worldHeight && chunk.getBlockID(x, _y, z) == 0 && chunk.getBlockLightValue(x, _y, z, 12) != 0) {
											f += temp[y];
										}

										++y;
									}
								case 0:
									y = 0;

									while (true) {
										if (y >= temp.length) {
											break label138;
										}

										_y = this.posY - y;
										if (_y > this.worldHeight || _y >= 0 && chunk.getBlockID(x, _y, z) == 0 && chunk.getBlockLightValue(x, _y, z, 12) != 0) {
											f += temp[y];
										}

										_y = this.posY + y + 1;
										if (_y > this.worldHeight || _y >= 0 && chunk.getBlockID(x, _y, z) == 0 && chunk.getBlockLightValue(x, _y, z, 12) != 0) {
											f += temp[y];
										}

										++y;
									}
								case 1:
								case 2:
								case 3:
								default:
									for (y = 0; y < temp.length; ++y) {
										_y = this.posY - y;
										if (_y < 0 || _y > this.worldHeight || chunk.getBlockID(x, _y, z) == 0 && chunk.getBlockLightValue(x, _y, z, 12) != 0) {
											f += temp[y];
										}

										_y = this.posY + y + 1;
										if (_y < 0 || _y > this.worldHeight || chunk.getBlockID(x, _y, z) == 0 && chunk.getBlockLightValue(x, _y, z, 12) != 0) {
											f += temp[y];
										}
									}
								}

								f = 0.8F - f;
								this.texture.setRGB(xCoord, zCoord, ftob(0.0F), ftob(f), ftob(0.0F));
							}
						}
					}
				}

			}
		}
	}

	private void renderRoundMap() {
		int mapscale = 1;
		if (this.mapScale == 0) {
			mapscale = this.scaledResolution.getScaleFactor();
		} else if (this.mapScale == 1) {
			while (this.scWidth >= (mapscale + 1) * 320 && this.scHeight >= (mapscale + 1) * 240) {
				++mapscale;
			}
		} else {
			mapscale = this.mapScale - 1;
		}

		int fscale = this.fontScale - 1;
		if (this.fontScale == 0) {
			fscale = this.scaledResolution.getScaleFactor() + 1 >> 1;
		} else if (this.fontScale == 1) {
			fscale = mapscale + 1 >> 1;
		}

		int centerX = (this.mapPosition & 2) == 0 ? 37 * mapscale : this.scWidth - 37 * mapscale;
		int centerY = (this.mapPosition & 1) == 0 ? 37 * mapscale : this.scHeight - 37 * mapscale;
		if ((this.mapPosition & 1) == 1) {
			centerY -= ((this.showMenuKey | this.showCoordinate ? 2 : 0) + (this.showMenuKey ? 9 : 0) + (this.showCoordinate ? 18 : 0)) * fscale;
		}

		GL11.glTranslated((double) centerX, (double) centerY, 0.0D);
		GL11.glScalef((float) mapscale, (float) mapscale, 1.0F);
		GL11.glDisable(3042);
		GL11.glColorMask(false, false, false, false);
		GL11.glEnable(2929);
		if (this.useStencil) {
			GL11.glAlphaFunc(515, 0.1F);
			GL11.glClearStencil(0);
			GL11.glClear(1024);
			GL11.glEnable(2960);
			GL11.glStencilFunc(519, 1, -1);
			GL11.glStencilOp(7680, 7681, 7681);
			GL11.glDepthMask(false);
		} else {
			GL11.glAlphaFunc(516, 0.0F);
			GL11.glDepthMask(true);
		}

		GL11.glPushMatrix();
		GL11.glRotatef(90.0F - this.playerRotationYaw, 0.0F, 0.0F, 1.0F);
		GLTexture.ROUND_MAP_MASK.bind();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawCenteringRectangle(0.0D, 0.0D, 1.01D, 64.0D, 64.0D);
		if (this.useStencil) {
			GL11.glStencilOp(7680, 7680, 7680);
			GL11.glStencilFunc(514, 1, -1);
		}

		GL11.glEnable(3042);
		GL11.glAlphaFunc(516, 0.0F);
		GL11.glBlendFunc(770, 771);
		GL11.glColorMask(true, true, true, true);
		double a = 0.25D / this.currentZoom;
		double slideX = (this.playerPosX - (double) this.lastX) * 0.00390625D;
		double slideY = (this.playerPosZ - (double) this.lastZ) * 0.00390625D;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapOpacity);
		this.texture.bind();
		this.startDrawingQuads();
		this.addVertexWithUV(-32.0D, 32.0D, 1.0D, 0.5D + a + slideX, 0.5D + a + slideY);
		this.addVertexWithUV(32.0D, 32.0D, 1.0D, 0.5D + a + slideX, 0.5D - a + slideY);
		this.addVertexWithUV(32.0D, -32.0D, 1.0D, 0.5D - a + slideX, 0.5D - a + slideY);
		this.addVertexWithUV(-32.0D, -32.0D, 1.0D, 0.5D - a + slideX, 0.5D + a + slideY);
		this.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		double dist;
		int width;
		double distance;
		float tx;
		Iterator i$;
		double wayX;
		double wayZ;
		float width0;
		if (this.visibleEntitiesRadar) {
			dist = this.useStencil ? 34.0D : 29.0D;
			(this.configEntityDirection ? GLTexture.ENTITY2 : GLTexture.ENTITY).bind();

			for (int ve = this.visibleEntities.length - 1; ve >= 0; --ve) {
				int col = this.visibleEntityColor[ve];
				List entityList = this.visibleEntities[ve];
				i$ = entityList.iterator();

				while (i$.hasNext()) {
					EntityLiving entity = (EntityLiving) i$.next();
					width = entity.isEntityAlive() ? col : (col & 16579836) >> 2 | -16777216;
					double entityPosX = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) this.renderPartialTicks;
					double entityPosZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) this.renderPartialTicks;
					wayX = this.playerPosX - entityPosX;
					wayZ = this.playerPosZ - entityPosZ;
					float locate = (float) Math.toDegrees(Math.atan2(wayX, wayZ));
					distance = Math.sqrt(wayX * wayX + wayZ * wayZ) * this.currentZoom * 0.5D;

					try {
						GL11.glPushMatrix();
						if (distance < dist) {
							float r = (float) (width >> 16 & 255) * 0.003921569F;
							float g = (float) (width >> 8 & 255) * 0.003921569F;
							float b = (float) (width & 255) * 0.003921569F;
							float alpha = (float) Math.max(0.20000000298023224D, 1.0D - Math.abs(this.playerPosY - entity.posY) * 0.04D);
							float mul = (float) Math.min(1.0D, Math.max(0.5D, 1.0D - (this.thePlayer.boundingBox.minY - entity.boundingBox.minY) * 0.1D));
							r *= mul;
							g *= mul;
							b *= mul;
							GL11.glColor4f(r, g, b, alpha);
							GL11.glRotatef(-locate - this.playerRotationYaw + 180.0F, 0.0F, 0.0F, 1.0F);
							GL11.glTranslated(0.0D, -distance, 0.0D);
							GL11.glRotatef(-(-locate - this.playerRotationYaw + 180.0F), 0.0F, 0.0F, 1.0F);
							if (this.configEntityDirection) {
								float entityRotationYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * this.renderPartialTicks;
								GL11.glRotatef(entityRotationYaw - this.playerRotationYaw, 0.0F, 0.0F, 1.0F);
							}

							this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
						}
					} finally {
						GL11.glPopMatrix();
					}
				}
			}
		}

		if (this.useStencil) {
			GL11.glDisable(2960);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapOpacity);
		GLTexture.ROUND_MAP.bind();
		this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 64.0D, 64.0D);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (this.visibleWaypoints) {
			dist = this.getVisibleDimensionScale();
			i$ = this.wayPts.iterator();

			while (i$.hasNext()) {
				Waypoint pt = (Waypoint) i$.next();
				if (pt.enable) {
					wayX = this.playerPosX - (double) pt.x * dist - 0.5D;
					wayZ = this.playerPosZ - (double) pt.z * dist - 0.5D;
					width0 = (float) Math.toDegrees(Math.atan2(wayX, wayZ));
					distance = Math.sqrt(wayX * wayX + wayZ * wayZ) * this.currentZoom * 0.5D;

					try {
						GL11.glPushMatrix();
						if (distance < 31.0D) {
							GL11.glColor4f(pt.red, pt.green, pt.blue, (float) Math.min(1.0D, Math.max(0.4D, (distance - 1.0D) * 0.5D)));
							Waypoint.FILE[pt.type].bind();
							GL11.glRotatef(-width0 - this.playerRotationYaw + 180.0F, 0.0F, 0.0F, 1.0F);
							GL11.glTranslated(0.0D, -distance, 0.0D);
							GL11.glRotatef(-(-width0 - this.playerRotationYaw + 180.0F), 0.0F, 0.0F, 1.0F);
							this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
						} else {
							GL11.glColor3f(pt.red, pt.green, pt.blue);
							Waypoint.MARKER[pt.type].bind();
							GL11.glRotatef(-width0 - this.playerRotationYaw + 180.0F, 0.0F, 0.0F, 1.0F);
							GL11.glTranslated(0.0D, -34.0D, 0.0D);
							this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
						}
					} finally {
						GL11.glPopMatrix();
					}
				}
			}
		}

		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		dist = Math.sin(Math.toRadians((double) this.playerRotationYaw)) * 28.0D;
		double c = Math.cos(Math.toRadians((double) this.playerRotationYaw)) * 28.0D;
		if (this.notchDirection) {
			GLTexture.W.bind();
			this.drawCenteringRectangle(c, -dist, 1.0D, 8.0D, 8.0D);
			GLTexture.S.bind();
			this.drawCenteringRectangle(-dist, -c, 1.0D, 8.0D, 8.0D);
			GLTexture.E.bind();
			this.drawCenteringRectangle(-c, dist, 1.0D, 8.0D, 8.0D);
			GLTexture.N.bind();
			this.drawCenteringRectangle(dist, c, 1.0D, 8.0D, 8.0D);
		} else {
			GLTexture.N.bind();
			this.drawCenteringRectangle(c, -dist, 1.0D, 8.0D, 8.0D);
			GLTexture.W.bind();
			this.drawCenteringRectangle(-dist, -c, 1.0D, 8.0D, 8.0D);
			GLTexture.S.bind();
			this.drawCenteringRectangle(-c, dist, 1.0D, 8.0D, 8.0D);
			GLTexture.E.bind();
			this.drawCenteringRectangle(dist, c, 1.0D, 8.0D, 8.0D);
		}

		GL11.glScaled(1.0D / (double) mapscale, 1.0D / (double) mapscale, 1.0D);
		FontRenderer fontRenderer = this.theMinecraft.fontRenderer;
		int alpha = (int) (this.zoomVisible * 255.0F);
		String str;
		int posX;
		int posY;
		if (alpha > 0) {
			str = String.format("%2.2fx", this.currentZoom);
			width = fontRenderer.getStringWidth(str);
			if (alpha > 255) {
				alpha = 255;
			}

			tx = 30 * mapscale - width * fscale;
			posX = 30 * mapscale - 8 * fscale;
			GL11.glTranslatef((float) tx, (float) posX, 0.0F);
			GL11.glScalef((float) fscale, (float) fscale, 1.0F);
			posY = alpha << 24 | 16777215;
			fontRenderer.drawStringWithShadow(str, 0, 0, posY);
			GL11.glScaled(1.0D / (double) fscale, 1.0D / (double) fscale, 1.0D);
			GL11.glTranslatef((float) (-tx), (float) (-posX), 0.0F);
		}

		if (this.visibleWaypoints && this.currentDimension != this.waypointDimension) {
			GL11.glPushMatrix();
			str = this.getDimensionName(this.waypointDimension);
			width0 = (float) fontRenderer.getStringWidth(str) * 0.5F * (float) fscale;
			width0 = (float) (37 * mapscale) < width0 ? (float) (37 * mapscale) - width0 : 0.0F;
			if ((this.mapPosition & 2) == 0) {
				width0 = -width0;
			}
			// TODO: GOVNO
			GL11.glTranslated((double) (width0 - width0), (double) (-30 * mapscale), 0.0D);
			GL11.glScaled((double) fscale, (double) fscale, 1.0D);
			fontRenderer.drawStringWithShadow(str, 0, 0, 16777215);
			GL11.glPopMatrix();
		}

		int ty = 32 * mapscale;
		String line1;
		float width1;
		if (this.showCoordinate) {
			String line2;
			if (this.coordinateType == 0) {
				posX = MathHelper.floor_double(this.playerPosX);
				posY = MathHelper.floor_double(this.thePlayer.boundingBox.minY);
				int posZ = MathHelper.floor_double(this.playerPosZ);
				line1 = String.format("%+d, %+d", posX, posZ);
				line2 = Integer.toString(posY);
			} else {
				line1 = String.format("%+1.2f, %+1.2f", this.playerPosX, this.playerPosZ);
				line2 = String.format("%1.2f (%d)", this.playerPosY, (int) this.thePlayer.boundingBox.minY);
			}

			width1 = (float) fontRenderer.getStringWidth(line1) * 0.5F * (float) fscale;
			float width2 = (float) fontRenderer.getStringWidth(line2) * 0.5F * (float) fscale;
			tx = (float) (37 * mapscale) < width1 ? (float) (37 * mapscale) - width1 : 0.0F;
			if ((this.mapPosition & 2) == 0) {
				tx = -tx;
			}

			GL11.glTranslatef(tx - width1, (float) ty, 0.0F);
			GL11.glScalef((float) fscale, (float) fscale, 1.0F);
			fontRenderer.drawStringWithShadow(line1, 0, 2, 16777215);
			GL11.glScaled(1.0D / (double) fscale, 1.0D / (double) fscale, 1.0D);
			GL11.glTranslatef(width1 - width2, 0.0F, 0.0F);
			GL11.glScalef((float) fscale, (float) fscale, 1.0F);
			fontRenderer.drawStringWithShadow(line2, 0, 11, 16777215);
			GL11.glScaled(1.0D / (double) fscale, 1.0D / (double) fscale, 1.0D);
			GL11.glTranslatef(width2 - tx, (float) (-ty), 0.0F);
			ty += 18 * fscale;
		}

		if (this.showMenuKey) {
			line1 = String.format("Menu: %s key", KeyInput.MENU_KEY.getKeyName());
			width1 = (float) this.theMinecraft.fontRenderer.getStringWidth(line1) * 0.5F * (float) fscale;
			width1 = (float) (32 * mapscale) - width1;
			if ((this.mapPosition & 2) == 0 && (float) (32 * mapscale) < width1) {
				width1 = (float) (-32 * mapscale) + width1;
			}

			GL11.glTranslatef(width1 - width1, (float) ty, 0.0F);
			GL11.glScalef((float) fscale, (float) fscale, 1.0F);
			fontRenderer.drawStringWithShadow(line1, 0, 2, 16777215);
			GL11.glScaled(1.0D / (double) fscale, 1.0D / (double) fscale, 1.0D);
			GL11.glTranslatef(width1 - width1, (float) (-ty), 0.0F);
		}

		GL11.glDepthMask(true);
		GL11.glEnable(2929);
	}

	private void renderSquareMap() {
		int mapscale = 1;
		if (this.mapScale == 0) {
			mapscale = this.scaledResolution.getScaleFactor();
		} else if (this.mapScale == 1) {
			while (this.scWidth >= (mapscale + 1) * 320 && this.scHeight >= (mapscale + 1) * 240) {
				++mapscale;
			}
		} else {
			mapscale = this.mapScale - 1;
		}

		int fscale = this.fontScale - 1;
		if (this.fontScale == 0) {
			fscale = this.scaledResolution.getScaleFactor() + 1 >> 1;
		} else if (this.fontScale == 1) {
			fscale = mapscale + 1 >> 1;
		}

		int centerX = (this.mapPosition & 2) == 0 ? 37 * mapscale : this.scWidth - 37 * mapscale;
		int centerY = (this.mapPosition & 1) == 0 ? 37 * mapscale : this.scHeight - 37 * mapscale;
		if ((this.mapPosition & 1) == 1) {
			centerY -= ((this.showMenuKey | this.showCoordinate ? 2 : 0) + (this.showMenuKey ? 9 : 0) + (this.showCoordinate ? 18 : 0)) * fscale;
		}

		GL11.glTranslated((double) centerX, (double) centerY, 0.0D);
		GL11.glScalef((float) mapscale, (float) mapscale, 1.0F);
		GL11.glDisable(3042);
		GL11.glColorMask(false, false, false, false);
		GL11.glEnable(2929);
		if (this.useStencil) {
			GL11.glAlphaFunc(515, 0.1F);
			GL11.glClearStencil(0);
			GL11.glClear(1024);
			GL11.glEnable(2960);
			GL11.glStencilFunc(519, 1, -1);
			GL11.glStencilOp(7680, 7681, 7681);
			GL11.glDepthMask(false);
		} else {
			GL11.glAlphaFunc(516, 0.0F);
			GL11.glDepthMask(true);
		}

		GLTexture.SQUARE_MAP_MASK.bind();
		this.drawCenteringRectangle(0.0D, 0.0D, 1.001D, 64.0D, 64.0D);
		if (this.useStencil) {
			GL11.glStencilOp(7680, 7680, 7680);
			GL11.glStencilFunc(514, 1, -1);
		}

		GL11.glEnable(3042);
		GL11.glAlphaFunc(516, 0.0F);
		GL11.glBlendFunc(770, 771);
		GL11.glColorMask(true, true, true, true);
		GL11.glDepthMask(true);
		double a = 0.25D / this.currentZoom;
		double slideX = (this.playerPosX - (double) this.lastX) * 0.00390625D;
		double slideY = (this.playerPosZ - (double) this.lastZ) * 0.00390625D;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapOpacity);
		this.texture.bind();
		this.startDrawingQuads();
		if (this.notchDirection) {
			this.addVertexWithUV(32.0D, 32.0D, 1.0D, 0.5D + a + slideX, 0.5D + a + slideY);
			this.addVertexWithUV(32.0D, -32.0D, 1.0D, 0.5D + a + slideX, 0.5D - a + slideY);
			this.addVertexWithUV(-32.0D, -32.0D, 1.0D, 0.5D - a + slideX, 0.5D - a + slideY);
			this.addVertexWithUV(-32.0D, 32.0D, 1.0D, 0.5D - a + slideX, 0.5D + a + slideY);
		} else {
			this.addVertexWithUV(-32.0D, 32.0D, 1.0D, 0.5D + a + slideX, 0.5D + a + slideY);
			this.addVertexWithUV(32.0D, 32.0D, 1.0D, 0.5D + a + slideX, 0.5D - a + slideY);
			this.addVertexWithUV(32.0D, -32.0D, 1.0D, 0.5D - a + slideX, 0.5D - a + slideY);
			this.addVertexWithUV(-32.0D, -32.0D, 1.0D, 0.5D - a + slideX, 0.5D + a + slideY);
		}

		this.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int alphai;
		int ty;
		int posY;
		double d;
		double t;
		double hypot;
		if (this.visibleEntitiesRadar) {
			float dist = this.useStencil ? 34.0F : 31.0F;
			(this.configEntityDirection ? GLTexture.ENTITY2 : GLTexture.ENTITY).bind();

			double doub;
			for (alphai = this.visibleEntities.length - 1; alphai >= 0; --alphai) {
				ty = this.visibleEntityColor[alphai];
				List entityList = this.visibleEntities[alphai];
				Iterator i$ = entityList.iterator();

				while (i$.hasNext()) {
					Entity entity = (EntityLiving) i$.next();
					posY = entity.isEntityAlive() ? ty : (ty & 16579836) >> 2 | -16777216;
					doub = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) this.renderPartialTicks;
					doub = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) this.renderPartialTicks;
					t = this.playerPosX - doub;
					hypot = this.playerPosZ - doub;
					t = t * this.currentZoom * 0.5D;
					hypot = hypot * this.currentZoom * 0.5D;
					doub = Math.max(Math.abs(t), Math.abs(hypot));

					try {
						GL11.glPushMatrix();
						if (doub < (double) dist) {
							float r = (float) (posY >> 16 & 255) * 0.003921569F;
							float g = (float) (posY >> 8 & 255) * 0.003921569F;
							float b = (float) (posY & 255) * 0.003921569F;
							float alpha = (float) Math.max(0.20000000298023224D, 1.0D - Math.abs(this.playerPosY - entity.posY) * 0.04D);
							float mul = (float) Math.min(1.0D, Math.max(0.5D, 1.0D - (this.thePlayer.boundingBox.minY - entity.boundingBox.minY) * 0.1D));
							r *= mul;
							g *= mul;
							b *= mul;
							GL11.glColor4f(r, g, b, alpha);
							float drawRotate = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * this.renderPartialTicks;
							double drawX;
							double drawY;
							if (this.notchDirection) {
								drawX = -t;
								drawY = -hypot;
								drawRotate += 180.0F;
							} else {
								drawX = hypot;
								drawY = -t;
								drawRotate -= 90.0F;
							}

							if (this.configEntityDirection) {
								GL11.glTranslated(drawX, drawY, 0.0D);
								GL11.glRotatef(drawRotate, 0.0F, 0.0F, 1.0F);
								GL11.glTranslated(-drawX, -drawY, 0.0D);
							}

							this.drawCenteringRectangle(drawX, drawY, 1.0D, 8.0D, 8.0D);
						}
					} finally {
						GL11.glPopMatrix();
					}
				}
			}
		}

		if (this.useStencil) {
			GL11.glDisable(2960);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.mapOpacity);
		GLTexture.SQUARE_MAP.bind();
		this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 64.0D, 64.0D);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (this.visibleWaypoints) {
			double scale = this.getVisibleDimensionScale();
			Iterator i$ = this.wayPts.iterator();

			while (i$.hasNext()) {
				Waypoint pt = (Waypoint) i$.next();
				if (pt.enable) {
					double wayX = this.playerPosX - (double) pt.x * scale - 0.5D;
					double wayZ = this.playerPosZ - (double) pt.z * scale - 0.5D;
					wayX = wayX * this.currentZoom * 0.5D;
					wayZ = wayZ * this.currentZoom * 0.5D;
					float locate = (float) Math.toDegrees(Math.atan2(wayX, wayZ));
					d = Math.max(Math.abs(wayX), Math.abs(wayZ));

					try {
						GL11.glPushMatrix();
						if (d < 31.0D) {
							GL11.glColor4f(pt.red, pt.green, pt.blue, (float) Math.min(1.0D, Math.max(0.4D, (d - 1.0D) * 0.5D)));
							Waypoint.FILE[pt.type].bind();
							if (this.notchDirection) {
								this.drawCenteringRectangle(-wayX, -wayZ, 1.0D, 8.0D, 8.0D);
							} else {
								this.drawCenteringRectangle(wayZ, -wayX, 1.0D, 8.0D, 8.0D);
							}
						} else {
							t = 34.0D / d;
							wayX *= t;
							wayZ *= t;
							hypot = Math.sqrt(wayX * wayX + wayZ * wayZ);
							GL11.glColor3f(pt.red, pt.green, pt.blue);
							Waypoint.MARKER[pt.type].bind();
							GL11.glRotatef((this.notchDirection ? 0.0F : 90.0F) - locate, 0.0F, 0.0F, 1.0F);
							GL11.glTranslated(0.0D, -hypot, 0.0D);
							this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
						}
					} finally {
						GL11.glPopMatrix();
					}
				}
			}
		}

		try {
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			GL11.glPushMatrix();
			GLTexture.MMARROW.bind();
			GL11.glRotatef(this.playerRotationYaw - (this.notchDirection ? 180.0F : 90.0F), 0.0F, 0.0F, 1.0F);
			this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
		} catch (Exception var62) {
			;
		} finally {
			GL11.glPopMatrix();
		}

		GL11.glScaled(1.0D / (double) mapscale, 1.0D / (double) mapscale, 1.0D);
		FontRenderer fontRenderer = this.theMinecraft.fontRenderer;
		alphai = (int) (this.zoomVisible * 255.0F);
		String str;
		int posX;
		if (alphai > 0) {
			str = String.format("%2.2fx", this.currentZoom);
			int width = fontRenderer.getStringWidth(str);
			if (alphai > 255) {
				alphai = 255;
			}

			int tx = 30 * mapscale - width * fscale;
			posX = 30 * mapscale - 8 * fscale;
			GL11.glTranslatef((float) tx, (float) posX, 0.0F);
			GL11.glScalef((float) fscale, (float) fscale, 1.0F);
			posY = alphai << 24 | 16777215;
			fontRenderer.drawStringWithShadow(str, 0, 0, posY);
			GL11.glScaled(1.0D / (double) fscale, 1.0D / (double) fscale, 1.0D);
			GL11.glTranslatef((float) (-tx), (float) (-posX), 0.0F);
		}

		float width;
		if (this.visibleWaypoints && this.currentDimension != this.waypointDimension) {
			GL11.glPushMatrix();
			str = this.getDimensionName(this.waypointDimension);
			float width0 = (float) fontRenderer.getStringWidth(str) * 0.5F * (float) fscale;
			width = (float) (37 * mapscale) < width0 ? (float) (37 * mapscale) - width0 : 0.0F;
			if ((this.mapPosition & 2) == 0) {
				width0 = -width0;
			}

			GL11.glTranslated((double) (width - width0), (double) (-30 * mapscale), 0.0D);
			GL11.glScaled((double) fscale, (double) fscale, 1.0D);
			fontRenderer.drawStringWithShadow(str, 0, 0, 16777215);
			GL11.glPopMatrix();
		}

		ty = 32 * mapscale;
		String line1;
		float width1;
		if (this.showCoordinate) {
			String line2;
			if (this.coordinateType == 0) {
				posX = MathHelper.floor_double(this.playerPosX);
				posY = MathHelper.floor_double(this.thePlayer.boundingBox.minY);
				int posZ = MathHelper.floor_double(this.playerPosZ);
				line1 = String.format("%+d, %+d", posX, posZ);
				line2 = Integer.toString(posY);
			} else {
				line1 = String.format("%+1.2f, %+1.2f", this.playerPosX, this.playerPosZ);
				line2 = String.format("%1.2f (%d)", this.playerPosY, (int) this.thePlayer.boundingBox.minY);
			}

			width1 = (float) fontRenderer.getStringWidth(line1) * 0.5F * (float) fscale;
			float width2 = (float) fontRenderer.getStringWidth(line2) * 0.5F * (float) fscale;
			float tx = (float) (37 * mapscale) < width1 ? (float) (37 * mapscale) - width1 : 0.0F;
			if ((this.mapPosition & 2) == 0) {
				tx = -tx;
			}

			GL11.glTranslatef(tx - width1, (float) ty, 0.0F);
			GL11.glScalef((float) fscale, (float) fscale, 1.0F);
			fontRenderer.drawStringWithShadow(line1, 0, 2, 16777215);
			GL11.glScaled(1.0D / (double) fscale, 1.0D / (double) fscale, 1.0D);
			GL11.glTranslatef(width1 - width2, 0.0F, 0.0F);
			GL11.glScalef((float) fscale, (float) fscale, 1.0F);
			fontRenderer.drawStringWithShadow(line2, 0, 11, 16777215);
			GL11.glScaled(1.0D / (double) fscale, 1.0D / (double) fscale, 1.0D);
			GL11.glTranslatef(width2 - tx, (float) (-ty), 0.0F);
			ty += 18 * fscale;
		}

		if (this.showMenuKey) {
			line1 = String.format("Menu: %s key", KeyInput.MENU_KEY.getKeyName());
			width = (float) this.theMinecraft.fontRenderer.getStringWidth(line1) * 0.5F * (float) fscale;
			width1 = (float) (32 * mapscale) - width;
			if ((this.mapPosition & 2) == 0 && (float) (32 * mapscale) < width) {
				width1 = (float) (-32 * mapscale) + width;
			}

			GL11.glTranslatef(width1 - width, (float) ty, 0.0F);
			GL11.glScalef((float) fscale, (float) fscale, 1.0F);
			fontRenderer.drawStringWithShadow(line1, 0, 2, 16777215);
			GL11.glScaled(1.0D / (double) fscale, 1.0D / (double) fscale, 1.0D);
			GL11.glTranslatef(width - width1, (float) (-ty), 0.0F);
		}

		GL11.glDepthMask(true);
		GL11.glEnable(2929);
	}

	private void renderFullMap() {
		int mapscale = 1;
		int fscale;
		if (this.largeMapScale == 0) {
			mapscale = this.scaledResolution.getScaleFactor();
		} else {
			for (fscale = this.largeMapScale == 1 ? 1000 : this.largeMapScale - 1; mapscale < fscale && this.scWidth >= (mapscale + 1) * 240 && this.scHeight >= (mapscale + 1) * 240; ++mapscale) {
				;
			}
		}

		fscale = this.fontScale - 1;
		if (this.fontScale == 0) {
			fscale = this.scaledResolution.getScaleFactor() + 1 >> 1;
		} else if (this.fontScale == 1) {
			fscale = mapscale + 1 >> 1;
		}

		GL11.glTranslated((double) this.scWidth * 0.5D, (double) this.scHeight * 0.5D, 0.0D);
		GL11.glScalef((float) mapscale, (float) mapscale, 0.0F);
		double a = 0.234375D / this.currentZoom;
		double slideX = (this.playerPosX - (double) this.lastX) * 0.00390625D;
		double slideY = (this.playerPosZ - (double) this.lastZ) * 0.00390625D;
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(false);
		GL11.glDisable(2929);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.texture.bind();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, this.largeMapOpacity);
		this.startDrawingQuads();
		if (this.notchDirection) {
			this.addVertexWithUV(120.0D, 120.0D, 1.0D, 0.5D + a + slideX, 0.5D + a + slideY);
			this.addVertexWithUV(120.0D, -120.0D, 1.0D, 0.5D + a + slideX, 0.5D - a + slideY);
			this.addVertexWithUV(-120.0D, -120.0D, 1.0D, 0.5D - a + slideX, 0.5D - a + slideY);
			this.addVertexWithUV(-120.0D, 120.0D, 1.0D, 0.5D - a + slideX, 0.5D + a + slideY);
		} else {
			this.addVertexWithUV(-120.0D, 120.0D, 1.0D, 0.5D + a + slideX, 0.5D + a + slideY);
			this.addVertexWithUV(120.0D, 120.0D, 1.0D, 0.5D + a + slideX, 0.5D - a + slideY);
			this.addVertexWithUV(120.0D, -120.0D, 1.0D, 0.5D - a + slideX, 0.5D - a + slideY);
			this.addVertexWithUV(-120.0D, -120.0D, 1.0D, 0.5D - a + slideX, 0.5D + a + slideY);
		}

		this.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int width;
		int height;
		int posZ;
		double wayZ;
		double drawX;
		double rx;
		Iterator i$;
		double wayX;
		float alpha;
		if (this.visibleEntitiesRadar) {
			(this.configEntityDirection ? GLTexture.ENTITY2 : GLTexture.ENTITY).bind();

			for (width = this.visibleEntities.length - 1; width >= 0; --width) {
				height = this.visibleEntityColor[width];
				List entityList = this.visibleEntities[width];
				i$ = entityList.iterator();

				while (i$.hasNext()) {
					EntityLiving entity = (EntityLiving) i$.next();
					posZ = entity.isEntityAlive() ? height : (height & 16579836) >> 2 | -16777216;
					wayZ = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) this.renderPartialTicks;
					double entityPosZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) this.renderPartialTicks;
					wayX = this.playerPosX - wayZ;
					wayZ = this.playerPosZ - entityPosZ;
					wayX = wayX * this.currentZoom * 2.0D;
					wayZ = wayZ * this.currentZoom * 2.0D;
					double d = Math.max(Math.abs(wayX), Math.abs(wayZ));

					try {
						GL11.glPushMatrix();
						if (d < 114.0D) {
							float r = (float) (posZ >> 16 & 255) * 0.003921569F;
							float g = (float) (posZ >> 8 & 255) * 0.003921569F;
							float b = (float) (posZ & 255) * 0.003921569F;
							alpha = (float) Math.max(0.20000000298023224D, 1.0D - Math.abs(this.playerPosY - entity.posY) * 0.04D);
							float mul = (float) Math.min(1.0D, Math.max(0.5D, 1.0D - (this.thePlayer.boundingBox.minY - entity.boundingBox.minY) * 0.1D));
							r *= mul;
							g *= mul;
							b *= mul;
							GL11.glColor4f(r, g, b, alpha);
							float drawRotate = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * this.renderPartialTicks;
							double drawY;
							if (this.notchDirection) {
								drawX = -wayX;
								drawY = -wayZ;
								drawRotate += 180.0F;
							} else {
								drawX = wayZ;
								drawY = -wayX;
								drawRotate -= 90.0F;
							}

							if (this.configEntityDirection) {
								GL11.glTranslated(drawX, drawY, 0.0D);
								GL11.glRotatef(drawRotate, 0.0F, 0.0F, 1.0F);
								GL11.glTranslated(-drawX, -drawY, 0.0D);
							}

							this.drawCenteringRectangle(drawX, drawY, 1.0D, 8.0D, 8.0D);
						}
					} finally {
						GL11.glPopMatrix();
					}
				}
			}
		}

		try {
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			GL11.glPushMatrix();
			GLTexture.MMARROW.bind();
			GL11.glRotatef(this.playerRotationYaw - (this.notchDirection ? 180.0F : 90.0F), 0.0F, 0.0F, 1.0F);
			this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
		} catch (Exception var60) {
			;
		} finally {
			GL11.glPopMatrix();
		}

		if (this.visibleWaypoints) {
			i$ = this.wayPts.iterator();

			while (i$.hasNext()) {
				Waypoint pt = (Waypoint) i$.next();
				wayX = this.getVisibleDimensionScale();
				if (pt.enable) {
					wayZ = this.playerPosX - (double) pt.x * wayX - 0.5D;
					wayZ = this.playerPosZ - (double) pt.z * wayX - 0.5D;
					wayZ = wayZ * this.currentZoom * 2.0D;
					wayZ = wayZ * this.currentZoom * 2.0D;
					alpha = (float) Math.toDegrees(Math.atan2(wayZ, wayZ));
					drawX = Math.max(Math.abs(wayZ), Math.abs(wayZ));

					try {
						GL11.glPushMatrix();
						double ry;
						if (drawX < 114.0D) {
							GL11.glColor4f(pt.red, pt.green, pt.blue, (float) Math.min(1.0D, Math.max(0.4D, (drawX - 1.0D) * 0.5D)));
							Waypoint.FILE[pt.type].bind();
							if (this.notchDirection) {
								rx = -wayZ;
								ry = -wayZ;
							} else {
								rx = wayZ;
								ry = -wayZ;
							}

							this.drawCenteringRectangle(rx, ry, 1.0D, 8.0D, 8.0D);
							if (this.largeMapLabel && pt.name != null && !pt.name.isEmpty()) {
								GL11.glDisable(3553);
								GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.627451F);
								int width0 = this.theMinecraft.fontRenderer.getStringWidth(pt.name);
								int _x = (int) rx;
								int _y = (int) ry;
								int x1 = _x - (width0 >> 1);
								int x2 = x1 + width0;
								int y1 = _y - 15;
								int y2 = _y - 5;
								this.tessellator.startDrawingQuads();
								this.tessellator.addVertex((double) (x1 - 1), (double) y2, 1.0D);
								this.tessellator.addVertex((double) (x2 + 1), (double) y2, 1.0D);
								this.tessellator.addVertex((double) (x2 + 1), (double) y1, 1.0D);
								this.tessellator.addVertex((double) (x1 - 1), (double) y1, 1.0D);
								this.tessellator.draw();
								GL11.glEnable(3553);
								this.theMinecraft.fontRenderer.drawStringWithShadow(pt.name, x1, y1 + 1, pt.type == 0 ? -1 : -65536);
							}
						} else {
							rx = 117.0D / drawX;
							wayZ *= rx;
							wayZ *= rx;
							ry = Math.sqrt(wayZ * wayZ + wayZ * wayZ);
							GL11.glColor3f(pt.red, pt.green, pt.blue);
							Waypoint.MARKER[pt.type].bind();
							GL11.glRotatef((this.notchDirection ? 0.0F : 90.0F) - alpha, 0.0F, 0.0F, 1.0F);
							GL11.glTranslated(0.0D, -ry, 0.0D);
							this.drawCenteringRectangle(0.0D, 0.0D, 1.0D, 8.0D, 8.0D);
						}
					} finally {
						GL11.glPopMatrix();
					}
				}
			}
		}

		int posX;
		int posY;
		if (this.renderType == 1) {
			GL11.glScaled(1.0D / (double) mapscale, 1.0D / (double) mapscale, 1.0D);
			GL11.glTranslated((double) this.scWidth * -0.5D, (double) this.scHeight * -0.5D, 0.0D);
			GL11.glScaled((double) fscale, (double) fscale, 1.0D);
			width = 0;
			height = 4;
			BiomeGenBase[] arr$ = bgbList;
			posX = arr$.length;

			BiomeGenBase bgb;
			for (posY = 0; posY < posX; ++posY) {
				bgb = arr$[posY];
				width = Math.max(width, this.theMinecraft.fontRenderer.getStringWidth(bgb.biomeName));
				height += 10;
			}

			width += 16;
			int xpos = (this.mapPosition & 2) == 0 ? 2 : this.scWidth / fscale - 2 - width;
			posX = (this.mapPosition & 1) == 0 ? 2 : this.scHeight / fscale - 2 - height;
			GL11.glDisable(3553);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.627451F);
			this.tessellator.startDrawingQuads();
			this.tessellator.addVertex((double) xpos, (double) (posX + height), 1.0D);
			this.tessellator.addVertex((double) (xpos + width), (double) (posX + height), 1.0D);
			this.tessellator.addVertex((double) (xpos + width), (double) posX, 1.0D);
			this.tessellator.addVertex((double) xpos, (double) posX, 1.0D);
			this.tessellator.draw();

			for (posY = 0; posY < bgbList.length; ++posY) {
				bgb = bgbList[posY];
				int color = bgb.color;
				String name = bgb.biomeName;
				GL11.glEnable(3553);
				this.theMinecraft.fontRenderer.drawStringWithShadow(name, xpos + 14, posX + 3 + posY * 10, 16777215);
				GL11.glDisable(3553);
				alpha = (float) (color >> 16 & 255) * 0.003921569F;
				float g = (float) (color >> 8 & 255) * 0.003921569F;
				float b = (float) (color & 255) * 0.003921569F;
				GL11.glColor3f(alpha, g, b);
				this.tessellator.startDrawingQuads();
				this.tessellator.addVertex((double) (xpos + 2), (double) (posX + posY * 10 + 12), 1.0D);
				this.tessellator.addVertex((double) (xpos + 12), (double) (posX + posY * 10 + 12), 1.0D);
				this.tessellator.addVertex((double) (xpos + 12), (double) (posX + posY * 10 + 2), 1.0D);
				this.tessellator.addVertex((double) (xpos + 2), (double) (posX + posY * 10 + 2), 1.0D);
				this.tessellator.draw();
			}

			GL11.glScaled(1.0D / (double) fscale, 1.0D / (double) fscale, 1.0D);
			GL11.glTranslated((double) this.scWidth * 0.5D, (double) this.scHeight * 0.5D, 0.0D);
			GL11.glScaled((double) mapscale, (double) mapscale, 1.0D);
			GL11.glEnable(3553);
		} else if (this.renderType != 2 && this.renderType == 3) {
			;
		}

		GL11.glScalef(1.0F / (float) mapscale, 1.0F / (float) mapscale, 1.0F);
		GL11.glDepthMask(true);
		GL11.glEnable(2929);
		FontRenderer fontRenderer;
		String line1;
		if (this.visibleWaypoints && this.currentDimension != this.waypointDimension) {
			fontRenderer = this.theMinecraft.fontRenderer;
			line1 = this.getDimensionName(this.waypointDimension);
			float width0 = (float) (fontRenderer.getStringWidth(line1) * fscale) * 0.5F;
			GL11.glTranslatef(-width0, -32.0F, 0.0F);
			GL11.glScaled((double) fscale, (double) fscale, 1.0D);
			fontRenderer.drawStringWithShadow(line1, 0, 0, 16777215);
			GL11.glScaled(1.0D / (double) fscale, 1.0D / (double) fscale, 1.0D);
			GL11.glTranslatef(width0, 32.0F, 0.0F);
		}

		if (this.showCoordinate) {
			fontRenderer = this.theMinecraft.fontRenderer;
			GL11.glTranslatef(0.0F, 16.0F, 0.0F);
			GL11.glScalef((float) fscale, (float) fscale, 1.0F);
			String line2;
			if (this.coordinateType == 0) {
				posX = MathHelper.floor_double(this.playerPosX);
				posY = MathHelper.floor_double(this.thePlayer.boundingBox.minY);
				posZ = MathHelper.floor_double(this.playerPosZ);
				line1 = String.format("%+d, %+d", posX, posZ);
				line2 = Integer.toString(posY);
			} else {
				line1 = String.format("%+1.2f, %+1.2f", this.playerPosX, this.playerPosZ);
				line2 = String.format("%1.2f (%d)", this.playerPosY, (int) this.thePlayer.boundingBox.minY);
			}

			fontRenderer.drawStringWithShadow(line1, (int) ((float) fontRenderer.getStringWidth(line1) * -0.5F), 2, 16777215);
			fontRenderer.drawStringWithShadow(line2, (int) ((float) fontRenderer.getStringWidth(line2) * -0.5F), 11, 16777215);
			GL11.glScaled(1.0D / (double) fscale, 1.0D / (double) fscale, 1.0D);
			GL11.glTranslatef(0.0F, -16.0F, 0.0F);
		}

	}

	private void texture(String texture) {
		this.theMinecraft.renderEngine.bindTexture(this.theMinecraft.renderEngine.getTexture(texture));
	}

	public void setOption(EnumOption option, EnumOptionValue value) {
		this.lock.lock();

		try {
			label249: switch (option) {
			case MINIMAP:
				this.enable = EnumOptionValue.bool(value);
				break;
			case SHOW_MENU_KEY:
				this.showMenuKey = EnumOptionValue.bool(value);
				break;
			case MASK_TYPE:
				this.useStencil = value == EnumOptionValue.STENCIL;
				break;
			case DIRECTION_TYPE:
				this.notchDirection = true;
				break;
			case MAP_SHAPE:
				this.roundmap = value == EnumOptionValue.ROUND;
				break;
			case TEXTURE:
				this.textureView = Math.max(0, option.getValue(value));
				switch (this.textureView) {
				case 0:
					GLTexture.setPack("/reifnsk/minimap/");
					break label249;
				case 1:
					GLTexture.setPack("/reifnsk/minimap/zantextures/");
				default:
					break label249;
				}
			case MAP_POSITION:
				this.mapPosition = Math.max(0, option.getValue(value));
				break;
			case MAP_SCALE:
				this.mapScale = option.getValue(value);
				break;
			case MAP_OPACITY:
				switch (value) {
				case PERCENT100:
				default:
					this.mapOpacity = 1.0F;
					break label249;
				case PERCENT75:
					this.mapOpacity = 0.75F;
					break label249;
				case PERCENT50:
					this.mapOpacity = 0.5F;
					break label249;
				case PERCENT25:
					this.mapOpacity = 0.25F;
					break label249;
				}
			case LARGE_MAP_SCALE:
				this.largeMapScale = option.getValue(value);
				break;
			case LARGE_MAP_OPACITY:
				switch (value) {
				case PERCENT100:
				default:
					this.largeMapOpacity = 1.0F;
					break label249;
				case PERCENT75:
					this.largeMapOpacity = 0.75F;
					break label249;
				case PERCENT50:
					this.largeMapOpacity = 0.5F;
					break label249;
				case PERCENT25:
					this.largeMapOpacity = 0.25F;
					break label249;
				}
			case LARGE_MAP_LABEL:
				this.largeMapLabel = EnumOptionValue.bool(value);
				break;
			case FILTERING:
				this.filtering = EnumOptionValue.bool(value);
				break;
			case SHOW_COORDINATES:
				this.coordinateType = Math.max(0, option.getValue(value));
				this.showCoordinate = value != EnumOptionValue.DISABLE;
				break;
			case FONT_SCALE:
				this.fontScale = Math.max(0, option.getValue(value));
				break;
			case UPDATE_FREQUENCY:
				this.updateFrequencySetting = Math.max(0, option.getValue(value));
				break;
			case THREADING:
				this.threading = EnumOptionValue.bool(value);
				break;
			case THREAD_PRIORITY:
				this.threadPriority = Math.max(0, option.getValue(value));
				if (this.workerThread != null && this.workerThread.isAlive()) {
					this.workerThread.setPriority(3 + this.threadPriority);
				}
				break;
			case PRELOADED_CHUNKS:
				this.preloadedChunks = EnumOptionValue.bool(value);
				break;
			case LIGHTING:
				this.lightmap = Math.max(0, option.getValue(value));
				break;
			case LIGHTING_TYPE:
				this.lightType = Math.max(0, option.getValue(value));
				break;
			case TERRAIN_UNDULATE:
				this.undulate = EnumOptionValue.bool(value);
				break;
			case TERRAIN_DEPTH:
				this.heightmap = EnumOptionValue.bool(value);
				break;
			case TRANSPARENCY:
				this.transparency = EnumOptionValue.bool(value);
				break;
			case ENVIRONMENT_COLOR:
				this.environmentColor = EnumOptionValue.bool(value);
				break;
			case OMIT_HEIGHT_CALC:
				this.omitHeightCalc = EnumOptionValue.bool(value);
				break;
			case HIDE_SNOW:
				this.hideSnow = EnumOptionValue.bool(value);
				break;
			case SHOW_CHUNK_GRID:
				this.showChunkGrid = EnumOptionValue.bool(value);
				break;
			case SHOW_SLIME_CHUNK:
				this.showSlimeChunk = EnumOptionValue.bool(value);
				break;
			case RENDER_TYPE:
				this.renderType = Math.max(0, option.getValue(value));
				break;
			case MINIMAP_OPTION:
				this.theMinecraft.displayGuiScreen(new GuiOptionScreen(1));
				break;
			case SURFACE_MAP_OPTION:
				this.theMinecraft.displayGuiScreen(new GuiOptionScreen(2));
				break;
			case ABOUT_MINIMAP:
				this.theMinecraft.displayGuiScreen(new GuiOptionScreen(5));
				break;
			case ENTITIES_RADAR_OPTION:
				this.theMinecraft.displayGuiScreen(new GuiOptionScreen(3));
				break;
			case ENG_FORUM:
				try {
					Desktop.getDesktop().browse(new URI("http://www.minecraftforum.net/index.php?showtopic=482147"));
				} catch (Exception var9) {
					error("Open Forum(en)", var9);
				}
				break;
			case JP_FORUM:
				try {
					Desktop.getDesktop().browse(new URI("http://forum.minecraftuser.jp/viewtopic.php?f=13&t=153"));
				} catch (Exception var8) {
					var8.printStackTrace();
					error("Open Forum(jp)", var8);
				}
				break;
			case DEATH_POINT:
				this.deathPoint = EnumOptionValue.bool(value);
				break;
			case ENTITY_DIRECTION:
				this.configEntityDirection = EnumOptionValue.bool(value);
				break;
			case MARKER_OPTION:
				this.theMinecraft.displayGuiScreen(new GuiOptionScreen(4));
				break;
			case MARKER:
				this.marker = EnumOptionValue.bool(value);
				break;
			case MARKER_ICON:
				this.markerIcon = EnumOptionValue.bool(value);
				break;
			case MARKER_LABEL:
				this.markerLabel = EnumOptionValue.bool(value);
				break;
			case MARKER_DISTANCE:
				this.markerDistance = EnumOptionValue.bool(value);
				break;
			case DEFAULT_ZOOM:
				this.defaultZoom = Math.max(0, option.getValue(value));
				break;
			case AUTO_UPDATE_CHECK:
				this.autoUpdateCheck = EnumOptionValue.bool(value);
				if (this.autoUpdateCheck) {
					this.updateCheck();
				}
				break;
			case UPDATE_CHECK:
				EnumOptionValue eov = EnumOption.UPDATE_CHECK.getValue(this.updateCheckFlag);
				if (eov != EnumOptionValue.UPDATE_FOUND1 && eov != EnumOptionValue.UPDATE_FOUND2) {
					this.updateCheck();
				} else {
					this.theMinecraft.displayGuiScreen(new GuiOptionScreen(5));
				}
			}

			this.forceUpdate = true;
			this.stripCounter.reset();
			if (this.threading) {
				this.mapCalc(false);
				if (this.isCompleteImage) {
					this.texture.register();
				}
			}
		} finally {
			this.lock.unlock();
		}

	}

	public EnumOptionValue getOption(EnumOption option) {
		switch (option) {
		case MINIMAP:
			return EnumOptionValue.bool(this.enable);
		case SHOW_MENU_KEY:
			return EnumOptionValue.bool(this.showMenuKey);
		case MASK_TYPE:
			return this.useStencil ? EnumOptionValue.STENCIL : EnumOptionValue.DEPTH;
		case DIRECTION_TYPE:
			return this.notchDirection ? EnumOptionValue.NORTH : EnumOptionValue.EAST;
		case MAP_SHAPE:
			return this.roundmap ? EnumOptionValue.ROUND : EnumOptionValue.SQUARE;
		case TEXTURE:
			return option.getValue(this.textureView);
		case MAP_POSITION:
			return option.getValue(this.mapPosition);
		case MAP_SCALE:
			return option.getValue(this.mapScale);
		case MAP_OPACITY:
			return this.mapOpacity == 0.25F ? EnumOptionValue.PERCENT25 : (this.mapOpacity == 0.5F ? EnumOptionValue.PERCENT50 : (this.mapOpacity == 0.75F ? EnumOptionValue.PERCENT75 : EnumOptionValue.PERCENT100));
		case LARGE_MAP_SCALE:
			return option.getValue(this.largeMapScale);
		case LARGE_MAP_OPACITY:
			return this.largeMapOpacity == 0.25F ? EnumOptionValue.PERCENT25 : (this.largeMapOpacity == 0.5F ? EnumOptionValue.PERCENT50 : (this.largeMapOpacity == 0.75F ? EnumOptionValue.PERCENT75 : EnumOptionValue.PERCENT100));
		case LARGE_MAP_LABEL:
			return EnumOptionValue.bool(this.largeMapLabel);
		case FILTERING:
			return EnumOptionValue.bool(this.filtering);
		case SHOW_COORDINATES:
			return option.getValue(this.coordinateType);
		case FONT_SCALE:
			return option.getValue(this.fontScale);
		case UPDATE_FREQUENCY:
			return option.getValue(this.updateFrequencySetting);
		case THREADING:
			return EnumOptionValue.bool(this.threading);
		case THREAD_PRIORITY:
			return option.getValue(this.threadPriority);
		case PRELOADED_CHUNKS:
			return EnumOptionValue.bool(this.preloadedChunks);
		case LIGHTING:
			return option.getValue(this.lightmap);
		case LIGHTING_TYPE:
			return option.getValue(this.lightType);
		case TERRAIN_UNDULATE:
			return EnumOptionValue.bool(this.undulate);
		case TERRAIN_DEPTH:
			return EnumOptionValue.bool(this.heightmap);
		case TRANSPARENCY:
			return EnumOptionValue.bool(this.transparency);
		case ENVIRONMENT_COLOR:
			return EnumOptionValue.bool(this.environmentColor);
		case OMIT_HEIGHT_CALC:
			return EnumOptionValue.bool(this.omitHeightCalc);
		case HIDE_SNOW:
			return EnumOptionValue.bool(this.hideSnow);
		case SHOW_CHUNK_GRID:
			return EnumOptionValue.bool(this.showChunkGrid);
		case SHOW_SLIME_CHUNK:
			return EnumOptionValue.bool(this.showSlimeChunk);
		case RENDER_TYPE:
			return option.getValue(this.renderType);
		case MINIMAP_OPTION:
		case SURFACE_MAP_OPTION:
		case ABOUT_MINIMAP:
		case ENTITIES_RADAR_OPTION:
		case ENG_FORUM:
		case JP_FORUM:
		case MARKER_OPTION:
		default:
			return option.getValue(0);
		case DEATH_POINT:
			return EnumOptionValue.bool(this.deathPoint);
		case ENTITY_DIRECTION:
			return EnumOptionValue.bool(this.configEntityDirection);
		case MARKER:
			return EnumOptionValue.bool(this.marker);
		case MARKER_ICON:
			return EnumOptionValue.bool(this.markerIcon);
		case MARKER_LABEL:
			return EnumOptionValue.bool(this.markerLabel);
		case MARKER_DISTANCE:
			return EnumOptionValue.bool(this.markerDistance);
		case DEFAULT_ZOOM:
			return option.getValue(this.defaultZoom);
		case AUTO_UPDATE_CHECK:
			return EnumOptionValue.bool(this.autoUpdateCheck);
		case UPDATE_CHECK:
			return option.getValue(this.updateCheckFlag);
		}
	}

	void saveOptions() {
		File file = new File(directory, "option.txt");

		try {
			PrintWriter out = new PrintWriter(file, "UTF-8");
			EnumOption[] arr$ = EnumOption.values();
			int len$ = arr$.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				EnumOption option = arr$[i$];
				if (option != EnumOption.DIRECTION_TYPE && option != EnumOption.UPDATE_CHECK && this.getOption(option) != EnumOptionValue.SUB_OPTION && this.getOption(option) != EnumOptionValue.VERSION && this.getOption(option) != EnumOptionValue.AUTHOR) {
					out.printf("%s: %s%n", capitalize(option.toString()), capitalize(this.getOption(option).toString()));
				}
			}

			out.flush();
			out.close();
		} catch (Exception var7) {
			var7.printStackTrace();
		}

	}

	private void loadOptions() {
		File file = new File(directory, "option.txt");
		if (file.exists()) {
			boolean error = false;

			try {
				Scanner in = new Scanner(file, "UTF-8");

				while (in.hasNextLine()) {
					try {
						String[] strs = in.nextLine().split(":");
						this.setOption(EnumOption.valueOf(toUpperCase(strs[0].trim())), EnumOptionValue.valueOf(toUpperCase(strs[1].trim())));
					} catch (Exception var5) {
						System.err.println(var5.getMessage());
						error = true;
					}
				}

				in.close();
			} catch (Exception var6) {
				var6.printStackTrace();
			}

			if (error) {
				this.saveOptions();
			}

			this.flagZoom = this.defaultZoom;
		}
	}

	public List getWaypoints() {
		return this.wayPts;
	}

	void saveWaypoints() {
		File waypointFile = new File(directory, this.currentLevelName + ".DIM" + this.waypointDimension + ".points");
		if (waypointFile.isDirectory()) {
			this.chatInfo("§E[Rei's Minimap] Error Saving Waypoints");
			error("[Rei's Minimap] Error Saving Waypoints: (" + waypointFile + ") is directory.");
		} else {
			try {
				PrintWriter out = new PrintWriter(waypointFile, "UTF-8");
				Iterator i$ = this.wayPts.iterator();

				while (i$.hasNext()) {
					Waypoint pt = (Waypoint) i$.next();
					out.println(pt);
				}

				out.flush();
				out.close();
			} catch (Exception var5) {
				this.chatInfo("§E[Rei's Minimap] Error Saving Waypoints");
				error("Error Saving Waypoints", var5);
			}

		}
	}

	void loadWaypoints() {
		this.wayPts = null;
		this.wayPtsMap.clear();
		Pattern pattern = Pattern.compile(Pattern.quote(this.currentLevelName) + "\\.DIM(-?[0-9])\\.points");
		int load = 0;
		int dim = 0;
		String[] arr$ = directory.list();
		int len$ = arr$.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			String file = arr$[i$];
			Matcher m = pattern.matcher(file);
			if (m.matches()) {
				++dim;
				int dimension = Integer.parseInt(m.group(1));
				ArrayList list = new ArrayList();
				Scanner in = null;

				try {
					in = new Scanner(new File(directory, file), "UTF-8");

					while (in.hasNextLine()) {
						Waypoint wp = Waypoint.load(in.nextLine());
						if (wp != null) {
							list.add(wp);
							++load;
						}
					}
				} catch (Exception var16) {
					;
				} finally {
					if (in != null) {
						in.close();
					}

				}

				this.wayPtsMap.put(dimension, list);
				if (dimension == this.currentDimension) {
					this.wayPts = list;
				}
			}
		}

		if (this.wayPts == null) {
			this.wayPts = new ArrayList();
		}

		if (load != 0) {
			this.chatInfo("§E[Rei's Minimap] " + load + " Waypoints loaded for " + this.currentLevelName);
		}

	}

	void chatInfo(String s) {
		this.ingameGUI.getChatGUI().printChatMessage(s);
	}

	private float[] generateLightBrightnessTable(float f) {
		float[] result = new float[16];

		for (int i = 0; i <= 15; ++i) {
			float f1 = 1.0F - (float) i / 15.0F;
			result[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
		}

		return result;
	}

	private int calculateSkylightSubtracted(long time, float k) {
		float f1 = this.calculateCelestialAngle(time) + k;
		float f2 = Math.max(0.0F, Math.min(1.0F, 1.0F - (MathHelper.cos(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F)));
		f2 = 1.0F - f2;
		f2 = (float) ((double) f2 * (1.0D - (double) (this.theWorld.getRainStrength(1.0F) * 5.0F) / 16.0D));
		f2 = (float) ((double) f2 * (1.0D - (double) (this.theWorld.getWeightedThunderStrength(1.0F) * 5.0F) / 16.0D));
		f2 = 1.0F - f2;
		return (int) (f2 * 11.0F);
	}

	private void updateLightmap(long time, float k) {
		float _f = this.func_35464_b(time, k);

		for (int i = 0; i < 256; ++i) {
			float f = _f * 0.95F + 0.05F;
			float sky = this.theWorld.provider.lightBrightnessTable[i / 16] * f;
			float block = this.theWorld.provider.lightBrightnessTable[i % 16] * 1.55F;
			if (this.theWorld.lastLightningBolt > 0) {
				sky = this.theWorld.provider.lightBrightnessTable[i / 16];
			}

			float skyR = sky * (_f * 0.65F + 0.35F);
			float skyG = sky * (_f * 0.65F + 0.35F);
			float blockG = block * ((block * 0.6F + 0.4F) * 0.6F + 0.4F);
			float blockB = block * (block * block * 0.6F + 0.4F);
			float red = skyR + block;
			float green = skyG + blockG;
			float blue = sky + blockB;
			red = Math.min(1.0F, red * 0.96F + 0.03F);
			green = Math.min(1.0F, green * 0.96F + 0.03F);
			blue = Math.min(1.0F, blue * 0.96F + 0.03F);
			float f12 = this.theMinecraft.gameSettings.gammaSetting;
			float f13 = 1.0F - red;
			float f14 = 1.0F - green;
			float f15 = 1.0F - blue;
			f13 = 1.0F - f13 * f13 * f13 * f13;
			f14 = 1.0F - f14 * f14 * f14 * f14;
			f15 = 1.0F - f15 * f15 * f15 * f15;
			red = red * (1.0F - f12) + f13 * f12;
			green = green * (1.0F - f12) + f14 * f12;
			blue = blue * (1.0F - f12) + f15 * f12;
			this.lightmapRed[i] = Math.max(0.0F, Math.min(1.0F, red * 0.96F + 0.03F));
			this.lightmapGreen[i] = Math.max(0.0F, Math.min(1.0F, green * 0.96F + 0.03F));
			this.lightmapBlue[i] = Math.max(0.0F, Math.min(1.0F, blue * 0.96F + 0.03F));
		}

	}

	private float func_35464_b(long time, float k) {
		float f1 = this.calculateCelestialAngle(time) + k;
		float f2 = Math.max(0.0F, Math.min(1.0F, 1.0F - (MathHelper.cos(f1 * 3.1415927F * 2.0F) * 2.0F + 0.2F)));
		f2 = 1.0F - f2;
		f2 *= 1.0F - this.theWorld.getRainStrength(1.0F) * 5.0F * 0.0625F;
		f2 *= 1.0F - this.theWorld.getWeightedThunderStrength(1.0F) * 5.0F * 0.0625F;
		return f2 * 0.8F + 0.2F;
	}

	private float calculateCelestialAngle(long time) {
		int i = (int) (time % 24000L);
		float f1 = (float) (i + 1) * 4.1666666E-5F - 0.25F;
		if (f1 < 0.0F) {
			++f1;
		} else if (f1 > 1.0F) {
			--f1;
		}

		float f2 = f1;
		f1 = 1.0F - (float) ((Math.cos((double) f1 * 3.141592653589793D) + 1.0D) * 0.5D);
		f1 = f2 + (f1 - f2) * 0.33333334F;
		return f1;
	}

	private void drawCenteringRectangle(double centerX, double centerY, double z, double w, double h) {
		w *= 0.5D;
		h *= 0.5D;
		this.startDrawingQuads();
		this.addVertexWithUV(centerX - w, centerY + h, z, 0.0D, 1.0D);
		this.addVertexWithUV(centerX + w, centerY + h, z, 1.0D, 1.0D);
		this.addVertexWithUV(centerX + w, centerY - h, z, 1.0D, 0.0D);
		this.addVertexWithUV(centerX - w, centerY - h, z, 0.0D, 0.0D);
		this.draw();
	}

	public static String capitalize(String src) {
		if (src == null) {
			return null;
		} else {
			boolean title = true;
			char[] cs = src.toCharArray();
			int i = 0;

			for (int j = cs.length; i < j; ++i) {
				char c = cs[i];
				if (c == '_') {
					c = ' ';
				}

				cs[i] = title ? Character.toTitleCase(c) : Character.toLowerCase(c);
				title = Character.isWhitespace(c);
			}

			return new String(cs);
		}
	}

	public static String toUpperCase(String src) {
		return src == null ? null : src.replace(' ', '_').toUpperCase(Locale.ENGLISH);
	}

	private static boolean checkGuiScreen(GuiScreen gui) {
		return gui == null || gui instanceof GuiScreenInterface || gui instanceof GuiChat || gui instanceof GuiGameOver;
	}

	String getDimensionName(int dim) {
		String name = (String) this.dimensionName.get(dim);
		return name == null ? "DIM:" + dim : name;
	}

	int getWaypointDimension() {
		return this.waypointDimension;
	}

	int getCurrentDimension() {
		return this.currentDimension;
	}

	private double getDimensionScale(int dim) {
		Double d = (Double) this.dimensionScale.get(dim);
		return d == null ? 1.0D : d;
	}

	double getVisibleDimensionScale() {
		return this.getDimensionScale(this.waypointDimension) / this.getDimensionScale(this.currentDimension);
	}

	void prevDimension() {
		Entry entry = this.wayPtsMap.lowerEntry(this.waypointDimension);
		if (entry == null) {
			entry = this.wayPtsMap.lowerEntry(Integer.MAX_VALUE);
		}

		if (entry != null) {
			this.waypointDimension = (Integer) entry.getKey();
			this.wayPts = (List) entry.getValue();
		}

	}

	void nextDimension() {
		Entry entry = this.wayPtsMap.higherEntry(this.waypointDimension);
		if (entry == null) {
			entry = this.wayPtsMap.higherEntry(Integer.MIN_VALUE);
		}

		if (entry != null) {
			this.waypointDimension = (Integer) entry.getKey();
			this.wayPts = (List) entry.getValue();
		}

	}

	private static SocketAddress getRemoteSocketAddress(EntityPlayer player) {
		NetClientHandler netClientHandler = ((EntityClientPlayerMP) player).sendQueue;
		INetworkManager networkManager = netClientHandler.getNetManager();
		return networkManager == null ? null : networkManager.getSocketAddress();
	}

	private static final void error(String str, Exception e) {
		File file = new File(directory, "error.txt");
		PrintWriter out = null;

		try {
			FileOutputStream fos = new FileOutputStream(file, true);
			out = new PrintWriter(new OutputStreamWriter(fos, "UTF-8"));
			information(out);
			out.println(str);
			e.printStackTrace(out);
			out.println();
			out.flush();
		} catch (Exception var8) {
			;
		} finally {
			if (out != null) {
				out.close();
			}

		}

	}

	private static final void error(String str) {
		File file = new File(directory, "error.txt");
		PrintWriter out = null;

		try {
			FileOutputStream fos = new FileOutputStream(file, true);
			out = new PrintWriter(new OutputStreamWriter(fos, "UTF-8"));
			information(out);
			out.println(str);
			out.println();
			out.flush();
		} catch (Exception var7) {
			;
		} finally {
			if (out != null) {
				out.close();
			}

		}

	}

	private static final void information(PrintWriter out) {
		out.printf("--- %1$tF %1$tT %1$tZ ---%n", System.currentTimeMillis());
		out.printf("Rei's Minimap %s [%s]%n", "v3.2_05", "1.4.6");
		out.printf("OS: %s (%s) version %s%n", System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version"));
		out.printf("Java: %s, %s%n", System.getProperty("java.version"), System.getProperty("java.vendor"));
		out.printf("VM: %s (%s), %s%n", System.getProperty("java.vm.name"), System.getProperty("java.vm.info"), System.getProperty("java.vm.vendor"));
		out.printf("LWJGL: %s%n", Sys.getVersion());
		out.printf("OpenGL: %s version %s, %s%n", GL11.glGetString(7937), GL11.glGetString(7938), GL11.glGetString(7936));
	}

	boolean isMinecraftThread() {
		return Thread.currentThread() == this.mcThread;
	}

	static final int version(int i, int major, int minor, int revision) {
		return (i & 255) << 24 | (major & 255) << 16 | (minor & 255) << 8 | (revision & 255) << 0;
	}

	int getWorldHeight() {
		return this.worldHeight;
	}

	private int[] getColor(String c) {
		InputStream in = null;
		Object var3 = null;

		int[] result;
		label74: {
			int[] var5;
			try {
				in = this.texturePack.getResourceAsStream(c);
				if (in == null) {
					break label74;
				}

				BufferedImage img = ImageIO.read(in);
				if (img.getWidth() != 256) {
					break label74;
				}

				result = new int[256 * img.getHeight()];
				img.getRGB(0, 0, 256, img.getHeight(), result, 0, 256);
				var5 = result;
			} catch (IOException var9) {
				break label74;
			} finally {
				close(in);
			}

			return var5;
		}

		result = new int[256];

		for (int i = 0; i < 256; ++i) {
			result[i] = -16777216 | i << 16 | i << 8 | i;
		}

		return result;
	}

	private static void close(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException var2) {
				var2.printStackTrace();
			}
		}

	}

	private int getEntityColor(Entity entity) {
		if (entity == this.thePlayer) {
			return 0;
		} else if (entity instanceof EntityPlayer) {
			return this.visibleEntityPlayer ? -16711681 : 0;
		}
		return 0;
	}

	private int getVisibleEntityType(Entity entity) {
		if (entity instanceof EntityLiving) {
			if (entity == this.thePlayer) {
				return -1;
			} else if (entity instanceof EntityPlayer) {
				return this.visibleEntityPlayer ? 0 : -1;
			}
			return -1;
		} else {
			return -1;
		}
	}

	private void updateCheck() {
		if (this.updateCheckURL != null) {
			EnumOptionValue value = EnumOption.UPDATE_CHECK.getValue(this.updateCheckFlag);
			if (value == EnumOptionValue.UPDATE_CHECK || value == EnumOptionValue.UPDATE_NOT_FOUND) {
				this.updateCheckFlag = EnumOption.UPDATE_CHECK.getValue(EnumOptionValue.UPDATE_CHECKING);
				(new Thread() {
					public void run() {
						while (ReiMinimap.this.ingameGUI == null) {
							try {
								Thread.sleep(100L);
							} catch (Exception var18) {
								;
							}
						}

						int flag = EnumOption.UPDATE_CHECK.getValue(EnumOptionValue.UPDATE_NOT_FOUND);
						InputStream in = null;

						try {
							in = ReiMinimap.this.updateCheckURL.openStream();
							Scanner scanner = new Scanner(in, "UTF-8");

							while (scanner.hasNextLine()) {
								String line = scanner.nextLine();
								String[] strs = line.split("\\s*,\\s*");
								if (strs.length >= 4) {
									try {
										int modver = Integer.decode(strs[0]);
										int mcver = Integer.decode(strs[1]);
										if (modver > 197125) {
											if (mcver == 33620998) {
												flag = EnumOption.UPDATE_CHECK.getValue(EnumOptionValue.UPDATE_FOUND1);
												ReiMinimap.this.chatInfo(String.format("§B[%s] Rei's Minimap %s update found!!", strs[3], strs[2]));
											} else {
												if (flag == EnumOption.UPDATE_CHECK.getValue(EnumOptionValue.UPDATE_NOT_FOUND)) {
													flag = EnumOption.UPDATE_CHECK.getValue(EnumOptionValue.UPDATE_FOUND2);
												}

												ReiMinimap.this.chatInfo(String.format("§9[%s] Rei's Minimap %s update found!", strs[3], strs[2]));
											}
										}
									} catch (NumberFormatException var19) {
										var19.printStackTrace();
									}
								}
							}
						} catch (Exception var20) {
							var20.printStackTrace();
						} finally {
							ReiMinimap.this.updateCheckFlag = flag;

							try {
								in.close();
							} catch (Exception var17) {
								;
							}

						}

					}
				}).start();
			}
		}
	}

	boolean getMarker() {
		return this.marker & (this.markerIcon | this.markerLabel | this.markerDistance);
	}

	boolean getMarkerIcon() {
		return this.markerIcon;
	}

	boolean getMarkerLabel() {
		return this.markerLabel;
	}

	boolean getMarkerDistance() {
		return this.markerDistance;
	}

	int getUpdateCount() {
		return this.updateCount;
	}

	Minecraft getMinecraft() {
		return this.theMinecraft;
	}

	World getWorld() {
		return this.theWorld;
	}

	static {
		directory = new File(Minecraft.getMinecraftDir(), "mods" + File.separatorChar + "rei_minimap");
		updateFrequencys = new int[] { 2, 5, 10, 20, 40 };
		instance = new ReiMinimap();
		LinkedList bgb = new LinkedList();
		BiomeGenBase[] arr$ = BiomeGenBase.biomeList;
		int len$ = arr$.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			BiomeGenBase b = arr$[i$];
			if (b != null) {
				bgb.add(b);
			}
		}

		bgbList = (BiomeGenBase[]) bgb.toArray(new BiomeGenBase[0]);
		InputStream in = GuiIngame.class.getResourceAsStream(GuiIngame.class.getSimpleName() + ".class");
		if (in != null) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];

				while (true) {
					int read = in.read(buffer);
					if (read == -1) {
						in.close();
						String str = (new String(baos.toByteArray(), "UTF-8")).toLowerCase(Locale.ENGLISH);
						if (str.indexOf("§0§0") != -1 && str.indexOf("§e§f") != -1) {
							instance.errorString = "serious error";
							instance.texture.unregister();
							instance.texture = null;
						}
						break;
					}

					baos.write(buffer, 0, read);
				}
			} catch (Exception var7) {
				;
			}
		}

		ZOOM_LIST = new double[] { 0.5D, 1.0D, 1.5D, 2.0D, 4.0D, 8.0D };
		Class clazz = null;

		try {
			if (clazz == null) {
				clazz = Class.forName("invmod.entity.EntityIMMob");
			}
		} catch (ClassNotFoundException var6) {
			;
		}

		try {
			if (clazz == null) {
				clazz = Class.forName("invmod.EntityIMWaveAttacker");
			}
		} catch (ClassNotFoundException var5) {
			;
		}

		entityIMWaveAttackerClass = clazz;
		temp = new float[10];
		float f = 0.0F;

		int i;
		for (i = 0; i < temp.length; ++i) {
			temp[i] = (float) (1.0D / Math.sqrt((double) (i + 1)));
			f += temp[i];
		}

		f = 0.3F / f;

		for (i = 0; i < temp.length; ++i) {
			temp[i] *= f;
		}

		f = 0.0F;

		for (i = 0; i < 10; ++i) {
			f += temp[i];
		}

	}
}
