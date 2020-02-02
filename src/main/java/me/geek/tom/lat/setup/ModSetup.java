package me.geek.tom.lat.setup;

import me.geek.tom.lat.LookAtThat;
import me.geek.tom.lat.block.hudsign.HudSignBlock;
import me.geek.tom.lat.block.hudsign.HudSignTile;
import me.geek.tom.lat.testingstuff.TestBlock;
import me.geek.tom.lat.testingstuff.TestTile;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@SuppressWarnings("ConstantConditions")
@ObjectHolder(LookAtThat.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {

    // OBJECT HOLDERS
    @ObjectHolder("testblock")
    private static TestBlock TESTBLOCK;

    @ObjectHolder("testblock")
    public static TileEntityType<TestTile> TESTBLOCK_TILE;

    @ObjectHolder("hud_sign")
    private static HudSignBlock HUDSIGN;

    @ObjectHolder("hud_sign")
    public static BlockItem HUDSIGN_ITEM;

    @ObjectHolder("hud_sign")
    public static TileEntityType<HudSignTile> HUDSIGN_TILE;

    // REGISTERING

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        System.out.println("Register blocks");
        event.getRegistry().registerAll(
                new TestBlock(),
                new HudSignBlock()
        );
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        System.out.println("Register items");
        Item.Properties properties = new Item.Properties().group(LookAtThat.INSTANCE.itemGroup);
        event.getRegistry().registerAll(
                new BlockItem(HUDSIGN, properties).setRegistryName("hud_sign")
        );
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        System.out.println("Register TE types");
        event.getRegistry().registerAll(
                TileEntityType.Builder.create(TestTile::new, TESTBLOCK).build(null).setRegistryName("testblock"),
                TileEntityType.Builder.create(HudSignTile::new, HUDSIGN).build(null).setRegistryName("hud_sign")
        );
    }
}
