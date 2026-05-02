package com.direwolf20.justdirethings.common.blockentities.basebe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.material.Fluid;

public class FluidContainerData implements ContainerData {
    FluidMachineBE fluidMachineBE;
    // Buffer the three incoming slots so the underlying stack-backed tank (which collapses
    // (fluid, 0) to EMPTY and loses fluid identity) doesn't drop the fluid type when the
    // fluid-id slot arrives before the amount slots on the first empty→filled sync.
    private int pendingFluidId;
    private int pendingAmount;

    public FluidContainerData(FluidMachineBE fluidMachineBE) {
        this.fluidMachineBE = fluidMachineBE;
        this.pendingFluidId = BuiltInRegistries.FLUID.getId(fluidMachineBE.getFluidStack().getFluid());
        this.pendingAmount = fluidMachineBE.getAmountStored();
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
            case 0 -> pendingFluidId = value;
            case 1 -> pendingAmount = (pendingAmount & 0xFFFF0000) | (value & 0xFFFF);
            case 2 -> pendingAmount = (pendingAmount & 0xFFFF) | (value << 16);
            default -> throw new IllegalArgumentException("Invalid index: " + index);
        }
        Fluid fluid = BuiltInRegistries.FLUID.byId(pendingFluidId);
        fluidMachineBE.setFluidStack(fluid, pendingAmount);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
