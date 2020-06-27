package me.geek.tom.lat.blockinfo.impl;

import me.geek.tom.lat.blockinfo.api.BlockInfoLine;
import me.geek.tom.lat.blockinfo.api.IBlockInfoSupplier;
import me.geek.tom.lat.modapi.CapabilityLATInfo;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class CapabilityInformationSupplier implements IBlockInfoSupplier {
    @Override
    public void addInfo(Consumer<BlockInfoLine> adder, BlockPos pos, BlockState state, World world) {
        TileEntity te = world.getTileEntity(pos);
        if (te == null) return;

        te.getCapability(CapabilityLATInfo.LAT_INFO_CAPABILITY).ifPresent(
                handler -> adder.accept(new BlockInfoLine(handler.getInfo(), 0x888800)));
    }

    @Override
    public boolean shouldHandle(BlockPos pos, BlockState state, World world) {
        return world.getTileEntity(pos) != null;
    }
}
