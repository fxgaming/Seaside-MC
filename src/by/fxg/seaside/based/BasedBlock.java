package by.fxg.seaside.based;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BasedBlock extends BlockContainer {
	public BasedBlock(int par1, Material par3Material) {
		super(par1, par3Material);
	}

	@Override
	 public void onBlockPlacedBy(World w, int x, int y, int z, EntityLiving e) {
		TileEntity tile = w.getBlockTileEntity(x, y, z);
		if (tile != null && tile instanceof BasedTileEntity) {
			w.markBlockForUpdate(x, y, z);
		}
	}
	
	public void breakBlock(World w, int x, int y, int z, int m, int par6) {
		super.breakBlock(w, x, y, z, m, par6);
		if (w.blockHasTileEntity(x, y, z)) {
			w.removeBlockTileEntity(x, y, z);
		}
	}
	
	public TileEntity createNewTileEntity(World world, int metadata) {
		return null;
	}

	public TileEntity createNewTileEntity(World var1) {
		return null;
	}
}
