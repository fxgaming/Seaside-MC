package by.fxg.seaside.systems.server;

import by.fxg.seaside.based.BasedCommand;
import by.fxg.seaside.utils.ALevel;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandSetAdministrativeLevel extends BasedCommand {
	public String getCommandName() {
		return "setadministrativelevel";
	}

	public void processCommand(ICommandSender var1, String[] var2) {
		if (this.hasRights(var1, ALevel.SERVER)) {
			
		} else if (var1.getCommandSenderName().toLowerCase().equals("console") && !(var1 instanceof EntityPlayer)) {
			//todo console changer
		}
	}
}
