package by.fxg.seaside.managers;

import by.fxg.seaside.network.SPacketSendPlayerData;
import by.fxg.seaside.storage.TProperty;
import by.fxg.seaside.systems.jobs.EnumJob;
import by.fxg.seaside.utils.ALevel;
import by.fxg.seaside.utils.ServerUtils;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerManager implements IExtendedEntityProperties {
	public final EntityPlayer player;
	public String login;
	public String name;
	public int money;
	public int moneyBank;
	public int moneyDeposit;
	public TProperty<Integer> creditCard;
	public TProperty<Integer> passport;
	public TProperty<Integer> simcard;
	public int wantedLevel;
	
	public EnumJob job;
	
	//public List<House> houses;
	//public List<Business> businesses;
	//public List<Cars> cars;
	public ALevel alevel;
	
	public int virtualWorld = -1;
	
	public PlayerManager(EntityPlayer player) {
		this.player = player;
		this.name = "Irricada_Nakamuro";//заменить блять
		this.money = 0;
		this.moneyBank = 0;
		this.moneyDeposit = 0;
		this.creditCard = new TProperty<Integer>(0, false);
		this.passport = new TProperty<Integer>(0, false);
		this.simcard = new TProperty<Integer>(0, false);
		this.wantedLevel = 0;
		this.job = EnumJob.NONE;
		this.alevel = ALevel.NONE;
	}
	
	public void updateData(boolean toAll) {
		if (toAll) {
			PacketDispatcher.sendPacketToAllPlayers(new SPacketSendPlayerData(this).get());
		} else {
			PacketDispatcher.sendPacketToPlayer(new SPacketSendPlayerData(this).get(), (Player)this.player);
		}
	}
	
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound data = new NBTTagCompound();
		data.setString("name", this.name);
		data.setInteger("money", this.money);
		data.setInteger("moneyBank", this.moneyBank);
		data.setInteger("moneyDeposit", this.moneyDeposit);
		data.setInteger("tpCredit0", this.creditCard.value);
		data.setBoolean("tpCredit1", this.creditCard.enabled);
		data.setInteger("tpPassport0", this.passport.value);
		data.setBoolean("tpPassport1", this.passport.enabled);
		data.setInteger("tpSimCard0", this.simcard.value);
		data.setBoolean("tpSimCard1", this.simcard.enabled);
		data.setInteger("wantedLevel", this.wantedLevel);
		data.setInteger("job", this.job.ordinal());
		
		data.setInteger("alevel", this.alevel.ordinal());
		compound.setCompoundTag("pmeData", data);
	}

	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound data = compound.getCompoundTag("pmeData");
		this.login = data.getString("login");
		this.name = data.getString("name");
		this.money = data.getInteger("money");
		this.moneyBank = data.getInteger("money");
		this.moneyDeposit = data.getInteger("money");
		this.creditCard = new TProperty<Integer>(data.getInteger("tpCredit0"), data.getBoolean("tpCredit1"));
		this.passport = new TProperty<Integer>(data.getInteger("tpPassport0"), data.getBoolean("tpPassport1"));
		this.simcard = new TProperty<Integer>(data.getInteger("tpSimCard0"), data.getBoolean("tpSimCard1"));
		this.wantedLevel = data.getInteger("wantedLevel");
		this.job = EnumJob.getByID(data.getInteger("job"));
		
		this.alevel = ALevel.getByID(data.getInteger("alevel"));
	}

	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties("pme", new PlayerManager(player));
	}

	public static final PlayerManager get(EntityPlayer player) {
		return (PlayerManager)player.getExtendedProperties("pme");
	}
	
	public static final PlayerManager get(String player) {
		return ServerUtils.getPlayer(player) == null ? null : (PlayerManager)ServerUtils.getPlayer(player).getExtendedProperties("pme");
	}
	
	public void init(Entity entity, World world) {
	}
}
