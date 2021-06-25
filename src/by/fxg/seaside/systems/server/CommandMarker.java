package by.fxg.seaside.systems.server;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.based.BasedCommand;
import by.fxg.seaside.network.SPacketMarker;
import by.fxg.seaside.storage.Coords;
import by.fxg.seaside.systems.markers.Marker;
import by.fxg.seaside.systems.markers.MarkerColor;
import by.fxg.seaside.utils.ALevel;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class CommandMarker extends BasedCommand {
	public String getCommandName() {
		return "marker";
	}

	public void processCommand(ICommandSender var1, String[] var2) {
		if (Seaside.SERVER) {
			if (this.hasRights(var1, ALevel.SERVER)) {
				if (var1 instanceof EntityPlayer) {
					if (var2.length == 3) {
						if (var2[0].equals("add")) {
							PacketDispatcher.sendPacketToPlayer(new SPacketMarker.Add(new Marker(var2[1], MarkerColor.getByID(Integer.valueOf(var2[2])), Coords.getFromEntity((Entity)var1))).get(), (Player)var1);
						} else {
							PacketDispatcher.sendPacketToPlayer(new SPacketMarker.Remove(var2[1]).get(), (Player)var1);
						}
					}
				}
			}
		}
		
	}
}
