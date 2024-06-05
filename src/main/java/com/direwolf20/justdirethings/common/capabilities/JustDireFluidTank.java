package com.direwolf20.justdirethings.common.capabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class JustDireFluidTank extends FluidTank implements INBTSerializable<CompoundTag> {
    public JustDireFluidTank(int capacity) {
        super(capacity);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return super.writeToNBT(provider, new CompoundTag());
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        fluid = super.readFromNBT(provider, nbt).getFluid();
    }
}
