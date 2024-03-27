package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.setup.Config;

public class PocketGeneratorT4 extends PocketGenerator {
    public PocketGeneratorT4() {
        super();
    }

    @Override
    public int getMaxEnergy() {
        return Config.POCKET_GENERATOR_T4_MAX_FE.get();
    }

    public int getFEPerTick() {
        return Config.POCKET_GENERATOR_T4_FE_PER_TICK.get();
    }

    public double getFePerFuelTick() {
        return Config.POCKET_GENERATOR_T4_FE_PER_FUEL_TICK.get();
    }

    public int getBurnSpeedMultiplier() {
        return Config.POCKET_GENERATOR_T4_BURN_SPEED_MULTIPLIER.get();
    }
}
