package me.geek.tom.lat.setup;

import me.geek.tom.lat.LookAtThat;
import me.geek.tom.lat.block.hudsign.HudSignBlock;
import me.geek.tom.lat.block.hudsign.HudSignTile;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static me.geek.tom.lat.LookAtThat.MODID;

@SuppressWarnings({"ConstantConditions", "unused"})
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);

    private static final RegistryObject<HudSignBlock> HUDSIGN = BLOCKS.register("hud_sign", HudSignBlock::new);

    public static final RegistryObject<Item> HUDSIGN_ITEM = ITEMS.register("hud_sign",
            () -> new BlockItem(HUDSIGN.get(), new Item.Properties().group(LookAtThat.INSTANCE.itemGroup)));

    public static final RegistryObject<TileEntityType<HudSignTile>> HUDSIGN_TILE = TILES.register("hud_sign",
            () -> TileEntityType.Builder.create(HudSignTile::new, HUDSIGN.get()).build(null));

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
