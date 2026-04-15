package com.direwolf20.justdirethings.common.blockentities.basebe;

import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public interface FluidMachineBE {
    default int getMaxMB() {
        return 8000;
    }

    ContainerData getFluidContainerData();

    FluidStacksResourceHandler getFluidTank();

    default FluidStack getFluidStack() {
        FluidStacksResourceHandler tank = getFluidTank();
        FluidResource resource = tank.getResource(0);
        int amount = tank.getAmountAsInt(0);
        if (resource.isEmpty() || amount <= 0) return FluidStack.EMPTY;
        return resource.toStack(amount);
    }

    default void setFluidStack(Fluid fluid, int amt) {
        FluidStacksResourceHandler tank = getFluidTank();
        try (Transaction tx = Transaction.openRoot()) {
            FluidResource current = tank.getResource(0);
            int currentAmt = tank.getAmountAsInt(0);
            if (currentAmt > 0) {
                tank.extract(0, current, currentAmt, tx);
            }
            if (fluid != null && amt > 0) {
                tank.insert(0, FluidResource.of(fluid), amt, tx);
            }
            tx.commit();
        }
    }

    default int getAmountStored() {
        return getFluidTank().getAmountAsInt(0);
    }

    default void setAmountStored(int value) {
        FluidStacksResourceHandler tank = getFluidTank();
        FluidResource resource = tank.getResource(0);
        if (resource.isEmpty()) return;
        try (Transaction tx = Transaction.openRoot()) {
            int current = tank.getAmountAsInt(0);
            if (value < current) {
                tank.extract(0, resource, current - value, tx);
            } else if (value > current) {
                tank.insert(0, resource, value - current, tx);
            }
            tx.commit();
        }
    }

    default boolean isFull() {
        return getAmountStored() >= getMaxMB();
    }
}
