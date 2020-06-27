package me.geek.tom.lat.block.hudsign;

import me.geek.tom.lat.setup.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HudSignBlock extends Block {
    private static final VoxelShape NORTH_SIGN = Block.makeCuboidShape(1d, 4d, 16d, 15d, 12d, 15d);
    private static final VoxelShape SOUTH_SIGN = Block.makeCuboidShape(1d, 4d, 0d, 15d, 12d, 1d);
    private static final VoxelShape WEST_SIGN =  Block.makeCuboidShape(16, 4, 1, 15, 11, 14);
    private static final VoxelShape EAST_SIGN =  Block.makeCuboidShape(0, 4, 1, 1, 12, 15);
    private static final VoxelShape DEFAULT = Block.makeCuboidShape(0, 0, 0, 16, 16, 16);

    public HudSignBlock() {
        super(Block.Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .harvestTool(ToolType.PICKAXE)
                .hardnessAndResistance(5.0f, 5.0f)
                .func_235838_a_(v -> 14) // Light level
        );
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new HudSignTile();
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        switch (state.get(BlockStateProperties.HORIZONTAL_FACING)) {
            case NORTH:
                return NORTH_SIGN;
            case SOUTH:
                return SOUTH_SIGN;
            case EAST:
                return EAST_SIGN;
            case WEST:
                return WEST_SIGN;
            default:
            	break;
        }
        return DEFAULT;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (!world.isRemote)
            return ActionResultType.SUCCESS;

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientProxy.openHudSignGui(world, pos));

        return ActionResultType.SUCCESS;
    }
}
