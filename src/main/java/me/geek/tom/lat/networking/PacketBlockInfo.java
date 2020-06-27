package me.geek.tom.lat.networking;

import me.geek.tom.lat.blockinfo.api.BlockInformation;
import me.geek.tom.lat.setup.ClientEventHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketBlockInfo {
    private final BlockInformation info;

    public PacketBlockInfo(PacketBuffer buf) {
        this.info = BlockInformation.fromPacket(buf);
    }

    public void toBytes(PacketBuffer buf) {
        this.info.serializeToPacket(buf);
    }

    public PacketBlockInfo(BlockInformation info) {
        this.info = info;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientEventHandler.getRenderer().currentBlockInfo = info;
        });
        ctx.get().setPacketHandled(true);
    }
}
