package me.geek.tom.lat.blockinfo.impl;

import me.geek.tom.lat.blockinfo.api.BlockInfoLine;
import me.geek.tom.lat.blockinfo.api.IBlockInfoSupplier;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.function.Consumer;

public class ContainerInformationSupplier implements IBlockInfoSupplier {
    @Override
    public void addInfo(Consumer<BlockInfoLine> adder, BlockPos pos, BlockState state, World world) {
        TileEntity te = world.getTileEntity(pos);
        if (te == null) return;

        te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            int numSlots = handler.getSlots();
            int maxItems = 0;
            int totalItems = 0;
            for (int slot = 0; slot < numSlots; slot++) {
                maxItems += Math.min(handler.getSlotLimit(slot), handler.getStackInSlot(slot).getMaxStackSize());
                totalItems += handler.getStackInSlot(slot).getCount();
            }
            adder.accept(new BlockInfoLine(String.format("%.1f%% full", ((float)totalItems / maxItems) * 100), 0x888800));
        });
    }

    @Override
    public boolean shouldHandle(BlockPos pos, BlockState state, World world) {
        return world.getTileEntity(pos) != null;
    }
}
