package by.fxg.seaside.systems.pickup;

import by.fxg.seaside.Seaside;
import by.fxg.seaside.based.BasedCommand;
import by.fxg.seaside.storage.Coords;
import by.fxg.seaside.utils.ALevel;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class CommandPickup extends BasedCommand {
	public String getCommandName() {
		return "pickup";
	}

	public void processCommand(ICommandSender var1, String[] var2) {
		if (Seaside.SERVER) {
			if (this.hasRights(var1, ALevel.SERVER)) {
				EntityPlayer var3 = (EntityPlayer)var1;
				if (!var3.worldObj.isRemote) {
					TileEntity tile = var3.worldObj.getBlockTileEntity((int)Math.floor(var3.posX), (int)Math.floor(var3.posY), (int)Math.floor(var3.posZ));
					if (tile != null && tile instanceof TilePickup) {
						if (var2.length >= 5) {
							try {
								((TilePickup)tile).pickupType = EnumPickup.getByID(Integer.valueOf(var2[0]));
								((TilePickup)tile).renderType = Integer.valueOf(var2[1]);
								((TilePickup)tile).extData = Integer.valueOf(var2[2]);
								((TilePickup)tile).extData1 = Integer.valueOf(var2[3]);
								((TilePickup)tile).extData2 = Integer.valueOf(var2[4]);
								((TilePickup)tile).tpTo = new Coords(var2[5].split(","));
								var3.worldObj.markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
								var1.sendChatToPlayer("§aПикап был изменен!");
							} catch (Exception e) {
								var1.sendChatToPlayer("§cПроизошла ошибка! /pickup <int:type> <int:render> <int:e0> <int:e1> <int:e2> <x,y,z>");
								e.printStackTrace();
							}
						} else if (var2.length == 0) {
							var1.sendChatToPlayer("§aПикап " + ((TilePickup)tile).pickupType.name() + "[" + ((TilePickup)tile).pickupType.ordinal() + "], [" + ((TilePickup)tile).extData + ", " + ((TilePickup)tile).extData1 + ", " + ((TilePickup)tile).extData2 + "], " + ((TilePickup)tile).tpTo.toString());
						} else {
							var1.sendChatToPlayer("§cНедостаточно аргументов! /pickup <int:type> <int:render> <int:e0> <int:e1> <int:e2> <x,y,z>");
						}
					} else {
						var1.sendChatToPlayer("§cВы находитесь вне пикапа!");
					}
				}
			}
		}
	}
}
