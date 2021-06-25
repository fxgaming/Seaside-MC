package by.fxg.seaside.systems.houses;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.based.BasedCommand;
import by.fxg.seaside.systems.Database;
import by.fxg.seaside.systems.pickup.TilePickup;
import by.fxg.seaside.utils.ServerUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class CommandHouse extends BasedCommand {
	public String getCommandName() {
		return "house";
	}

	public void processCommand(ICommandSender var1, String[] var2) {
		if (Seaside.SERVER) {
			if (var1 instanceof EntityPlayer) {
				EntityPlayer var3 = (EntityPlayer)var1;
				if (ServerUtils.getTileAtEntity(var3) != null && ServerUtils.getTileAtEntity(var3) instanceof TilePickup) {
					TilePickup tp = (TilePickup)ServerUtils.getTileAtEntity(var3);
					if (Database.houses.containsKey(tp.extData)) {
						House house = Database.houses.get(tp.extData);
						boolean allowed = house.owner.equals(var3.username) || house.living.contains(var3.username);
						if (allowed) {
							String[][] data = {
									{"Меню дома " + tp.extData}, {},
									house.owner.equals(var3.username) ? 
									new String[]{house.isOpen ? "Закрыть дом" : "Открыть дом", (house.isOnRent ? "Закрыть" : "Открыть") + " аренду дома", "Изменить цену аренды", "Добавить жильца", "Убрать жильца", "Продать дом игроку", "Улучшить интерьер", "Статус дома"} :
									new String[]{house.isOpen ? "Закрыть дом" : "Открыть дом", "Перестать арендовывать дом", "Статус дома"},
									{"Выбрать", "Отмена"}
							};
							int i = ServerUtils.sendGuiData(var3, data);
							var3.openGui(Seaside.instance, 1003, var3.worldObj, i, 10001, tp.extData);
						} else var1.sendChatToPlayer("§cВы не владелец дома и тут не проживаете!");
					} else {
						var1.sendChatToPlayer("§cДанного дома не существует, обратитесь к администратору");
						var1.sendChatToPlayer("§f[PE:" + tp.extData + "," + tp.extData1 + "," + tp.extData2 + "]" + tp.tpTo.toString() + "[P:" + var1.getCommandSenderName() + "] (Сделайте скриншот с /time)");
					}
				} else var1.sendChatToPlayer("§cВы должны находится рядом с домом!");
			}
		}
	}
}
