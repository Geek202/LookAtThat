package me.geek.tom.lat.networking;

import me.geek.tom.lat.modapi.CapabilityLATInfo;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateSignText {
    private final String message;
    private final BlockPos pos;
    private final RegistryKey<World> dimension;

    public PacketUpdateSignText(String message, BlockPos pos, RegistryKey<World> dimension) {
        this.message = message;
        this.pos = pos;
        this.dimension = dimension;
    }

    public PacketUpdateSignText(PacketBuffer buf) {
        this.message = buf.readString(32767);
        this.pos = buf.readBlockPos();
        this.dimension = RegistryKey.func_240903_a_(Registry.WORLD_KEY, buf.readResourceLocation());
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeString(this.message);
        buf.writeBlockPos(this.pos);
        buf.writeResourceLocation(this.dimension.func_240901_a_());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerWorld world = ctx.get().getSender().world.getServer().getWorld(this.dimension);
            TileEntity te = world.getTileEntity(pos);

            if (te == null)
                throw new RuntimeException("No tileentity at the position recieved! This suggests that someone is messing with packets!");

            te.getCapability(CapabilityLATInfo.LAT_INFO_CAPABILITY).ifPresent((handler) ->
                    handler.setMessage(this.message));
        });
    }
}
