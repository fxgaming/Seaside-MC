package by.fxg.seaside.network.minimap;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

public class ChunkData {
   private static final int CHUNK_RELOAD_INTERVAL = 60;
   private static final int CHUNK_SIZE = 32;
   private static final int CHUNK_MASK = 31;
   private static final int CHUNK_ARRAY_SIZE = 1024;
   private static final int CHUNK_SHIFT = 5;
   public final int xPosition;
   public final int zPosition;
   private Chunk chunk;
   private boolean chunkFileExist = true;
   private boolean chunkModified;
   private int chunkUpdateCount;
   private boolean chunkUpdate;
   private int lastRenderCount;
   private int lastAccessCount;
   private int lastUpdateCount;
   private int chunkReloadCount;
   int[] foliageColors;
   int[] grassColors;
   int[] waterColors;
   int[] smoothFoliageColors;
   int[] smoothGrassColors;
   int[] smoothWaterColors;
   float[] heightValues;
   BiomeGenBase[] biomes;
   boolean enviromnentColorUpdateReq;
   boolean slime;
   private static BiomeGenBase[] PLAINS = new BiomeGenBase[256];
   private static final ReiMinimap reimm;
   private static final ChunkData[] cache;
   private static Minecraft minecraft;
   private static World world;
   private static WorldProvider worldProvider;
   private static WorldChunkManager worldChunkManager;
   private static IChunkProvider chunkProvider;
   private static IChunkLoader chunkLoader;
   private static long seed;

   private ChunkData(int cx, int cz) {
      this.lastRenderCount = reimm.getUpdateCount();
      this.lastAccessCount = reimm.getUpdateCount();
      this.lastUpdateCount = reimm.getUpdateCount();
      this.chunkReloadCount = reimm.getUpdateCount();
      this.foliageColors = new int[256];
      this.grassColors = new int[256];
      this.waterColors = new int[256];
      this.smoothFoliageColors = new int[256];
      this.smoothGrassColors = new int[256];
      this.smoothWaterColors = new int[256];
      this.heightValues = new float[256];
      this.biomes = PLAINS;
      this.xPosition = cx;
      this.zPosition = cz;
      this.slime = seed != 0L && (new Random(seed + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ 987234911L)).nextInt(10) == 0;
   }

   public final boolean isAtLocation(int cx, int cz) {
      return cx == this.xPosition && cz == this.zPosition;
   }

   boolean updateChunk(boolean chunkLoadFromFile) {
      boolean result = false;
      int currentCount = reimm.getUpdateCount();
      IChunkLoader loader = chunkLoadFromFile ? chunkLoader : null;
      Chunk prevChunk = this.chunk;
      if (this.chunkReloadCount - currentCount <= 0) {
         this.chunkReloadCount = currentCount + 60;
         if (chunkProvider.chunkExists(this.xPosition, this.zPosition)) {
            this.chunk = chunkProvider.provideChunk(this.xPosition, this.zPosition);
            this.chunkFileExist = true;
         } else if (this.chunk == null && loader != null && this.chunkFileExist) {
            try {
               this.chunk = loader.loadChunk(world, this.xPosition, this.zPosition);
            } catch (IOException var7) {
               var7.printStackTrace();
            }

            this.chunkFileExist = this.chunk != null;
            result = true;
         } else {
            this.chunkReloadCount = currentCount + 7;
         }
      }

      if (this.chunk == null) {
         this.chunk = prevChunk;
      }

      if (this.chunk != prevChunk || this.enviromnentColorUpdateReq) {
         this.updateEnvironmentColor();
         this.chunkUpdate = true;
      }

      if (this.chunk != null) {
         if (this.chunk instanceof ICustomChunk) {
            ICustomChunk icc = (ICustomChunk)this.chunk;
            if (this.chunkUpdateCount != icc.updateCount()) {
               this.chunkUpdateCount = icc.updateCount();
               this.chunkUpdate = true;
            }
         } else if (this.chunkModified != this.chunk.isModified || this.chunkModified && currentCount - this.lastUpdateCount >= 10) {
            this.chunkModified = this.chunk.isModified;
            this.chunkUpdate = true;
         }
      }

      return result;
   }

   public Chunk getChunk() {
      return this.chunk;
   }

