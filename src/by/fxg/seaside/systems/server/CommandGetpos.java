package by.fxg.seaside.systems.server;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.based.BasedCommand;
import by.fxg.seaside.utils.ALevel;
import by.fxg.seaside.utils.ServerUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandGetpos extends BasedCommand {
	public String getCommandName() {
		return "getpos";
	}

	public void processCommand(ICommandSender var1, String[] var2) {
		if (Seaside.SERVER) {
			if (this.hasRights(var1, ALevel.SERVER)) { 
				int[] pos = ServerUtils.getEntityPos((EntityPlayer)var1);
				var1.sendChatToPlayer("§aВаша позиция: [" + pos[0] + ", " + pos[1] + ", " + pos[2] + "]");
			}
		}
	}
}
