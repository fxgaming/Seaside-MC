package by.fxg.seaside.based;

import java.util.List;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.managers.PlayerManager;
import by.fxg.seaside.utils.ALevel;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public abstract class BasedCommand implements ICommand {
	public int compareTo(Object o) {
		return 0;
	}

	public String getCommandUsage(ICommandSender var1) {
		return null;
	}

	public List getCommandAliases() {
		return null;
	}

	public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
		return null;
	}
	
	public boolean isUsernameIndex(int var1) {
		return false;
	}

	public boolean canCommandSenderUseCommand(ICommandSender var1) {
		return true;
	}
	
	public boolean hasRights(ICommandSender var1, ALevel var2) {
		if (var1 instanceof EntityPlayer && (PlayerManager.get((EntityPlayer)var1).alevel.ordinal() >= var2.ordinal() || Seaside.DEBUG)) {
			return true;
		} else {
			var1.sendChatToPlayer("§cУ вас недостаточно прав!");
			return false;
		}
	}
}