   private void updateEnvironmentColor() {
      int x;
      if (this.chunk != null) {
         byte[] biomesArray = this.chunk.getBiomeArray();
         this.biomes = (BiomeGenBase[])Arrays.copyOf(this.biomes, 256);

         for(x = 0; x < 256; ++x) {
            int id = biomesArray[x] & 255;
            if (id == 255) {
               this.enviromnentColorUpdateReq = true;
               return;
            }

            BiomeGenBase biome = BiomeGenBase.biomeList[id];
            this.biomes[x] = biome == null ? BiomeGenBase.plains : biome;
         }
      }

      this.enviromnentColorUpdateReq = false;

      int z;
      for(z = 0; z < 256; ++z) {
         BiomeGenBase bgb = this.biomes[z];
         this.waterColors[z] = bgb.waterColorMultiplier;
         this.foliageColors[z] = bgb.getBiomeFoliageColor();
         this.grassColors[z] = bgb.getBiomeGrassColor();
      }

      for(z = -1; z <= 16; ++z) {
         for(x = -1; x <= 16; ++x) {
            this.calcSmoothColor(x, z);
         }
      }

   }

   private void calcSmoothColor(int x, int z) {
      int setPtr = x & 15 | (z & 15) << 4;
      x += this.xPosition << 4;
      z += this.zPosition << 4;
      ChunkData target = getChunkData(x >> 4, z >> 4);
      if (target != null) {
         int count = 0;
         int fr = 0;
         int fg = 0;
         int fb = 0;
         int gr = 0;
         int gg = 0;
         int gb = 0;
         int wr = 0;
         int wg = 0;
         int wb = 0;

         for(int bz = z - 1; bz <= z + 1; ++bz) {
            for(int bx = x - 1; bx <= x + 1; ++bx) {
               ChunkData cd = getChunkData(bx >> 4, bz >> 4);
               if (cd != null && cd.biomes != null) {
                  int p = bx & 15 | (bz & 15) << 4;
                  int foliageColor = cd.foliageColors[p];
                  fr += foliageColor >> 16 & 255;
                  fg += foliageColor >> 8 & 255;
                  fb += foliageColor & 255;
                  int grassColor = cd.grassColors[p];
                  gr += grassColor >> 16 & 255;
                  gg += grassColor >> 8 & 255;
                  gb += grassColor & 255;
                  int waterColor = cd.waterColors[p];
                  wr += waterColor >> 16 & 255;
                  wg += waterColor >> 8 & 255;
                  wb += waterColor & 255;
                  ++count;
               }
            }
         }

         target.smoothFoliageColors[setPtr] = (fr / count & 255) << 16 | (fg / count & 255) << 8 | fb / count & 255;
         target.smoothGrassColors[setPtr] = (gr / count & 255) << 16 | (gg / count & 255) << 8 | gb / count & 255;
         target.smoothWaterColors[setPtr] = (wr / count & 255) << 16 | (wg / count & 255) << 8 | wb / count & 255;
      }
   }

   static void init() {
      try {
         Arrays.fill(cache, (Object)null);
         minecraft = reimm.getMinecraft();
         world = reimm.getWorld();
         seed = 0L;
         if (Minecraft.getMinecraft().getIntegratedServer() != null) {
            WorldServer[] ws = Minecraft.getMinecraft().getIntegratedServer().worldServers;
            if (ws != null && ws.length >= 1) {
               seed = ws[0].getSeed();
            }
         }

         worldProvider = world.provider;
         worldChunkManager = world.getWorldChunkManager();
         chunkProvider = world.getChunkProvider();
         chunkLoader = getChunkLoader(chunkProvider);
      } catch (Exception var1) {
         throw new RuntimeException(var1);
      }
   }

   private static IChunkLoader getChunkLoader(IChunkProvider chunkProvider) {
      try {
         for(Class clazz = chunkProvider.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            Field[] arr$ = clazz.getDeclaredFields();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Field f = arr$[i$];
               if (f.getType() == IChunkLoader.class) {
                  f.setAccessible(true);
                  return (IChunkLoader)f.get(chunkProvider);
               }
            }
         }
      } catch (Exception var6) {
         ;
      }

      return null;
   }

   public static ChunkData getChunkData(int cx, int cz) {
      int ptr = chunkPointer(cx, cz);
      ChunkData data = cache[ptr];
      return data != null && data.isAtLocation(cx, cz) ? data : null;
   }

   static ChunkData createChunkData(int cx, int cz) {
      int ptr = chunkPointer(cx, cz);
      ChunkData data = cache[ptr];
      return data != null && data.isAtLocation(cx, cz) ? data : (cache[ptr] = new ChunkData(cx, cz));
   }

   private static int chunkPointer(int cx, int cz) {
      return (cz & 31) << 5 | cx & 31;
   }

   void setHeightValue(int x, int z, float value) {
      this.heightValues[z << 4 | x] = value;
   }

   float getHeightValue(int x, int z) {
      return this.heightValues[z << 4 | x];
   }

   static {
      Arrays.fill(PLAINS, BiomeGenBase.plains);
      reimm = ReiMinimap.instance;
      cache = new ChunkData[1024];
   }
}
