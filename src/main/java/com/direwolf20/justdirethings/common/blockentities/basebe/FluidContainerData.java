package com.direwolf20.justdirethings.common.blockentities.basebe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.ContainerData;

public class FluidContainerData implements ContainerData {
    FluidMachineBE fluidMachineBE;

    public FluidContainerData(FluidMachineBE fluidMachineBE) {
        this.fluidMachineBE = fluidMachineBE;
    }

    @Override
    public int get(int index) {
        return switch (index) {
            case 0 -> BuiltInRegistries.FLUID.getId(fluidMachineBE.getFluidStack().getFluid());
            case 1 -> fluidMachineBE.getAmountStored() & 0xFFFF;
            case 2 -> fluidMachineBE.getAmountStored() >> 16;
            default -> throw new IllegalArgumentException("Invalid index: " + index);
        };
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case 0 ->
                    fluidMachineBE.setFluidStack(BuiltInRegistries.FLUID.byId(value), fluidMachineBE.getAmountStored()); //Double it'll be used by who knows
            case 1 ->
                    fluidMachineBE.setAmountStored((fluidMachineBE.getAmountStored() & 0xFFFF0000) | (value & 0xFFFF));
            case 2 -> fluidMachineBE.setAmountStored((fluidMachineBE.getAmountStored() & 0xFFFF) | (value << 16));
            default -> throw new IllegalArgumentException("Invalid index: " + index);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
