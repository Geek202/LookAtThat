package me.geek.tom.lat.blockinfo.impl;

import me.geek.tom.lat.blockinfo.api.BlockInfoLine;
import me.geek.tom.lat.blockinfo.api.IBlockInfoSupplier;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;

import static me.geek.tom.lat.blockinfo.InformationGatherer.getModName;

public class BasicInformationSupplier implements IBlockInfoSupplier {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void addInfo(Consumer<BlockInfoLine> adder, BlockPos pos, BlockState state, World world) {
        ItemStack stack = new ItemStack(state.getBlock());

        String displayName = stack.getDisplayName().getString();
        String modName = getModName(stack);
        if (modName == null)
            modName = state.getBlock().getRegistryName().getNamespace().toUpperCase();

        adder.accept(new BlockInfoLine(displayName, 0xFFFFFF));
        adder.accept(new BlockInfoLine(modName, 0x0088FF));
    }

    @Override
    public boolean shouldHandle(BlockPos pos, BlockState state, World world) {
        return true; // We want to handle all blocks.
    }
}
