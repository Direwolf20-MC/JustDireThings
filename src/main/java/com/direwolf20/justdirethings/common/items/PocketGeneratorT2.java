package com.direwolf20.justdirethings.common.items;

import com.direwolf20.justdirethings.setup.Config;

public class PocketGeneratorT2 extends PocketGenerator {
    public PocketGeneratorT2() {
        super();
    }

    @Override
    public int getMaxEnergy() {
        return Config.POCKET_GENERATOR_T2_MAX_FE.get();
    }

    public int getFEPerTick() {
        return Config.POCKET_GENERATOR_T2_FE_PER_TICK.get();
    }

    public int getFePerFuelTick() {
        return Config.POCKET_GENERATOR_T2_FE_PER_FUEL_TICK.get();
    }

    public int getBurnSpeedMultiplier() {
        return Config.POCKET_GENERATOR_T2_BURN_SPEED_MULTIPLIER.get();
    }
}
