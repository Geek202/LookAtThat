package me.geek.tom.lat.block.hudsign;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
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
                .lightValue(14)
        );
        setRegistryName("hud_sign");
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
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nonnull
    @Override
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

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (!world.isRemote)
            return true;

        Minecraft.getInstance().displayGuiScreen(new EditHudSignScreen());

        return true;
    }
}
