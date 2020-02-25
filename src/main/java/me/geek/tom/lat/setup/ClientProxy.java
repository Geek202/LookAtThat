package me.geek.tom.lat.setup;

import me.geek.tom.lat.block.hudsign.EditHudSignScreen;
import me.geek.tom.lat.modapi.CapabilityLATInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicReference;

public class ClientProxy implements IProxy {
    @Override
    public void init() {

    }

    public static void openHudSignGui(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            AtomicReference<String> msg = new AtomicReference<>();
            te.getCapability(CapabilityLATInfo.LAT_INFO_CAPABILITY).ifPresent(handler -> {
                msg.set(handler.getInfo());
            });
            Minecraft.getInstance().displayGuiScreen(new EditHudSignScreen(pos, world.dimension.getType())); // @TODO Request message from server.
        }
    }
}
