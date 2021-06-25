package by.fxg.seaside.systems.jobs;

import java.util.HashMap;
import java.util.Map;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.managers.PlayerManager;
import by.fxg.seaside.network.SPacketMarker;
import by.fxg.seaside.storage.Coords;
import by.fxg.seaside.systems.GameLogging;
import by.fxg.seaside.systems.markers.Marker;
import by.fxg.seaside.systems.markers.MarkerColor;
import by.fxg.seaside.systems.pickup.EnumPickup;
import by.fxg.seaside.utils.ServerUtils;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;

public class JobSystem {
	public static JobSystem instance;
	private Map<Integer, Job> jobs = new HashMap<Integer, Job>();
	
	public JobSystem() {
		instance = this;
	}
	
	public boolean isWorking(int playerID) {
		return this.jobs.containsKey(playerID);
	}
	
	public boolean addToWork(int playerID, EnumJob job) {
		if (!this.jobs.containsKey(playerID)) {
			this.jobs.put(playerID, new Job(job, playerID));
			return true;
		}
		return false;
	}
	
	public boolean removeFromWork(int playerID) {
		if (this.jobs.containsKey(playerID)) {
			this.jobs.remove(playerID);
			return true;
		}
		return false;
	}

	public void onWorkCheckpoint(EntityPlayer player, int extData, int extData1, int ordinal) {
		PlayerManager pm = PlayerManager.get(player);
		int playerid = Seaside.getID(player.username);
		if (ordinal == EnumPickup.JOB_CLAIM.ordinal()) {
			if (pm.job == EnumJob.NONE) {
				pm.job = EnumJob.getByID(extData);
				pm.updateData(false);
				int key = ServerUtils.sendGuiData(player, new String[]{"Трудоустройство"}, new String[]{"§cВы уже трудоустроены!", "Увольтесь прежде чем устраиваться", "на новую работу!"}, new String[]{"Хорошо"});
				player.openGui(Seaside.instance, 1000, null, key, 0, 0);
			} else {
				int key = ServerUtils.sendGuiData(player, new String[]{"Трудоустройство"}, new String[]{"§aВы трудоустроились на новую работу!", "Информация - /mm -> Помощь -> Помощь по работе"}, new String[]{"Хорошо"});
				player.openGui(Seaside.instance, 1000, null, key, 0, 0);
			}
		} else if (ordinal == EnumPickup.JOB_CHANGE.ordinal()) {
			if (pm.job.ordinal() == extData) {
				if (this.isWorking(playerid)) {
					this.removeFromWork(playerid);
					//TODO: Сделать выдачу денег за окончание смены если работа такая
					int key = ServerUtils.sendGuiData(player, new String[]{"Работа"}, new String[]{"§aВы окончили рабочую смену."}, new String[]{"Хорошо"});
					player.openGui(Seaside.instance, 1000, null, key, 0, 0);
				} else {
					this.addToWork(playerid, pm.job);
					int key = ServerUtils.sendGuiData(player, new String[]{"Работа"}, new String[]{"§aВы начали рабочую смену.", "Информация - /mm -> Помощь -> Помощь по работе"}, new String[]{"Хорошо"});
					player.openGui(Seaside.instance, 1000, null, key, 0, 0);
				}
			} else {
				player.addChatMessage("§cВы не работаете здесь!");
			}
		} else if (ordinal == EnumPickup.JOB_PAYMENT.ordinal()) {
			if (pm.job.ordinal() == extData) {
				if (this.isWorking(playerid)) {
					if (this.jobs.get(playerid).vars.size() > 0 && (Integer)this.jobs.get(playerid).vars.get(0) > 0) {
						player.addChatMessage("§aВы получили " + (Integer)this.jobs.get(playerid).vars.get(0) + "$!");
						pm.money += (Integer)this.jobs.get(playerid).vars.get(0);
						this.jobs.get(playerid).vars.set(0, 0);
					} else {
						player.addChatMessage("§cВы не заработали денег!");
					}
				} else {
					player.addChatMessage("§cВы не выходили на смену!");
				}
			} else {
				player.addChatMessage("§cВы не работаете здесь!");
			}
		} else if (ordinal == EnumPickup.JOB_GET.ordinal()) {
			if (pm.job.ordinal() == extData) {
				
			} else {
				player.addChatMessage("§cВы не работаете здесь!");
			}
		} else if (ordinal == EnumPickup.JOB_SET.ordinal()) {
			if (pm.job.ordinal() == extData) {
				
			} else {
				player.addChatMessage("§cВы не работаете здесь!");
			}
		} else if (ordinal == EnumPickup.JOB_WORK.ordinal()) {
			if (pm.job.ordinal() == extData) {
				
			} else {
				player.addChatMessage("§cВы не работаете здесь!");
			}
		} else {
			
		}
//		if (ordinal == 14) { //claim
//			if (!isWorking) {
//				isWorking = true;
//				player.addChatMessage("§aвы приняты на работу подкидышем");
//			} else {
//				player.addChatMessage("§cты че дурачек, ты уже работаешь у нас");
//			}
//		} else if (ordinal == 15) {//change
//			if (!isChange) {
//				player.addChatMessage("§aдобро пожаловать на смену, раб\'о\'тяга");
//				isChange = true;
//			} else {
//				player.addChatMessage("§4не советовал бы так мало раб\'о\'тать, но, ладно");
//				isChange = get = false;
//			}
//		} else if (ordinal == 16) {//payment
//			if (mani > 0) {
//				player.addChatMessage("§eдержи твои " + mani + "$ за раскиданные " + (mani / 15) + " пицц");
//			} else {
//				player.addChatMessage("§cкакие деньги? ха-ха иди поработай сначала");
//			}
//		} else if (ordinal == 17) {//get
//			if (isChange) {
//				if (!get) {
//					player.sendChatToPlayer("§aтак молодец теперь пиздуй унеси эту хуйню на маркер");
//					get = true;
//					Marker mark = new Marker("dibil", MarkerColor.RED, new Coords(-166, 31, 260));
//					PacketDispatcher.sendPacketToPlayer(new SPacketMarker.Add(mark).get(), (Player)player);
//				} else {
//					player.sendChatToPlayer("§cнеси! ты уже взял одну");
//				}
//			} else {
//				player.sendChatToPlayer("§cа на смену выйти не хочешь?");
//			}
//		} else if (ordinal == 18) {//set
//			if (isChange) {
//				if (get) {
//					player.sendChatToPlayer("§aмолодец, 15$ за пиццу получаешь, иди дальше!");
//					mani += 15;
//					get = false;
//					PacketDispatcher.sendPacketToPlayer(new SPacketMarker.Remove("dibil").get(), (Player)player);
//				} else {
//					player.sendChatToPlayer("§cкакие деньги? ха-ха иди сначала возьми пиццу");
//				}
//			} else {
//				player.sendChatToPlayer("§cты кто!?!?!");
//			}
//		}
	}
}
