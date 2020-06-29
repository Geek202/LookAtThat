package me.geek.tom.lat;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static String CATEGORY_GENERAL = "general";

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue CENTER_HUD;
    public static ForgeConfigSpec.BooleanValue SHOW_HARVESTABILITY;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("General settings").push(CATEGORY_GENERAL);

        CENTER_HUD = CLIENT_BUILDER.comment("Whether or not the HUD will be centered or to the left on the screen.")
                .define("centerHud", false);

        SHOW_HARVESTABILITY = CLIENT_BUILDER.comment("Should the overlay show harvestability.")
                .define("harvestability", true);

        CLIENT_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

}
