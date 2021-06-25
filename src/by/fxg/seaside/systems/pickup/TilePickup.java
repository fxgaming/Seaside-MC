package by.fxg.seaside.systems.pickup;

import java.util.HashMap;
import java.util.Map;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.based.BasedTileEntity;
import by.fxg.seaside.based.IActionable;
import by.fxg.seaside.managers.PlayerManager;
import by.fxg.seaside.storage.Coords;
import by.fxg.seaside.systems.Database;
import by.fxg.seaside.systems.houses.CommandHouse;
import by.fxg.seaside.systems.houses.House;
import by.fxg.seaside.systems.jobs.JobSystem;
import by.fxg.seaside.utils.ServerUtils;
import by.fxg.seaside.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TilePickup extends BasedTileEntity implements IActionable {
	public EnumPickup pickupType = EnumPickup.NONE;
	public int renderType = 0;
	public int extData = -1;
	public int extData1 = -1;
	public int extData2 = -1;
	public Coords tpTo = new Coords(0, 0, 0);
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.pickupType = EnumPickup.getByID(nbt.getInteger("pickupType"));
		this.renderType = nbt.getInteger("renderType");
		this.extData = nbt.getInteger("extData");
		this.extData1 = nbt.getInteger("extData1");
		this.extData2 = nbt.getInteger("extData2");
		this.tpTo = new Coords(nbt.getInteger("tx"), nbt.getInteger("ty"), nbt.getInteger("tz"));
    }

    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("pickupType", this.pickupType.ordinal());
        nbt.setInteger("renderType", this.renderType);
        nbt.setInteger("extData", this.extData);
        nbt.setInteger("extData1", this.extData1);
        nbt.setInteger("extData2", this.extData2);
        nbt.setInteger("tx", this.tpTo.x);
        nbt.setInteger("ty", this.tpTo.y);
        nbt.setInteger("tz", this.tpTo.z);
    }
	
	public boolean canUpdate() {
		return false;
	}
	
	
	public void onAction(EntityPlayer player, Coords c, boolean altKey) {
		if (Seaside.SERVER) {
			switch (this.pickupType) {
				case HOUSE: //ID, OUTDOOR, Reserve, coords
					if (Database.houses.containsKey(this.extData)) {
						if (altKey) {
							PlayerManager.get(player).virtualWorld = this.extData1 == 1 ? 10000 + Database.houses.get(this.extData).ID : -1;
							if (Database.houses.get(this.extData).isOpen) player.setPositionAndUpdate(this.tpTo.x, this.tpTo.y, this.tpTo.z);
							else player.sendChatToPlayer("§cДом закрыт!");
						} else {
							House house = Database.houses.get(this.extData);
							if (Database.houses.get(this.extData).owner.equals("none")) {
								Seaside.captcha.put(player.username, Utils.getRandom(100000, 999999));
								player.openGui(Seaside.instance, 1002, this.worldObj, ServerUtils.sendGuiData(player, new String[]{"Покупка дома"}, new String[]{"Для покупки дома введите", "капчу: " + Seaside.captcha.get(player.username)}, new String[]{"Ввести", "Отмена"}), 10000, this.extData);
							} else {
								boolean allowed = house.owner.equals(player.username) || house.living.contains(player.username);
								if (allowed) {
									String[][] data = {
											{"Меню дома " + this.extData}, {},
											house.owner.equals(player.username) ? 
											new String[]{house.isOpen ? "Закрыть дом" : "Открыть дом", (house.isOnRent ? "Закрыть" : "Открыть") + " аренду дома", "Изменить цену аренды", "Добавить жильца", "Убрать жильца", "Продать дом игроку", "Улучшить интерьер", "Статус дома"} :
											new String[]{house.isOpen ? "Закрыть дом" : "Открыть дом", "Перестать арендовывать дом", "Статус дома"},
											{"Выбрать", "Отмена"}
									};
									player.openGui(Seaside.instance, 1003, player.worldObj, ServerUtils.sendGuiData(player, data), 10001, this.extData);
								} else {
									//11.02.2021 всё что я смог написать за 3 часа из-за своего убитого состояния
									if (house.isOnRent) {
										
									}
									player.sendChatToPlayer("§cВы не владелец дома и тут не проживаете!");
								}
							}
						}
					} else {
						player.sendChatToPlayer("§cДанного дома не существует, обратитесь к администратору");
						player.sendChatToPlayer("§f[PE:" + this.extData + "," + this.extData1 + "," + this.extData2 + "]" + this.tpTo.toString() + "[P:" + player.username + "] (Сделайте скриншот с /time)");
					}
					break;
				case HOUSE_GARAGE: break;
				case BUSINESS: break;
				
				case BARBERSHOP: break;
				case GAS_STATION: break;
				case GET_BANK_CARD: break;
				case GET_PASSPORT: break;
				case INSURANCE_AGENCY: break;
				case INTERIOR_ENTER: break;
				case INTERIOR_EXIT: break;
				case JOB_CHANGE:
				case JOB_CLAIM:
				case JOB_GET:
				case JOB_PAYMENT:
				case JOB_SET:
				case JOB_WORK:
					JobSystem.instance.onWorkCheckpoint(player, this.extData, this.extData1, this.pickupType.ordinal());
					break;
				case PHARMACY: break;
				case PIZZASTALL: break;
				case PLASTIC_SURGERY: break;
			}
		}
	}
}
