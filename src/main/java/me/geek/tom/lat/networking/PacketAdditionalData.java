package me.geek.tom.lat.networking;

import me.geek.tom.lat.setup.ClientEventHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAdditionalData {
    private final String[] data;

    public PacketAdditionalData(PacketBuffer buf) {
        int len = buf.readInt();
        this.data = new String[len];
        for (int i = 0; i < len; i++) {
            this.data[i] = buf.readString();
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(data.length);
        for (String s : data) {
            buf.writeString(s);
        }
    }

    public PacketAdditionalData(String... data) {
        this.data = data;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientEventHandler.getRenderer().setHasAdditionalData(true);
            ClientEventHandler.getRenderer().setAdditionalData(this.data);
        });
        ctx.get().setPacketHandled(true);
    }
}
