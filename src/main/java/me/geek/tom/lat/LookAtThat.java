package me.geek.tom.lat;

import me.geek.tom.lat.blockinfo.InformationGatherer;
import me.geek.tom.lat.modapi.CapabilityLATInfo;
import me.geek.tom.lat.networking.Networking;
import me.geek.tom.lat.setup.ModSetup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LookAtThat.MODID)
public class LookAtThat {
    public ItemGroup itemGroup = new ItemGroup("lookatthat") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModSetup.HUDSIGN_ITEM.get());
        }
    };

    public static final String MODID = "lookatthat";

    public static LookAtThat INSTANCE;

    public LookAtThat() {
        INSTANCE = this;

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        ModSetup.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        Networking.registerMessages();
        CapabilityLATInfo.register();
        StartupMessageManager.addModMessage("LookAtThat::init");
        InformationGatherer.fireRegisterEvent();
    }
}
