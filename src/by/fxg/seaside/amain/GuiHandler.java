package by.fxg.seaside.amain;

import java.util.HashMap;
import java.util.Map;

import by.fxg.sampui.client.ISAMPCallback;
import by.fxg.sampui.client.ISAMPDialog;
import by.fxg.sampui.client.SampDialogInput;
import by.fxg.sampui.client.SampDialogList;
import by.fxg.sampui.client.SampDialogText;
import by.fxg.seaside.Seaside;
import by.fxg.seaside.managers.PlayerManager;
import by.fxg.seaside.network.SPacketServerDialog;
import by.fxg.seaside.storage.SellQuery;
import by.fxg.seaside.systems.Database;
import by.fxg.seaside.systems.houses.House;
import by.fxg.seaside.utils.SQType;
import by.fxg.seaside.utils.ServerUtils;
import by.fxg.seaside.utils.Utils;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler, ISAMPCallback {
	public static GuiHandler instance;
	public Map<Integer, String[][]> guiData = new HashMap<Integer, String[][]>();
	
	public GuiHandler() {
		instance = this;
	}
	
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID > 999) {
			return new Container(){public boolean canInteractWith(EntityPlayer e){return true;}};
		}
		return null;
	}

	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case 1000: 
				SampDialogText dialog0 = new SampDialogText(y, this.guiData.get(x)[0][0], this.guiData.get(x)[1], this.guiData.get(x)[2][0]).setSubData(z).addCallback(this);
				this.guiData.remove(x);
				return dialog0;
			case 1001:
			case 1002:
				SampDialogInput dialog2 = new SampDialogInput(y, this.guiData.get(x)[0][0], this.guiData.get(x)[1], this.guiData.get(x)[2][0], this.guiData.get(x)[2][1]).setSubData(z).addCallback(this);
				this.guiData.remove(x);
				return dialog2;
			case 1003:
				SampDialogList dialog3 = new SampDialogList(y, this.guiData.get(x)[0][0], this.guiData.get(x)[1], this.guiData.get(x)[2], this.guiData.get(x)[3][0], this.guiData.get(x)[3][1]).setSubData(z).addCallback(this);
				this.guiData.remove(x);
				return dialog3;
		}
		return null;
	}

	public void onButtonUse(ISAMPDialog dialog, int dialogID, int buttonID, int mouseID, Object... data) {
		if (dialogID == 0) {
			Minecraft.getMinecraft().thePlayer.closeScreen();
		} else {
			if (dialogID >= 10000 && dialogID <= 10010) {
				Minecraft.getMinecraft().thePlayer.closeScreen();
				PacketDispatcher.sendPacketToServer(new SPacketServerDialog(dialogID, buttonID, mouseID, (Integer)data[0], (dialog instanceof SampDialogList ? (Integer)data[1] : 0), (dialog instanceof SampDialogInput ? (String)data[1] : "")).get());
			}
		}
	}
	
	public void onButtonServerUse(EntityPlayer player, int dialogID, int buttonID, int mouseID, int ext, int index, String inputData) {
		if (Seaside.SERVER) {
			PlayerManager pm = PlayerManager.get(player);
			if (dialogID == 10000 && buttonID == 0) { //Покупка дома, каптча
				if (Seaside.captcha.containsKey(player.username) && Database.houses.containsKey(ext)) {
					if (("" + Seaside.captcha.get(player.username)).equals(inputData)) {
						if (Database.houses.get(ext).owner.equals("none") && pm.money >= Database.houses.get(ext).govPrice) {
							Seaside.captcha.remove(player.username);
							Database.houses.get(ext).owner = player.username;
							Database.mergeData(2);
							Database.save();
							pm.money -= Database.houses.get(ext).govPrice;
							pm.updateData(false);
							player.sendChatToPlayer("§aВы купили дом " + ext + " по государвственной цене за " + Database.houses.get(ext).govPrice + "$");
						} else {
							Seaside.captcha.remove(player.username);
							player.sendChatToPlayer("§cУ вас недостаточно денег!");
						}
					} else {
						Seaside.captcha.remove(player.username);
						player.sendChatToPlayer("§cКапча введена неверно! Попробуйте еще раз.");
					}
				} else player.sendChatToPlayer("§cПроизошла ошибка, попробуйте еще раз!");
			} else if (dialogID == 10001 && buttonID == 0 || buttonID == 2) { //Меню дома
				if (Database.houses.containsKey(ext)) {
					boolean isOwner = Database.houses.get(ext).owner.equals(player.username);
					boolean isLiving = isOwner || Database.houses.get(ext).living.contains(player.username);
					if (index == 0) {//Закрыть/открыть дом x2
						if (isLiving) {
							Database.houses.get(ext).isOpen = !Database.houses.get(ext).isOpen;
							Database.mergeData(2);
							Database.save();
						} else player.sendChatToPlayer("§cВы не владелец дома и тут не проживаете!");
					} else if (index == 1) {//Открыть/закрыть аренду | Перестать арендовывать дом
						if (isOwner) {
							Database.houses.get(ext).isOnRent = !Database.houses.get(ext).isOnRent;
							Database.mergeData(2);
							Database.save();
						} else if (isLiving) {
							
						} else player.sendChatToPlayer("§cВы не владелец дома и тут не проживаете!");
					} else if (index == 2 && isOwner) {//Изменить цену аренды | Статус дома
						
					} else if (index == 3) {//Добавить жильца
						if (isOwner) {
							
						} else player.sendChatToPlayer("§cВы не владелец дома!");
					} else if (index == 4) {//Убрать жильца
						if (isOwner) {
							
						} else player.sendChatToPlayer("§cВы не владелец дома!");
					} else if (index == 5) {//Продать дом игроку
						if (isOwner) {
							player.openGui(Seaside.instance, 1002, player.worldObj, ServerUtils.sendGuiData(player, new String[][]{{"Продажа дома"}, {"Введите сумму и ID игрока через запятую."}, {"Далее", "Отмена"}}), 10004, ext);
						} else player.sendChatToPlayer("§cВы не владелец дома!");
					} else if (index == 6) {//Улучшить интерьер
						if (isOwner) {
							//TODO: Смена интерьеров
						} else player.sendChatToPlayer("§cВы не владелец дома!");
					} else if (index == 7 || index == 2 && isLiving) {
						if (isLiving) {
							House house = Database.houses.get(ext);
							String[][] data = {
								{"Статус дома " + house.ID + ", [" + (house.isOpen ? "§aОткрыт" : "§cЗакрыт") + "§r]"}, 
								{"Класс " + house.classID + ", Интерьер: " + house.interiorID, 
								"Гос. Стоимость: §a" + house.govPrice + "$", 
								"Владелец: " + (house.owner.equals("none") ? "Государство" : house.owner),
								house.isOnRent ?  "Жильцы: (" + house.living.size() + "/" + house.classID + ")" : "Дом не арендуется",
								"Стоимость аренды: §a" + house.rentPrice + "$"}, {"Ок"}
							};
							player.openGui(Seaside.instance, 1000, player.worldObj, ServerUtils.sendGuiData(player, data), 0, house.ID);
						} else player.sendChatToPlayer("§cВы не владелец дома и тут не проживаете!");
					}
				} else player.sendChatToPlayer("§cПроизошла ошибка, попробуйте еще раз!");
			} else if (dialogID == 10002) {//Добавление жильца
				
			} else if (dialogID == 10003) {//Удаление жильца
				
			} else if (dialogID == 10004) {//Отправка покупки дома с рук
				if (Database.houses.containsKey(ext) && Database.houses.get(ext).owner.equals(player.username)) {
					if (!Seaside.sellerQuery.containsKey(player.username) && !Seaside.buyerQuery.containsKey(player.username)) {
						try {
							int sum = Integer.valueOf(inputData.split(",")[0].replace(" ", ""));
							int id = Integer.valueOf(inputData.split(",")[1].replace(" ", ""));
							if (Seaside.playerids[id] != null && player.worldObj.getPlayerEntityByName(Seaside.playerids[id]) != null) {
								if (player.getDistanceToEntity(player.worldObj.getPlayerEntityByName(Seaside.playerids[id])) < 2.0F) {
									if (!Seaside.sellerQuery.containsKey(Seaside.playerids[id]) && !Seaside.buyerQuery.containsKey(Seaside.playerids[id])) {
										SellQuery query = new SellQuery(SQType.HOUSE, player.username, Seaside.playerids[id], sum, ext, 0);
										Seaside.sellerQuery.put(player.username, query);
										Seaside.buyerQuery.put(Seaside.playerids[id], query);
										player.openGui(Seaside.instance, 1000, player.worldObj, ServerUtils.sendGuiData(player, new String[][]{{"Продажа дома"}, {"Вы предложили " + Seaside.playerids[id] + " купить", "ваш дом за §a" + sum + "$"}, {"Ок"}}), 0, 0);
										Seaside.captcha.put(Seaside.playerids[id], Utils.getRandom(100000, 999999));
										player.worldObj.getPlayerEntityByName(Seaside.playerids[id]).openGui(Seaside.instance, 1002, player.worldObj, ServerUtils.sendGuiData(player.worldObj.getPlayerEntityByName(Seaside.playerids[id]), new String[][]{{"Покупка дома"}, {player.username + " предложил вам купить", "у него дом за §a" + sum + "$", "Для покупки введите капчу " + Seaside.captcha.get(Seaside.playerids[id])}, {"Далее", "Отмена"}}), 10005, ext);
									} else player.sendChatToPlayer("§cИгрок имеет активные предложения покупки/продажи!");
								} else player.sendChatToPlayer("§cИгрок слишком далеко от вас!");
							} else player.sendChatToPlayer("§cИгрок с ID " + id + " оффлайн!");
						} catch (Exception e) {
							player.sendChatToPlayer("§cНужно вводить сумму и ID игрока через запятую!");
							player.sendChatToPlayer("§cПример: [100000,5] или [2000000, 10]");
						}
					} else {
						player.sendChatToPlayer("§cУ вас уже есть активное предложение покупки/продажи!");
						player.sendChatToPlayer("§cИспользуйте /cleardeals для очистки активных предложении!");
					}
				} else player.sendChatToPlayer("§cВы не владелец дома!");
			} else if (dialogID == 10005) {//Покупка дома с рук, капча
				if (buttonID == 0) {
					if (Seaside.captcha.containsKey(player.username) && Database.houses.containsKey(ext) && Seaside.buyerQuery.containsKey(player.username)) {
						if (("" + Seaside.captcha.get(player.username)).equals(inputData)) {
							SellQuery sellQuery = Seaside.buyerQuery.get(player.username);
							if (Database.houses.get(ext).owner.equals(sellQuery.seller) && pm.money >= sellQuery.price) {
								Seaside.captcha.remove(player.username);
								PlayerManager spm = PlayerManager.get(sellQuery.seller);
								pm.money -= sellQuery.price;
								spm.money += sellQuery.price;
								Database.houses.get(ext).owner = player.username;
								Database.houses.get(ext).isOnRent = false;
								Database.houses.get(ext).living.clear();
								Database.mergeData(2);
								Database.save();
								pm.updateData(false);
								spm.updateData(false);
								Seaside.sellerQuery.remove(sellQuery.seller);
								Seaside.buyerQuery.remove(player.username);
								player.sendChatToPlayer("§aВы успешно купили дом у игрока " + sellQuery.seller + " за §a" + sellQuery.price + "$");
								spm.player.sendChatToPlayer("§aВы успешно продали дом игроку " + player.username +" за §a" + sellQuery.price + "$");
							} else {
								player.sendChatToPlayer("§cУ вас недостаточно денег!");
								Seaside.captcha.remove(player.username); Seaside.sellerQuery.remove(sellQuery.seller); Seaside.buyerQuery.remove(player.username);
								player.worldObj.getPlayerEntityByName(sellQuery.seller).sendChatToPlayer("§cИгрок " + player.username +" не смог принять предложение покупки дома!");
							}
						} else {
							player.sendChatToPlayer("§cКапча введена неверно! Попробуйте еще раз.");
							if (Seaside.buyerQuery.containsKey(player.username)) {
								Seaside.captcha.remove(player.username); SellQuery sellQuery = Seaside.buyerQuery.get(player.username); Seaside.sellerQuery.remove(sellQuery.seller); Seaside.buyerQuery.remove(player.username);
								player.worldObj.getPlayerEntityByName(sellQuery.seller).sendChatToPlayer("§cИгрок " + player.username +" не смог принять предложение покупки дома!");
							}
						}
					} else {
						player.sendChatToPlayer("§cПроизошла ошибка, попробуйте еще раз!");
						if (Seaside.buyerQuery.containsKey(player.username)) {
							Seaside.captcha.remove(player.username); SellQuery sellQuery = Seaside.buyerQuery.get(player.username); Seaside.sellerQuery.remove(sellQuery.seller); Seaside.buyerQuery.remove(player.username);
							player.worldObj.getPlayerEntityByName(sellQuery.seller).sendChatToPlayer("§cИгрок " + player.username +" не смог принять предложение покупки дома!");
						}
					}
				} else {
					if (Seaside.captcha.containsKey(player.username) && Database.houses.containsKey(ext) && Seaside.buyerQuery.containsKey(player.username)) {
						Seaside.captcha.remove(player.username);
						SellQuery sellQuery = Seaside.buyerQuery.get(player.username);
						Seaside.sellerQuery.remove(sellQuery.seller);
						Seaside.buyerQuery.remove(player.username);
						player.worldObj.getPlayerEntityByName(sellQuery.seller).sendChatToPlayer("§cИгрок " + player.username +" отказался от предложения покупки дома!");
						player.sendChatToPlayer("§cВы отказались от предложения игрока " + sellQuery.seller + " о покупке дома!");
					} else player.sendChatToPlayer("§cПроизошла ошибка, попробуйте еще раз!");
				}
			}
		}
	}
}
