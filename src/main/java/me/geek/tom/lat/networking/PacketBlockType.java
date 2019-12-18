package me.geek.tom.lat.networking;

import me.geek.tom.lat.setup.ClientEventHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class PacketBlockType {
    private final ResourceLocation block;
    private final boolean isItem;

    public PacketBlockType(PacketBuffer buf) {
        this.block = new ResourceLocation(buf.readString());
        this.isItem = buf.readBoolean();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeString(this.block.toString());
        buf.writeBoolean(isItem);
    }

    public PacketBlockType(ResourceLocation block, boolean isItem) {
        this.isItem = isItem;
        this.block = block;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (this.isItem) {
                Item item = ForgeRegistries.ITEMS.getValue(this.block);
                ClientEventHandler.getRenderer().setItem(item);
            } else {
                Block block = ForgeRegistries.BLOCKS.getValue(this.block);
                ClientEventHandler.getRenderer().setBlock(block);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
