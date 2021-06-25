package by.fxg.seaside.systems.server;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.based.BasedCommand;
import by.fxg.seaside.managers.PlayerManager;
import by.fxg.seaside.systems.jobs.EnumJob;
import by.fxg.seaside.utils.ALevel;
import net.minecraft.command.ICommandSender;

public class CommandClearDeals extends BasedCommand {
	public String getCommandName() {
		return "cleardeals";
	}
	
	public void processCommand(ICommandSender var1, String[] var2) {
		if (Seaside.buyerQuery.containsKey(var1.getCommandSenderName())) Seaside.buyerQuery.remove(var1.getCommandSenderName());
		if (Seaside.sellerQuery.containsKey(var1.getCommandSenderName())) Seaside.sellerQuery.remove(var1.getCommandSenderName());
		var1.sendChatToPlayer("§aАктивные предложения о покупке/продаже очищены!");
	}
}
