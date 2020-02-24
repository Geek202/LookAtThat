package me.geek.tom.lat.networking;

import me.geek.tom.lat.modapi.CapabilityLATInfo;
import me.geek.tom.lat.modapi.IProvidesLATInfo;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateSignText {
    private final String message;
    private final BlockPos pos;
    private final DimensionType dimension;

    public PacketUpdateSignText(String message, BlockPos pos, DimensionType dimension) {
        this.message = message;
        this.pos = pos;
        this.dimension = dimension;
    }

    public PacketUpdateSignText(PacketBuffer buf) {
        this.message = buf.readString();
        this.pos = buf.readBlockPos();
        this.dimension = DimensionType.getById(buf.readInt());
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeString(this.message);
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.dimension.getId());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerWorld world = null;
            for (ServerWorld w : ctx.get().getSender().world.getServer().getWorlds())
                if (w.getDimension().getType().equals(this.dimension))
                    world = w;
            if (world == null)
                return;
            TileEntity te = world.getTileEntity(pos);

            if (te == null)
                throw new RuntimeException("No tileentity at the position recieved! This indicates someone is messing with packets!");

            te.getCapability(CapabilityLATInfo.LAT_INFO_CAPABILITY).ifPresent((hanlder) -> {
                hanlder.setMessage(this.message);
            });
        });
    }
}
