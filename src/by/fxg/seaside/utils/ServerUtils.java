package by.fxg.seaside.utils;

import java.util.List;

import by.fxg.seaside.network.SPacketGuiData;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class ServerUtils {
	public static MinecraftServer getServer() {
		return MinecraftServer.getServer();
	}

	public static ServerConfigurationManager getManager() {
		return MinecraftServer.getServerConfigurationManager(getServer());
	}
	
	public static EntityPlayerMP getPlayer(String playerName) {
		return getManager().getPlayerForUsername(playerName);
	}
	
	public static int[] getEntityPos(Entity e) {
		return e == null ? null : new int[]{(int)Math.floor(e.posX), (int)Math.floor(e.posY), (int)Math.floor(e.posZ)};
	}
	
	public static <T extends Entity> List<T> getNearbyEntities(Entity ent, Class<T> type, double radius) {
		if (ent != null && type != null && radius > 0.0D) {
			int[] entPos = getEntityPos(ent);
			return ent.worldObj.getEntitiesWithinAABB(type, AxisAlignedBB.getBoundingBox(entPos[0] - radius, entPos[1] - radius, entPos[2] - radius, entPos[0] + radius, entPos[1] + radius, entPos[2] + radius));
		}
		return null;
	}
	
	public static int sendGuiData(EntityPlayer player, String[]... str) {
		SPacketGuiData packet = new SPacketGuiData(str);
		PacketDispatcher.sendPacketToPlayer(packet.get(), (Player)player);
		return packet.key;
	}
	
	public static int getBlockIdAtEntity(Entity e) {
		return e.worldObj.getBlockId((int)Math.floor(e.posX), (int)Math.floor(e.posY), (int)Math.floor(e.posZ));
	}
	
	public static TileEntity getTileAtEntity(Entity e) {
		return e.worldObj.getBlockTileEntity((int)Math.floor(e.posX), (int)Math.floor(e.posY), (int)Math.floor(e.posZ));
	}
	
	public static void setPlayerVirtualWorld(EntityPlayer player, int virtworld) {
		
	}
}
