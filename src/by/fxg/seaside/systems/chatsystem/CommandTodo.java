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

public class CommandTodo extends BasedCommand {
	public String getCommandName() {
		return "todo";
	}

	public void processCommand(ICommandSender var1, String[] var2) {
		if (Seaside.SERVER) {
			if (var1 instanceof EntityPlayer) {
				EntityPlayer var3 = (EntityPlayer)var1;
				if (!Punishments.instance.hasMute(var3)) {
					String str = Utils.sculptString(var2);
					if (var2.length > 0 && str.contains("*")) {
						String message = "\"" + str.split("\\*")[0] + "\", - сказал(а) " + var1.getCommandSenderName() + "[" + Seaside.getID(var3.getEntityName()) + "], §d" + str.split("\\*")[1] + ".";
						GameLogging.instance.logChat(message);
						List<EntityPlayer> list = ServerUtils.getNearbyEntities(var3, EntityPlayer.class, SVars.cfg.getDouble("speakRadius"));
						if (list != null) {
							for (EntityPlayer player : list) {
								player.addChatMessage(message);
							}
						}
					} else {
						var3.sendChatToPlayer("§c[Использование]§f /todo [фраза*действие]");
					}
				} else {
					//TODO: MUTE
				}
			} else {
				var1.sendChatToPlayer("/todo - nope!");
			}
		}
	}
}
