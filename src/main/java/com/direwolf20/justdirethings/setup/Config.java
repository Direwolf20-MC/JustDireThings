package com.direwolf20.justdirethings.setup;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final String CATEGORY_GENERAL = "general";

    public static final String CATEGORY_FUEL_CANISTER = "fuel_canister";
    public static ModConfigSpec.IntValue FUEL_CANISTER_MINIMUM_TICKS_CONSUMED;
    public static ModConfigSpec.IntValue FUEL_CANISTER_MAXIMUM_FUEL;

    public static final String CATEGORY_POCKET_GENERATOR = "pocket_generator";
    public static ModConfigSpec.DoubleValue POCKET_GENERATOR_FE_PER_FUEL_TICK;
    public static ModConfigSpec.IntValue POCKET_GENERATOR_BURN_SPEED_MULTIPLIER;
    public static ModConfigSpec.IntValue POCKET_GENERATOR_MAX_FE;
    public static ModConfigSpec.IntValue POCKET_GENERATOR_FE_PER_TICK;
    public static ModConfigSpec.DoubleValue POCKET_GENERATOR_T2_FE_PER_FUEL_TICK;
    public static ModConfigSpec.IntValue POCKET_GENERATOR_T2_BURN_SPEED_MULTIPLIER;
    public static ModConfigSpec.IntValue POCKET_GENERATOR_T2_MAX_FE;
    public static ModConfigSpec.IntValue POCKET_GENERATOR_T2_FE_PER_TICK;


    public static void register() {
        //registerServerConfigs();
        registerCommonConfigs();
        //registerClientConfigs();
    }

    private static void registerClientConfigs() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_BUILDER.build());
    }

    private static void registerCommonConfigs() {
        generalConfig();
        fuelCanisterConfig();
        pocketGeneratorConfig();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_BUILDER.build());
    }

    private static void registerServerConfigs() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
    }

    private static void generalConfig() {
        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);

        COMMON_BUILDER.pop();
    }

    private static void fuelCanisterConfig() {
        COMMON_BUILDER.comment("Fuel Canister").push(CATEGORY_FUEL_CANISTER);
        FUEL_CANISTER_MINIMUM_TICKS_CONSUMED = COMMON_BUILDER.comment("The amount of ticks 'consumed' per operation in the furnace. Lower is more efficient fuel use.")
                .defineInRange("fuel_canister_minimum_ticks_consumed", 50, 1, Integer.MAX_VALUE);
        FUEL_CANISTER_MAXIMUM_FUEL = COMMON_BUILDER.comment("The maximum amount of fuel (in ticks) permitted in the fuel canister.")
                .defineInRange("fuel_canister_maximum_fuel", 2000000, 100, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
    }

    private static void pocketGeneratorConfig() {
        COMMON_BUILDER.comment("Pocket Generator").push(CATEGORY_POCKET_GENERATOR);
        POCKET_GENERATOR_FE_PER_FUEL_TICK = COMMON_BUILDER.comment("The amount of Forge Energy created per burn tick of fuel. Coal has 1600 burn ticks. Sticks have 100 burn ticks.")
                .defineInRange("pocket_generator_fe_per_fuel_tick", 12.5, 1, Integer.MAX_VALUE);
        POCKET_GENERATOR_BURN_SPEED_MULTIPLIER = COMMON_BUILDER.comment("The multiplier for the burn speed, making the generator run faster. Coal is 1600 ticks to burn, if you set this to 10, it will burn in 160 ticks")
                .defineInRange("pocket_generator_burn_speed_multiplier", 2, 1, 1000);
        POCKET_GENERATOR_MAX_FE = COMMON_BUILDER.comment("The maximum amount of Forge Energy the generator can hold in its buffer")
                .defineInRange("pocket_generator_max_fe", 50000, 1, Integer.MAX_VALUE);
        POCKET_GENERATOR_FE_PER_TICK = COMMON_BUILDER.comment("The FE per Tick that the generator charges other items at")
                .defineInRange("pocket_generator_fe_per_tick", 150, 1, Integer.MAX_VALUE);
        POCKET_GENERATOR_T2_FE_PER_FUEL_TICK = COMMON_BUILDER.comment("The amount of Forge Energy created per burn tick of fuel. Coal has 1600 burn ticks. Sticks have 100 burn ticks.")
                .defineInRange("pocket_generator_t2_fe_per_fuel_tick", 12.5, 1, Integer.MAX_VALUE);
        POCKET_GENERATOR_T2_BURN_SPEED_MULTIPLIER = COMMON_BUILDER.comment("The multiplier for the burn speed, making the generator run faster. Coal is 1600 ticks to burn, if you set this to 10, it will burn in 160 ticks")
                .defineInRange("pocket_generator_t2_burn_speed_multiplier", 10, 1, 1000);
        POCKET_GENERATOR_T2_MAX_FE = COMMON_BUILDER.comment("The maximum amount of Forge Energy the generator can hold in its buffer")
                .defineInRange("pocket_generator_t2_max_fe", 500000, 1, Integer.MAX_VALUE);
        POCKET_GENERATOR_T2_FE_PER_TICK = COMMON_BUILDER.comment("The FE per Tick that the generator charges other items at")
                .defineInRange("pocket_generator_t2_fe_per_tick", 1000, 1, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
    }
}
