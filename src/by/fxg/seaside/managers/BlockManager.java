package by.fxg.seaside.managers;

import by.fxg.seaside.systems.pickup.BlockPickup;
import by.fxg.seaside.systems.pickup.TilePickup;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class BlockManager {
	public static Block blockPickup;
	
	public BlockManager() {
		GameRegistry.registerBlock(this.blockPickup = new BlockPickup(500), "blockPickup");
		GameRegistry.registerTileEntity(TilePickup.class, "tilePickup");
		
	}
}
