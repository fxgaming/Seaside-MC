package by.fxg.seaside.systems.houses;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.based.BasedCommand;
import by.fxg.seaside.managers.PlayerManager;
import by.fxg.seaside.storage.Coords;
import by.fxg.seaside.systems.Database;
import by.fxg.seaside.utils.ALevel;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class CommandInterior extends BasedCommand {
	public String getCommandName() {
		return "interior";
	}

	public void processCommand(ICommandSender var1, String[] var2) {
		if (Seaside.SERVER) {
			if (this.hasRights(var1, ALevel.SERVER)) {
				if (var2.length >= 2) {
					try {
						if (var2[0].startsWith("add")) {
							if (Database.interiors.containsKey(Integer.valueOf(var2[1]))) {
								var1.sendChatToPlayer("§cИнтерьер " + var2[1] + " уже существует!");
							} else {
								Database.interiors.put(Integer.valueOf(var2[1]), Coords.getFromEntity((EntityPlayer)var1));
								Database.mergeData(1);
								Database.save();
								var1.sendChatToPlayer("§aИнтерьер создан. ID: " + var2[1] + ", " + Database.interiors.get(Integer.valueOf(var2[1])).toString());
							}
						} else if (var2[0].startsWith("rem")) {
							if (Database.interiors.containsKey(Integer.valueOf(var2[1]))) {
								Database.interiors.remove(Integer.valueOf(var2[1]));
								Database.mergeData(1);
								Database.save();
								var1.sendChatToPlayer("§aИнтерьер " + var2[1] + " удален!");
							} else {
								var1.sendChatToPlayer("§cИнтерьера " + var2[1] + " не существует!");
							}
						} else if (var2[0].startsWith("get")) {
							if (Database.interiors.containsKey(Integer.valueOf(var2[1]))) {
								var1.sendChatToPlayer("§aИнтерьер " + var2[1] + ". " + Database.interiors.get(Integer.valueOf(var2[1])).toString());
							} else {
								var1.sendChatToPlayer("§cИнтерьера " + var2[1] + " не существует!");
							}
						} else {
							var1.sendChatToPlayer("§cНедостаточно аргументов! /interior <add/rem/get> <int:id>");
						}
					} catch (Exception e) {
						var1.sendChatToPlayer("§cПроизошла ошибка! /interior <add/rem/get> <int:id>");
					}
				} else {
					var1.sendChatToPlayer("§cНедостаточно аргументов! /interior <add/rem/get> <int:id>");
				}
			}
		}
	}
}
