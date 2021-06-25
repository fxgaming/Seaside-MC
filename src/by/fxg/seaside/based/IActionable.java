package by.fxg.seaside.based;

import by.fxg.seaside.storage.Coords;
import net.minecraft.entity.player.EntityPlayer;

public interface IActionable {
	public void onAction(EntityPlayer player, Coords pos, boolean altKey);
}
