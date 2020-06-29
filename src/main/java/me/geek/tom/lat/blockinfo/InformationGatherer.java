package me.geek.tom.lat.blockinfo;

import me.geek.tom.lat.blockinfo.api.BlockInformation;
import me.geek.tom.lat.blockinfo.api.IBlockInfoSupplier;
import me.geek.tom.lat.blockinfo.impl.BasicInformationSupplier;
import me.geek.tom.lat.blockinfo.impl.CapabilityInformationSupplier;
import me.geek.tom.lat.blockinfo.impl.ContainerInformationSupplier;
import me.geek.tom.lat.event.RegisterInfoSuppliersEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InformationGatherer {

    private static final List<IBlockInfoSupplier> blockInfoSuppliers = new ArrayList<>();

    private static void registerBlockInfoSupplier(IBlockInfoSupplier supplier) {
        if (!blockInfoSuppliers.contains(supplier))
            blockInfoSuppliers.add(supplier);
    }

    public static void fireRegisterEvent() {
        blockInfoSuppliers.add(new BasicInformationSupplier());
        blockInfoSuppliers.add(new ContainerInformationSupplier());
        blockInfoSuppliers.add(new CapabilityInformationSupplier());

        MinecraftForge.EVENT_BUS.post(new RegisterInfoSuppliersEvent(InformationGatherer::registerBlockInfoSupplier));
    }

    public static BlockInformation gatherInformation(World world, BlockPos pos, ServerPlayerEntity sender) {
        BlockState state = world.getBlockState(pos);

        //boolean canHarvest = state.canHarvestBlock(world, pos, sender);
        boolean canHarvest;

        ItemStack stack = sender.getHeldItemMainhand();
        ToolType tool = state.getHarvestTool();
        if (stack.isEmpty() || tool == null) {
            canHarvest = state.getBlockHardness(world, pos) >= 0;
        } else {
            int toolLevel = stack.getItem().getHarvestLevel(stack, tool, sender, state);
            canHarvest = toolLevel >= state.getHarvestLevel();
        }

        BlockInformation info = new BlockInformation(canHarvest);

        for (IBlockInfoSupplier supplier : blockInfoSuppliers) {
            if (supplier.shouldHandle(pos, state, world))
                supplier.addInfo(info::addInformation, pos, state, world);
        }

        return info;
    }

    @Nullable
    public static String getModName(ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            String modId = item.getCreatorModId(itemStack);
            if (modId != null) {
                return ModList.get().getModContainerById(modId)
                        .map(modContainer -> modContainer.getModInfo().getDisplayName())
                        .orElse(StringUtils.capitalize(modId));
            }
        }
        return null;
    }

}
