package me.geek.tom.lat.networking;

import me.geek.tom.lat.blockinfo.InformationGatherer;
import me.geek.tom.lat.blockinfo.api.BlockInformation;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class PacketRequestBlockInfo {
    private final RegistryKey<World> dim;
    private final BlockPos pos;

    public PacketRequestBlockInfo(PacketBuffer buf) {
        dim = RegistryKey.func_240903_a_(Registry.WORLD_KEY, buf.readResourceLocation());
        pos = buf.readBlockPos();
    }

    public PacketRequestBlockInfo(RegistryKey<World> type, BlockPos pos) {
        this.dim = type;
        this.pos = pos;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeResourceLocation(dim.func_240901_a_());
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerWorld world = ctx.get().getSender().world.getServer().getWorld(dim);

            BlockInformation info = InformationGatherer.gatherInformation(world, pos, ctx.get().getSender());
            Networking.INSTANCE.sendTo(
                    new PacketBlockInfo(info),
                    ctx.get().getSender().connection.netManager,
                    NetworkDirection.PLAY_TO_CLIENT
            );
        });
    }
}
