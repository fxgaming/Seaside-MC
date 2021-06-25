package by.fxg.seaside.systems.server;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.based.BasedCommand;
import by.fxg.seaside.managers.PlayerManager;
import by.fxg.seaside.systems.jobs.EnumJob;
import by.fxg.seaside.utils.ALevel;
import net.minecraft.command.ICommandSender;

public class CommandSetStat extends BasedCommand {
	public String getCommandName() {
		return "setstat";
	}
	
	public void processCommand(ICommandSender var1, String[] var2) {
		if (this.hasRights(var1, ALevel.EXEC_ADMINISTRATOR)) {
			if (var2.length >= 3) {
				try {
					if (Seaside.playerids[Integer.valueOf(var2[0])] != null) {
						System.out.println(Seaside.playerids[Integer.valueOf(var2[0])]);
						PlayerManager pm = PlayerManager.get(Seaside.playerids[Integer.valueOf(var2[0])]);
						switch (Integer.valueOf(var2[1])) {
							case 0: pm.name = var2[2]; break;
							case 1: pm.money = Integer.valueOf(var2[2]); break;
							case 2: pm.moneyBank = Integer.valueOf(var2[2]); break;
							case 3: pm.moneyDeposit = Integer.valueOf(var2[2]); break;
							case 4: pm.creditCard.enabled = Integer.valueOf(var2[2]) == 1; break;
							case 5: pm.creditCard.value = Integer.valueOf(var2[2]); break;
							case 6: pm.passport.enabled = Integer.valueOf(var2[2]) == 1; break;
							case 7: pm.passport.value = Integer.valueOf(var2[2]); break;
							case 8: pm.simcard.enabled = Integer.valueOf(var2[2]) == 1; break;
							case 9: pm.simcard.value = Integer.valueOf(var2[2]); break;
							case 10: pm.wantedLevel = Integer.valueOf(var2[2]); break;
							case 11: pm.job = EnumJob.getByID(Integer.valueOf(var2[2])); break;
						}
						pm.updateData(true);
						var1.sendChatToPlayer("§aДанные обновлены! [" + var2[0] + ", " + var2[1] + ", " + var2[2] + "][" + Seaside.playerids[Integer.valueOf(var2[0])] + ", " + var1.getCommandSenderName() + "]");
					} else {
						var1.sendChatToPlayer("§cИгрок с ID " + var2[0] + " оффлайн!");
					}
				} catch (Exception e) {
					var1.sendChatToPlayer("§cОшибка! /setstat <id> <код> <значение>");
				}
			} else {
				var1.sendChatToPlayer("");
				var1.sendChatToPlayer("§cНедостаточно аргументов! /setstat <id> <код> <значение>");
				var1.sendChatToPlayer("§c0 - Имя, 1 - Деньги, 2 - Деньги в банке, 3 - Деньги депозита,");
				var1.sendChatToPlayer("§c4 - Карта банка есть?, 5 - Карта пинкод, 6 - Пасспорт есть?,");
				var1.sendChatToPlayer("§c7 - Пасспорт номер, 8 - Сим-карта есть?, 9 - Сим-карта, 10 - розыск,");
				var1.sendChatToPlayer("§c11 - работа");
			}
		}
	}
}
