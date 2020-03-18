package me.geek.tom.lat.networking;

import me.geek.tom.lat.modapi.CapabilityLATInfo;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class PacketRequestBlockInfo {
    private final DimensionType dim;
    private final BlockPos pos;

    public PacketRequestBlockInfo(PacketBuffer buf) {
        dim = DimensionType.getById(buf.readInt());
        pos = buf.readBlockPos();
    }

    public PacketRequestBlockInfo(DimensionType type, BlockPos pos) {
        this.dim = type;
        this.pos = pos;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(dim.getId());
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerWorld world = ctx.get().getSender().world.getServer().getWorld(dim);

            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            Item item;

            if (block instanceof AbstractBannerBlock)
                item = Items.WHITE_BANNER;
            else if (block instanceof AttachedStemBlock)
                item = getGrownFruit((AttachedStemBlock) block);
            else if (block instanceof StemBlock)
                item = getSeedItem((StemBlock) block);
            else
                item = block.getItem(world, pos, state).getItem();

            ResourceLocation type;
            boolean isItem;

            if (!item.equals(Items.AIR)) {
                type = item.getRegistryName();
                isItem = true;
            } else {
                type = state.getBlock().getRegistryName();
                isItem = false;
            }

            TileEntity te = world.getTileEntity(pos);
            AtomicReference<String> additional = new AtomicReference<>("");
            if (te != null) {
                AtomicBoolean shouldCheckCustom = new AtomicBoolean(true);
                te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                    int numSlots = handler.getSlots();
                    int maxItems = 0;
                    int totalItems = 0;
                    for (int slot = 0; slot < numSlots; slot++) {
                        maxItems += Math.min(handler.getSlotLimit(slot), handler.getStackInSlot(slot).getMaxStackSize());
                        totalItems += handler.getStackInSlot(slot).getCount();
                    }
                    additional.set(String.format("%.1f%% full", ((float)totalItems / maxItems) * 100));
                    shouldCheckCustom.set(false);
                });

                if (shouldCheckCustom.get()) {
                    te.getCapability(CapabilityLATInfo.LAT_INFO_CAPABILITY).ifPresent(handler -> {
                        additional.set(handler.getInfo());
                    });
                }
            }

            Networking.INSTANCE.sendTo(
                    new PacketBlockType(type, isItem),
                    ctx.get().getSender().connection.netManager,
                    NetworkDirection.PLAY_TO_CLIENT
            );

            if (!additional.get().equals(""))
                Networking.INSTANCE.sendTo(
                        new PacketAdditionalData(additional.get()),
                        ctx.get().getSender().connection.netManager,
                        NetworkDirection.PLAY_TO_CLIENT
                );
        });
        ctx.get().setPacketHandled(true);
    }

    private Item getGrownFruit(AttachedStemBlock block) {
        Field field;
        try {
            field = AttachedStemBlock.class.getDeclaredField("grownFruit");
        } catch (NoSuchFieldException e) {
            try {
                //noinspection JavaReflectionMemberAccess
                field = AttachedStemBlock.class.getDeclaredField("field_196281_b");
            } catch (NoSuchFieldException ex) {
                ex.printStackTrace();
                throw new IllegalStateException("Field not found!");
            }
        }
        field.setAccessible(true);

        try {
            return Item.getItemFromBlock((Block) field.get(block));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private Item getSeedItem(StemBlock block) {
        if (block.getCrop() == Blocks.PUMPKIN) {
            return Items.PUMPKIN_SEEDS;
        } else {
            return block.getCrop() == Blocks.MELON ? Items.MELON_SEEDS : null;
        }
    }
}
