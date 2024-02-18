package com.direwolf20.justdirethings.setup;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final String CATEGORY_GENERAL = "general";

    public static void register() {
        //registerServerConfigs();
        registerCommonConfigs();
        //registerClientConfigs();
    }

    private static void registerClientConfigs() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_BUILDER.build());
    }

    private static void registerCommonConfigs() {
        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        generalConfig();
        COMMON_BUILDER.pop();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_BUILDER.build());
    }

    private static void registerServerConfigs() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
    }

    private static void generalConfig() {

    }
}
