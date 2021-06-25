package by.fxg.seaside.systems.houses;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.based.BasedCommand;
import by.fxg.seaside.managers.PlayerManager;
import by.fxg.seaside.systems.Database;
import by.fxg.seaside.utils.ALevel;
import by.fxg.seaside.utils.ServerUtils;
import net.minecraft.command.ICommandSender;

public class CommandManageHouse extends BasedCommand {
	public String getCommandName() {
		return "mhouse";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		if (Seaside.SERVER) {
			if (this.hasRights(var1, ALevel.SERVER)) {
				try {
					if (var2.length >= 4) {
						if (var2[0].startsWith("add")) {
							if (Database.houses.containsKey(Integer.valueOf(var2[1]))) {
								var1.sendChatToPlayer("§cДом " + var2[1] + " уже существует!");
							} else {
								Database.houses.put(Integer.valueOf(var2[1]), new House(Integer.valueOf(var2[1]), Integer.valueOf(var2[2]), Integer.valueOf(var2[3])));
								Database.mergeData(2);
								Database.save();
								var1.sendChatToPlayer("§aДом создан. ID: " + var2[1] + ", Класс: " + var2[2] + ", Интерьер: " + var2[3]);
							}
						} else {
							var1.sendChatToPlayer("§cНедостаточно аргументов! /mhouse <add/rem/get> <int:id> <int:class> <int:interior>");
						}
					} else if (var2.length == 2 && var2[0].startsWith("rem")) {
						if (Database.houses.containsKey(Integer.valueOf(var2[1]))) {
							Database.houses.remove(Integer.valueOf(var2[1]));
							Database.mergeData(2);
							Database.save();
							var1.sendChatToPlayer("§aДом " + var2[1] + " удален!");
						} else {
							var1.sendChatToPlayer("§cДома " + var2[1] + " не существует!");
						}
					} else if (var2.length == 2 && var2[0].startsWith("get")) {
						if (Database.houses.containsKey(Integer.valueOf(var2[1]))) {
							House h = Database.houses.get(Integer.valueOf(var2[1]));
							var1.sendChatToPlayer("§aДом " + h.ID + ", Класс: " + h.classID + ", Интерьер: " + h.interiorID);
							if (h.owner.equals("none")) var1.sendChatToPlayer("§aВладелец: отсутствует");
							else {
								PlayerManager pm = PlayerManager.get(h.owner);
								if (pm != null) {
									var1.sendChatToPlayer("§aВладелец: " + h.owner + "[" + pm.name + "]");
								} else {
									var1.sendChatToPlayer("§cКритическая ошибка mhouse->pm null");
								}
							}
						} else {
							var1.sendChatToPlayer("§cДома " + var2[1] + " не существует!");
						}
					} else {
						var1.sendChatToPlayer("§cНедостаточно аргументов! /mhouse <add/rem/get> <int:id> <int:class> <int:interior>");
					}
				} catch (Exception e) {
					var1.sendChatToPlayer("§cПроизошла ошибка! /mhouse <add/rem/get> <int:id> <int:class> <int:interior>");
				}
			}
		}
	}
}
