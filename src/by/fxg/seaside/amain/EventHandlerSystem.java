package by.fxg.seaside.amain;

import java.util.List;

import by.fxg.seaside.SVars;
import by.fxg.seaside.Seaside;
import by.fxg.seaside.based.IActionable;
import by.fxg.seaside.managers.PlayerManager;
import by.fxg.seaside.storage.Coords;
import by.fxg.seaside.systems.GameLogging;
import by.fxg.seaside.utils.ServerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

public class EventHandlerSystem {
	@ForgeSubscribe
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer && PlayerManager.get((EntityPlayer)event.entity) == null) PlayerManager.register((EntityPlayer) event.entity);
		if (event.entity instanceof EntityPlayer && event.entity.getExtendedProperties("pme") == null) event.entity.registerExtendedProperties("pme", new PlayerManager((EntityPlayer) event.entity));
	}
	
	@ForgeSubscribe
	public void onServerChat(ServerChatEvent event) {
		if (event.player != null && event.line.length() > 0) {
			String line = event.username + "[" + Seaside.getID(event.username) + "] говорит: " + event.line.split("<" + event.username + "> ")[1];
			GameLogging.instance.logChat(line);
			List<EntityPlayer> list = ServerUtils.getNearbyEntities(event.player, EntityPlayer.class, SVars.cfg.getDouble("speakRadius"));
			if (list != null) {
				for (EntityPlayer player : list) {
					player.addChatMessage(line);
				}
			}
			event.setCanceled(true);
		}
	}
	
	public void onClientPlayerInteractionUsed(EntityPlayer player, INetworkManager networkManager, boolean altKey) {
		int[] xyz = ServerUtils.getEntityPos(player);
		TileEntity te = player.worldObj.getBlockTileEntity(xyz[0], xyz[1], xyz[2]);
		if (te != null && te instanceof IActionable) {
			((IActionable)te).onAction(player, new Coords(xyz[0], xyz[1], xyz[2]), altKey);
		}
	}
}
