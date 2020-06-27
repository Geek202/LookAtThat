package me.geek.tom.lat.blockinfo.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;

/**
 * Returns infomation about blocks when asked.
 */
public interface IBlockInfoSupplier {

    /**
     * Add information to this block.
     *  @param adder a consumer to add infomation
     * @param pos
     * @param state The block state.
     */
    void addInfo(Consumer<BlockInfoLine> adder, BlockPos pos, BlockState state, World world);

    /**
     * Can this block provide information about
     *
     *
     * @param pos
     * @param state The blockstate
     * @param world
     * @return true if you want to handle this block, false otherwise.
     */
    boolean shouldHandle(BlockPos pos, BlockState state, World world);
}
