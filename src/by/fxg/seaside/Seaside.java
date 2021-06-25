package by.fxg.seaside;

import java.util.HashMap;
import java.util.Map;

import by.fxg.seaside.amain.EventHandlerSystem;
import by.fxg.seaside.amain.GuiHandler;
import by.fxg.seaside.amain.ServerTickHandler;
import by.fxg.seaside.amain.ThreadTimer;
import by.fxg.seaside.managers.BlockManager;
import by.fxg.seaside.network.SeasidePackets;
import by.fxg.seaside.storage.SellQuery;
import by.fxg.seaside.systems.SystemsManager;
import by.fxg.seaside.systems.chatsystem.CommandB;
import by.fxg.seaside.systems.chatsystem.CommandDo;
import by.fxg.seaside.systems.chatsystem.CommandMe;
import by.fxg.seaside.systems.chatsystem.CommandS;
import by.fxg.seaside.systems.chatsystem.CommandTodo;
import by.fxg.seaside.systems.chatsystem.CommandTry;
import by.fxg.seaside.systems.houses.CommandHouse;
import by.fxg.seaside.systems.houses.CommandInterior;
import by.fxg.seaside.systems.houses.CommandManageHouse;
import by.fxg.seaside.systems.jobs.JobSystem;
import by.fxg.seaside.systems.pickup.CommandPickup;
import by.fxg.seaside.systems.server.CommandClearDeals;
import by.fxg.seaside.systems.server.CommandGetpos;
import by.fxg.seaside.systems.server.CommandMarker;
import by.fxg.seaside.systems.server.CommandSetAdministrativeLevel;
import by.fxg.seaside.systems.server.CommandSetStat;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "Seaside")
@NetworkMod(clientSideRequired = false, serverSideRequired = false, channels = {"ss"}, packetHandler = SeasidePackets.class)
public class Seaside implements IPlayerTracker {
	public static final boolean SERVER = true;
	public static final boolean DEBUG = true;
	@Instance("Seaside")
	public static Seaside instance;
	@SidedProxy(clientSide = "by.fxg.seaside.ClientProxy", serverSide = "by.fxg.seaside.ServerProxy")
	public static ServerProxy proxy;

	public GuiHandler guiHandler;
	public SystemsManager systemsManager;
	public BlockManager blockManager;
	
	public ServerTickHandler serverTickHandler;
	public EventHandlerSystem eventHandlerSystem;
	
	@PreInit
	public void onPreInit(FMLPreInitializationEvent event) {
		instance = this;
		GameRegistry.registerPlayerTracker(this);
		NetworkRegistry.instance().registerGuiHandler(this, this.guiHandler = new GuiHandler());
		this.systemsManager = new SystemsManager().register();
		this.blockManager = new BlockManager();
		
		MinecraftForge.EVENT_BUS.register(eventHandlerSystem = new EventHandlerSystem());
		TickRegistry.registerTickHandler(serverTickHandler = new ServerTickHandler(), Side.SERVER);
		new ThreadTimer().start();
		
		
		proxy.preInit();
	}
	
	@ServerStarting
	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandPickup());
		
		event.registerServerCommand(new CommandB());
		event.registerServerCommand(new CommandS());
		event.registerServerCommand(new CommandDo());
		event.registerServerCommand(new CommandMe());
		event.registerServerCommand(new CommandTry());
		event.registerServerCommand(new CommandTodo());
		
		event.registerServerCommand(new CommandMarker());
		event.registerServerCommand(new CommandGetpos());
		event.registerServerCommand(new CommandSetStat());
		event.registerServerCommand(new CommandSetAdministrativeLevel());
		event.registerServerCommand(new CommandClearDeals());
		
		event.registerServerCommand(new CommandHouse());
		event.registerServerCommand(new CommandManageHouse());
		event.registerServerCommand(new CommandInterior());
	}
	
	public static final int MAX_PLAYERS = 1000;
	public static String[] playerids = new String[MAX_PLAYERS];
	public static int getID(String login) {
		for (int i = 0; i != playerids.length; i++) {
			if (playerids[i] != null && playerids[i].equals(login)) {
				return i;
			}
		}
		return -1;
	}
	
	public void onPlayerLogin(EntityPlayer player) {
		for (int i = 0; i != MAX_PLAYERS; i++) {
			if (playerids[i] == null) {
				playerids[i] = player.username;
				return;
			}
		}
	}

	public void onPlayerLogout(EntityPlayer player) {
		int playerid = getID(player.username);
		if (playerid >= 0) {
			playerids[playerid] = null;
			if (JobSystem.instance.isWorking(playerid)) {
				JobSystem.instance.removeFromWork(playerid);
			}
		}
	}

	public void onPlayerRespawn(EntityPlayer player) {

	}

	public void onPlayerChangedDimension(EntityPlayer player) {}
	
	public static Map<String, Integer> captcha = new HashMap<String, Integer>();
	public static Map<String, SellQuery> sellerQuery = new HashMap<String, SellQuery>();
	public static Map<String, SellQuery> buyerQuery = new HashMap<String, SellQuery>();
}
