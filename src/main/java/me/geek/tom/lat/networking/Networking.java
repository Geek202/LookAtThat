package me.geek.tom.lat.networking;

import me.geek.tom.lat.LookAtThat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(LookAtThat.MODID, "lookatthat"), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(
                nextID(),
                PacketBlockType.class,
                PacketBlockType::toBytes,
                PacketBlockType::new,
                PacketBlockType::handle
        );

        INSTANCE.registerMessage(
                nextID(),
                PacketRequestBlockInfo.class,
                PacketRequestBlockInfo::toBytes,
                PacketRequestBlockInfo::new,
                PacketRequestBlockInfo::handle
        );

        INSTANCE.registerMessage(
                nextID(),
                PacketAdditionalData.class,
                PacketAdditionalData::toBytes,
                PacketAdditionalData::new,
                PacketAdditionalData::handle
        );
    }
}
