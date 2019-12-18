package me.geek.tom.lat;

import me.geek.tom.lat.modapi.CapabilityLATInfo;
import me.geek.tom.lat.networking.Networking;
import me.geek.tom.lat.setup.ClientProxy;
import me.geek.tom.lat.setup.IProxy;
import me.geek.tom.lat.setup.ModSetup;
import me.geek.tom.lat.setup.ServerProxy;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LookAtThat.MODID)
public class LookAtThat {
    public ItemGroup itemGroup = new ItemGroup("lookatthat") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModSetup.HUDSIGN_ITEM);
        }
    };

    public static final String MODID = "lookatthat";

    private IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static LookAtThat INSTANCE;

    public LookAtThat() {
        INSTANCE = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        proxy.init();
        Networking.registerMessages();
        CapabilityLATInfo.register();
    }
}
