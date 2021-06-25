package by.fxg.seaside.systems.pickup;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.based.BasedBlock;
import by.fxg.seaside.network.SPacketHouseInfo;
import by.fxg.seaside.storage.Coords;
import by.fxg.seaside.systems.Database;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockPickup extends BasedBlock {
	public BlockPickup(int par1) {
		super(par1, Material.leaves);
		this.setBlockName("blockPickup");
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.isBlockContainer = true;
	}

	public void onEntityCollidedWithBlock(World w, int x, int y, int z, Entity e) {
		if (Seaside.SERVER) {
			if (!w.isRemote && e instanceof EntityPlayer) {
				if (w.getBlockTileEntity(x, y, z) != null && w.getBlockTileEntity(x, y, z) instanceof TilePickup) {
					if (((TilePickup)w.getBlockTileEntity(x, y, z)).pickupType == EnumPickup.HOUSE && ((TilePickup)w.getBlockTileEntity(x, y, z)).extData1 == 1) {
						if (Database.houses.containsKey(((TilePickup)w.getBlockTileEntity(x, y, z)).extData)) {
							PacketDispatcher.sendPacketToPlayer(new SPacketHouseInfo(Database.houses.get(((TilePickup)w.getBlockTileEntity(x, y, z)).extData), new Coords(x, y + 1, z)).get(), (Player)e);
						}
					}
				}
			}
		}
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }	

	public TileEntity createNewTileEntity(World world) {
		return null;
	}
	
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TilePickup();
	}

	public boolean renderAsNormalBlock() {
		return false;
	}
	
	public boolean isOpaqueCube() {
		return false;
	}
	
	public int getRenderType() {
        return -1;
    }
}
