package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.setup.Config;

public class PocketGeneratorT3 extends PocketGenerator {
    public PocketGeneratorT3() {
        super();
    }

    @Override
    public int getMaxEnergy() {
        return Config.POCKET_GENERATOR_T3_MAX_FE.get();
    }

    public int getFEPerTick() {
        return Config.POCKET_GENERATOR_T3_FE_PER_TICK.get();
    }

    public int getFePerFuelTick() {
        return Config.POCKET_GENERATOR_T3_FE_PER_FUEL_TICK.get();
    }

    public int getBurnSpeedMultiplier() {
        return Config.POCKET_GENERATOR_T3_BURN_SPEED_MULTIPLIER.get();
    }
}
