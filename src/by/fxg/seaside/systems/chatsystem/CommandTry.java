package by.fxg.seaside.systems.chatsystem;

import java.util.List;

import by.fxg.seaside.SVars;
import by.fxg.seaside.Seaside;
import by.fxg.seaside.based.BasedCommand;
import by.fxg.seaside.systems.GameLogging;
import by.fxg.seaside.systems.punishment.Punishments;
import by.fxg.seaside.utils.ServerUtils;
import by.fxg.seaside.utils.Utils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandTry extends BasedCommand {
	public String getCommandName() {
		return "try";
	}

	public void processCommand(ICommandSender var1, String[] var2) {
		if (Seaside.SERVER) {
			if (var1 instanceof EntityPlayer) {
				EntityPlayer var3 = (EntityPlayer)var1;
				if (!Punishments.instance.hasMute(var3)) {
					if (var2.length > 0) {
						String message = "§d " + var1.getCommandSenderName() + "[" + Seaside.getID(var3.getEntityName()) + "] " + Utils.sculptString(var2) + " - " + (var3.worldObj.rand.nextInt(2) == 0 ? "§aУдачно" : "§cНеудачно");
						GameLogging.instance.logChat(message);
						List<EntityPlayer> list = ServerUtils.getNearbyEntities(var3, EntityPlayer.class, SVars.cfg.getDouble("speakRadius"));
						if (list != null) {
							for (EntityPlayer player : list) {
								player.addChatMessage(message);
							}
						}
					} else {
						var3.sendChatToPlayer("§c[Использование]§f /try [действие]");
					}
				} else {
					//TODO: MUTE
				}
			} else {
				var1.sendChatToPlayer("/try - nope!");
			}
		}
	}
}
