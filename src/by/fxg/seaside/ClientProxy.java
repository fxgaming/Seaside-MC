package by.fxg.seaside;

import by.fxg.seaside.amain.ClientTickHandler;
import by.fxg.seaside.amain.EventHandlerUI;
import by.fxg.seaside.amain.KeyLoader;
import by.fxg.seaside.managers.BlockManager;
import by.fxg.seaside.managers.ModelManager;
import by.fxg.seaside.systems.pickup.RenderPickup;
import by.fxg.seaside.systems.pickup.TilePickup;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends ServerProxy {
	public EventHandlerUI eventHandlerClient;
	public ClientTickHandler clientTickHandler;
	public ModelManager modelManager;
	
	public void preInit() {
		this.modelManager = new ModelManager();
		KeyBindingRegistry.registerKeyBinding(new KeyLoader());
		MinecraftForge.EVENT_BUS.register(eventHandlerClient = new EventHandlerUI());
		TickRegistry.registerTickHandler(clientTickHandler = new ClientTickHandler(), Side.CLIENT);
		
		
		ClientRegistry.bindTileEntitySpecialRenderer(TilePickup.class, new RenderPickup());
		MinecraftForgeClient.registerItemRenderer(BlockManager.blockPickup.blockID, new RenderPickup());
	}
}
